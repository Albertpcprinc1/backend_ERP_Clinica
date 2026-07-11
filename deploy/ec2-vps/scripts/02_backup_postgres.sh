#!/usr/bin/env bash
set -e

APP_DIR="/opt/sanfernando/backend"
BACKUP_DIR="/opt/sanfernando/backups/postgres"
STAMP="$(date +%Y%m%d_%H%M%S)"

cd "$APP_DIR"

mkdir -p "$BACKUP_DIR"

echo "== Creando backup PostgreSQL =="
docker compose exec -T postgres pg_dump -U "$POSTGRES_USER" "$POSTGRES_DB" > "$BACKUP_DIR/erp_clinica_principal_$STAMP.sql"

gzip "$BACKUP_DIR/erp_clinica_principal_$STAMP.sql"

echo "Backup creado:"
echo "$BACKUP_DIR/erp_clinica_principal_$STAMP.sql.gz"