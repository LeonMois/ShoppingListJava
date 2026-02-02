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

ensure_user() {
  if id -u "${APP_USER}" >/dev/null 2>&1; then
    log "User exists: ${APP_USER}"
    return 0
  fi

  log "Creating system user: ${APP_USER}"
  sudo useradd --system --no-create-home --shell /usr/sbin/nologin "${APP_USER}"
}

ensure_group() {
  if getent group "${APP_GROUP}" >/dev/null 2>&1; then
    log "Group exists: ${APP_GROUP}"
    return 0
  fi

  log "Creating system group: ${APP_GROUP}"
  sudo groupadd --system "${APP_GROUP}"
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
ExecStart=/usr/bin/java -jar ${APP_JAR} --server.port=${APP_PORT}
Restart=on-failure
RestartSec=5
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
EOF
}

enable_backend_service() {
  log "Enabling backend service"
  sudo systemctl daemon-reload
  sudo systemctl enable "${APP_NAME}-backend"
  sudo systemctl restart "${APP_NAME}-backend"
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
  sudo systemctl enable nginx
  sudo systemctl restart nginx
}

main() {
  require_cmd sudo
  require_cmd systemctl
  require_cmd java

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
