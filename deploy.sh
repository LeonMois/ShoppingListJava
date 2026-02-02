#!/usr/bin/env bash
set -euo pipefail

#############################################
# CONFIG — adjust these to your repo layout #
#############################################

# Repo paths (relative to repo root)
FRONTEND_DIR="shoppinglist-frontend"     # Angular project folder
BACKEND_DIR="backend"                   # Spring Boot project folder

# Deploy targets on the Raspberry Pi
FRONTEND_TARGET="/var/www/html"
BACKEND_TARGET_DIR="/opt/shoppinglist"
BACKEND_TARGET_JAR="${BACKEND_TARGET_DIR}/shoppinglist-backend.jar"

# systemd service name for Spring Boot (optional)
BACKEND_SERVICE="shoppinglist-backend"
NGINX_SERVICE="nginx"

# Angular build output directory:
# common: dist/<appname>/ OR dist/<appname>/browser (SSR)
# We'll auto-detect if possible, else you can hardcode
ANGULAR_DIST_DIR=""


#############################################
# Helpers                                   #
#############################################
log() { echo -e "\n[deploy] $*\n" >&2; }

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || {
    echo "[deploy] ERROR: missing command: $1" >&2
    exit 1
  }
}

has_systemd_service() {
  local service="$1"
  if ! command -v systemctl >/dev/null 2>&1; then
    return 1
  fi
  systemctl list-unit-files --type=service --no-pager 2>/dev/null | grep -q "^${service}\.service"
}

repo_root() {
  git rev-parse --show-toplevel 2>/dev/null || pwd
}

detect_angular_dist() {
  # If user explicitly set it, use it
  if [[ -n "${ANGULAR_DIST_DIR}" ]]; then
    echo "${ANGULAR_DIST_DIR}"
    return 0
  fi

  # Try to infer Angular dist folder:
  # - dist/*/browser exists (SSR builds)
  # - else dist/* exists
  local dist_base="${1}/dist"
  if [[ ! -d "${dist_base}" ]]; then
    echo ""
    return 0
  fi

  local browser_dir
  browser_dir="$(find "${dist_base}" -maxdepth 2 -type d -name browser 2>/dev/null | head -n 1 || true)"
  if [[ -n "${browser_dir}" ]]; then
    echo "${browser_dir}"
    return 0
  fi

  local app_dir
  app_dir="$(find "${dist_base}" -mindepth 1 -maxdepth 1 -type d 2>/dev/null | head -n 1 || true)"
  if [[ -n "${app_dir}" ]]; then
    echo "${app_dir}"
    return 0
  fi

  echo ""
}

build_frontend() {
  local root="$1"
  local fe_path="${root}/${FRONTEND_DIR}"

  log "Building Angular frontend in: ${fe_path}"

  if [[ ! -d "${fe_path}" ]]; then
    echo "[deploy] ERROR: FRONTEND_DIR not found: ${fe_path}" >&2
    exit 1
  fi

  pushd "${fe_path}" >/dev/null

  # Use npm ci if lockfile exists, else npm install
  if [[ -f package-lock.json ]]; then
    require_cmd npm
    npm ci >&2
  else
    require_cmd npm
    npm install >&2
  fi

  # Prefer npm run build (works for most Angular setups)
  npm run build >&2

  local dist_dir
  dist_dir="$(detect_angular_dist "${fe_path}")"

  if [[ -z "${dist_dir}" ]]; then
    echo "[deploy] ERROR: Could not detect Angular dist output folder. Set ANGULAR_DIST_DIR in script." >&2
    exit 1
  fi

  popd >/dev/null

  echo "${dist_dir}"
}

deploy_frontend() {
  local dist_dir="$1"

  log "Deploying frontend from: ${dist_dir} -> ${FRONTEND_TARGET}"

  # Ensure target exists
  sudo mkdir -p "${FRONTEND_TARGET}"

  # Sync build output to nginx root
  sudo rsync -a --delete "${dist_dir}/" "${FRONTEND_TARGET}/"

  # Reload nginx to pick up changes (not strictly required for static files, but harmless)
  sudo systemctl reload "${NGINX_SERVICE}"
}

