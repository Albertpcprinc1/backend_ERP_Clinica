# Despliegue EC2 como VPS - ERP Clinica San Fernando

## Objetivo

Desplegar el ERP Clinica Principal en una instancia AWS EC2 Ubuntu administrada como VPS.

## Dominios

- sanfernandocentromedico.com
- www.sanfernandocentromedico.com
- api.sanfernandocentromedico.com
- intranet.sanfernandocentromedico.com
- paciente.sanfernandocentromedico.com

## Estructura servidor

/opt/sanfernando/backend
/opt/sanfernando/backups/postgres
/var/www/sanfernando/landing
/var/www/sanfernando/intranet
/var/www/sanfernando/paciente

## Servicios

- Docker
- Docker Compose
- PostgreSQL en contenedor
- Backend Spring Boot en contenedor
- Nginx
- Certbot
- Fail2ban
- UFW

## Flujo general

1. Crear EC2 Ubuntu.
2. Asociar Elastic IP.
3. Crear usuario deploy.
4. Configurar SSH.
5. Ejecutar scripts/01_prepare_server.sh.
6. Configurar DNS en Namecheap.
7. Copiar docker-compose.yml y .env real a /opt/sanfernando/backend.
8. Levantar backend y PostgreSQL.
9. Copiar Angular intranet compilado a /var/www/sanfernando/intranet.
10. Copiar Nginx conf a /etc/nginx/sites-available/sanfernando.conf.
11. Activar Nginx site.
12. Emitir certificados SSL con Certbot.
13. Validar API e intranet.

## Comandos base en servidor

cd /opt/sanfernando/backend
docker compose up -d --build
docker compose ps
docker logs -f sanfernando-backend

## Health check esperado

https://api.sanfernandocentromedico.com/api/public/health

## Intranet esperada

https://intranet.sanfernandocentromedico.com

## Nota

No subir .env real al repositorio.
Usar deploy/ec2-vps/.env.example como plantilla.