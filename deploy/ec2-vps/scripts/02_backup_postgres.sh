#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-/opt/sanfernando/backend/repo/deploy/ec2-vps}"
BACKUP_DIR="${BACKUP_DIR:-/opt/sanfernando/backups/postgres}"
RETENTION_DAYS="${RETENTION_DAYS:-14}"

echo "== Backup PostgreSQL - ERP Clinica San Fernando =="

cd "$APP_DIR"

if [ ! -f ".env" ]; then
  echo "ERROR: No existe .env en $APP_DIR"
  exit 1
fi

if [ ! -f "docker-compose.yml" ]; then
  echo "ERROR: No existe docker-compose.yml en $APP_DIR"
  exit 1
fi

set -a
. ./.env
set +a

mkdir -p "$BACKUP_DIR"

STAMP="$(date +%Y%m%d_%H%M%S)"
BACKUP_SQL="$BACKUP_DIR/erp_clinica_principal_${STAMP}.sql"
BACKUP_GZ="$BACKUP_SQL.gz"

echo "== Validando contenedores =="
docker compose ps

echo "== Generando dump PostgreSQL =="
docker compose exec -T postgres pg_dump -U "$POSTGRES_USER" "$POSTGRES_DB" > "$BACKUP_SQL"

echo "== Comprimiendo backup =="
gzip "$BACKUP_SQL"

echo "== Validando integridad gzip =="
gunzip -t "$BACKUP_GZ"

echo "== Limpiando backups mayores a $RETENTION_DAYS dias =="
find "$BACKUP_DIR" -type f -name "erp_clinica_principal_*.sql.gz" -mtime +"$RETENTION_DAYS" -print -delete || true

echo "BACKUP_OK: $BACKUP_GZ"
ls -lh "$BACKUP_DIR"