build_backend() {
  local root="$1"
  local be_path="${root}/${BACKEND_DIR}"

  log "Building Spring Boot backend in: ${be_path}"

  if [[ ! -d "${be_path}" ]]; then
    echo "[deploy] ERROR: BACKEND_DIR not found: ${be_path}" >&2
    exit 1
  fi

  pushd "${be_path}" >/dev/null

  # Choose Gradle if present, else Maven
  local jar_path=""

  if [[ -f "./gradlew" ]]; then
    log "Detected Gradle wrapper. Running: ./gradlew clean bootJar"
    chmod +x ./gradlew
    ./gradlew clean bootJar >&2

    # Find the newest jar in build/libs (exclude *plain.jar if possible)
    jar_path="$(ls -1t build/libs/*.jar 2>/dev/null | grep -v 'plain\.jar' | head -n 1 || true)"
    if [[ -z "${jar_path}" ]]; then
      jar_path="$(ls -1t build/libs/*.jar 2>/dev/null | head -n 1 || true)"
    fi

  elif [[ -f "./mvnw" || -f "pom.xml" ]]; then
    if [[ -f "./mvnw" ]]; then
      log "Detected Maven wrapper. Running: ./mvnw -DskipTests package"
      chmod +x ./mvnw
      ./mvnw -DskipTests package >&2
    else
      require_cmd mvn
      log "Detected Maven. Running: mvn -DskipTests package"
      mvn -DskipTests package >&2
    fi

    # Find the newest jar in target (exclude *original.jar if possible)
    jar_path="$(ls -1t target/*.jar 2>/dev/null | grep -v 'original\.jar' | head -n 1 || true)"
    if [[ -z "${jar_path}" ]]; then
      jar_path="$(ls -1t target/*.jar 2>/dev/null | head -n 1 || true)"
    fi
  else
    echo "[deploy] ERROR: Could not detect Gradle or Maven project in ${be_path}" >&2
    exit 1
  fi

  if [[ -z "${jar_path}" ]]; then
    echo "[deploy] ERROR: Could not find built JAR." >&2
    exit 1
  fi

  # Make jar path absolute for return
  jar_path="$(cd "$(dirname "${jar_path}")" && pwd)/$(basename "${jar_path}")"

  popd >/dev/null
  echo "${jar_path}"
}

deploy_backend() {
  local jar_path="$1"

  log "Deploying backend from: ${jar_path} -> ${BACKEND_TARGET_JAR}"

  sudo mkdir -p "${BACKEND_TARGET_DIR}"

  # Copy jar atomically to reduce risk of partial reads
  local tmp_jar="${BACKEND_TARGET_DIR}/.myapp.jar.tmp"
  sudo cp "${jar_path}" "${tmp_jar}"
  sudo mv "${tmp_jar}" "${BACKEND_TARGET_JAR}"

  # Restart backend service
  if [[ -n "${BACKEND_SERVICE}" ]] && has_systemd_service "${BACKEND_SERVICE}"; then
    sudo systemctl restart "${BACKEND_SERVICE}"
  else
    log "Skipping backend restart (systemd service not found): ${BACKEND_SERVICE}"
  fi
}

status_check() {
  log "Service status"
  if [[ -n "${BACKEND_SERVICE}" ]] && has_systemd_service "${BACKEND_SERVICE}"; then
    sudo systemctl --no-pager --full status "${BACKEND_SERVICE}" || true
  else
    log "Skipping backend status (systemd service not found): ${BACKEND_SERVICE}"
  fi

  if has_systemd_service "${NGINX_SERVICE}"; then
    sudo systemctl --no-pager --full status "${NGINX_SERVICE}" || true
  else
    log "Skipping nginx status (systemd service not found): ${NGINX_SERVICE}"
  fi

  log "Listening ports (quick check)"
  ss -tulpn | grep -E ':(80|443|8080)\b' || true
}

#############################################
# Main                                      #
#############################################
main() {
  local root
  root="$(repo_root)"
  log "Repo root: ${root}"

  require_cmd rsync
  require_cmd sudo
  require_cmd git
  require_cmd ss

  # Build + deploy frontend
  local dist_dir
  dist_dir="$(build_frontend "${root}")"
  deploy_frontend "${dist_dir}"

  # Build + deploy backend
  local jar_path
  jar_path="$(build_backend "${root}")"
  deploy_backend "${jar_path}"

  status_check

  log "Done."
}

main "$@"
