#!/usr/bin/env bash
set -euo pipefail

##############################
# Configurable defaults
##############################
APP_NAME="shoppinglist"
APP_USER="shoppinglist"
APP_GROUP="shoppinglist"
APP_DIR="/opt/shoppinglist"
APP_JAR="${APP_DIR}/shoppinglist-backend.jar"
APP_PORT="8080"
JAVA_BIN="/opt/jdk-21.0.7+6/bin/java"

SYSTEMCTL_BIN="/usr/bin/systemctl"

NGINX_SITE_AVAILABLE="/etc/nginx/sites-available/shoppinglist"
NGINX_SITE_ENABLED="/etc/nginx/sites-enabled/shoppinglist"

SYSTEMD_UNIT_PATH="/etc/systemd/system/${APP_NAME}-backend.service"

log() { echo -e "\n[setup] $*\n"; }

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || {
    echo "[setup] ERROR: missing command: $1" >&2
    exit 1
  }
}

require_executable() {
  local path="$1"
  if [[ ! -x "${path}" ]]; then
    echo "[setup] ERROR: executable not found or not executable: ${path}" >&2
    exit 1
  fi
}

ensure_group() {
  if getent group "${APP_GROUP}" >/dev/null 2>&1; then
    log "Group exists: ${APP_GROUP}"
    return 0
  fi

  log "Creating system group: ${APP_GROUP}"
  sudo groupadd --system "${APP_GROUP}"
}

ensure_user() {
  if id -u "${APP_USER}" >/dev/null 2>&1; then
    log "User exists: ${APP_USER}"
    return 0
  fi

  log "Creating system user: ${APP_USER}"
  if getent group "${APP_GROUP}" >/dev/null 2>&1; then
    sudo useradd --system --no-create-home --shell /usr/sbin/nologin -g "${APP_GROUP}" "${APP_USER}"
  else
    sudo useradd --system --no-create-home --shell /usr/sbin/nologin "${APP_USER}"
  fi
}

ensure_user_in_group() {
  if id -nG "${APP_USER}" | tr ' ' '\n' | grep -q "^${APP_GROUP}$"; then
    return 0
  fi

  log "Adding ${APP_USER} to group ${APP_GROUP}"
  sudo usermod -a -G "${APP_GROUP}" "${APP_USER}"
}

ensure_app_dir() {
  log "Ensuring app directory: ${APP_DIR}"
  sudo mkdir -p "${APP_DIR}"
  sudo chown -R "${APP_USER}:${APP_GROUP}" "${APP_DIR}"
}

write_systemd_unit() {
  log "Writing systemd unit: ${SYSTEMD_UNIT_PATH}"

  sudo tee "${SYSTEMD_UNIT_PATH}" >/dev/null <<EOF
[Unit]
Description=Shopping List Backend
After=network.target

[Service]
Type=simple
User=${APP_USER}
Group=${APP_GROUP}
WorkingDirectory=${APP_DIR}
ExecStart=${JAVA_BIN} -jar ${APP_JAR} --server.port=${APP_PORT}
Restart=on-failure
RestartSec=5
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF
}

enable_backend_service() {
  log "Enabling backend service"
  sudo "${SYSTEMCTL_BIN}" daemon-reload
  sudo "${SYSTEMCTL_BIN}" enable "${APP_NAME}-backend"
  sudo "${SYSTEMCTL_BIN}" restart "${APP_NAME}-backend"
}

enable_nginx_site() {
  if [[ ! -f "${NGINX_SITE_AVAILABLE}" ]]; then
    echo "[setup] ERROR: nginx site config not found: ${NGINX_SITE_AVAILABLE}" >&2
    exit 1
  fi

  if [[ ! -L "${NGINX_SITE_ENABLED}" ]]; then
    log "Enabling nginx site: ${NGINX_SITE_ENABLED}"
    sudo ln -s "${NGINX_SITE_AVAILABLE}" "${NGINX_SITE_ENABLED}"
  else
    log "Nginx site already enabled: ${NGINX_SITE_ENABLED}"
  fi
}

reload_nginx() {
  log "Reloading nginx"
  sudo "${SYSTEMCTL_BIN}" enable nginx
  sudo "${SYSTEMCTL_BIN}" restart nginx
}

main() {
  require_cmd sudo
  require_executable "${SYSTEMCTL_BIN}"
  require_executable "${JAVA_BIN}"

  ensure_group
  ensure_user
  ensure_user_in_group
  ensure_app_dir

  if [[ ! -f "${APP_JAR}" ]]; then
    echo "[setup] WARNING: Backend jar not found at ${APP_JAR}" >&2
  fi

  write_systemd_unit
  enable_backend_service

  enable_nginx_site
  reload_nginx

  log "Done."
}

main "$@"
