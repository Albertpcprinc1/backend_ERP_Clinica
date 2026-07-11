#!/usr/bin/env bash
set -e

echo "== ERP Clinica San Fernando - Preparacion servidor EC2 =="

sudo apt update
sudo apt upgrade -y

sudo apt install -y \
  ca-certificates \
  curl \
  gnupg \
  lsb-release \
  ufw \
  fail2ban \
  nginx \
  certbot \
  python3-certbot-nginx \
  unzip \
  git \
  htop

echo "== Instalando Docker =="
if ! command -v docker >/dev/null 2>&1; then
  curl -fsSL https://get.docker.com | sh
fi

sudo usermod -aG docker "$USER"

echo "== Habilitando servicios =="
sudo systemctl enable nginx
sudo systemctl enable fail2ban
sudo systemctl restart nginx
sudo systemctl restart fail2ban

echo "== Preparando carpetas web =="
sudo mkdir -p /var/www/sanfernando/landing
sudo mkdir -p /var/www/sanfernando/intranet
sudo mkdir -p /var/www/sanfernando/paciente
sudo mkdir -p /opt/sanfernando/backend
sudo mkdir -p /opt/sanfernando/backups/postgres

sudo chown -R "$USER":"$USER" /var/www/sanfernando
sudo chown -R "$USER":"$USER" /opt/sanfernando

echo "== Firewall UFW =="
sudo ufw allow OpenSSH
sudo ufw allow "Nginx Full"
sudo ufw --force enable
sudo ufw status verbose

echo "== Versiones =="
docker --version
docker compose version || true
nginx -v

echo "Servidor preparado. Cierre sesion y vuelva a entrar para activar permisos Docker."