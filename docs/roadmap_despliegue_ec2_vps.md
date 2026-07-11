# Roadmap de despliegue EC2 como VPS

## Dominio principal

sanfernandocentromedico.com

## Estructura de subdominios aprobada

- sanfernandocentromedico.com
  - Landing pública futura.

- www.sanfernandocentromedico.com
  - Redirección hacia dominio principal.

- api.sanfernandocentromedico.com
  - Backend Spring Boot.

- intranet.sanfernandocentromedico.com
  - Intranet universal para personal interno.

- paciente.sanfernandocentromedico.com
  - Portal cliente/paciente.

## Decisión de infraestructura inicial

La primera etapa usará una instancia AWS EC2 Ubuntu administrada como VPS.

No se usará RDS en esta primera etapa.

PostgreSQL se ejecutará dentro de la misma EC2, preferiblemente mediante Docker Compose.

## Motivo de la decisión

Esta estrategia permite:

- Reducir costos iniciales.
- Validar el ERP en nube.
- Mantener una administración similar a VPS Contabo.
- Facilitar pruebas funcionales con dominio real.
- Migrar posteriormente a RDS o a una VPS real si conviene.

## Estructura recomendada en servidor

Angular será servido directamente por Nginx desde carpetas del sistema:

/var/www/sanfernando/intranet
/var/www/sanfernando/landing
/var/www/sanfernando/paciente

El backend Spring Boot y PostgreSQL se ejecutarán con Docker Compose.

## Componentes de servidor

La EC2 deberá tener:

- Ubuntu Server.
- Usuario deploy.
- UFW firewall.
- Fail2ban.
- Docker.
- Docker Compose.
- Nginx.
- Certbot.
- Certificados SSL Let's Encrypt.
- Backups.
- Logs de operación.

## Estructura lógica

Internet
-> Elastic IP AWS
-> EC2 Ubuntu
-> Nginx
-> Backend Spring Boot
-> PostgreSQL
-> Angular Intranet
-> Landing pública futura
-> Portal paciente futuro

## DNS en Namecheap

Registros tipo A hacia la Elastic IP:

- @
- www
- api
- intranet
- paciente

## Fases de despliegue

### Fase 1: Preparación del proyecto

- Variables de entorno.
- Perfil productivo backend.
- Dockerfile backend.
- Docker Compose.
- Build Angular intranet.
- Configuración CORS para dominio real.
- Scripts de backup.

### Fase 2: Infraestructura AWS

- Crear EC2 Ubuntu.
- Crear Elastic IP.
- Configurar Security Group.
- Configurar acceso SSH.
- Instalar Docker, Nginx, Certbot, Fail2ban y UFW.

### Fase 3: DNS

- Configurar registros A en Namecheap.
- Validar propagación DNS.
- Preparar Nginx por subdominio.

### Fase 4: Despliegue

- Levantar PostgreSQL.
- Levantar backend.
- Aplicar Flyway.
- Subir Angular intranet.
- Configurar SSL.
- Probar endpoints públicos.

### Fase 5: Validación

- Health backend.
- Login futuro.
- Inventario.
- DCI.
- Medicamentos.
- Ingreso de stock.
- Salida FEFO.
- Kardex.
- Salida por receta.

## Decisión futura

Después de analizar costos y estabilidad, se decidirá entre:

1. Mantener AWS EC2 como VPS.
2. Migrar base de datos a AWS RDS.
3. Crear arquitectura AWS más formal con VPC, RDS, backups administrados y monitoreo.
4. Migrar a VPS Contabo u otro proveedor similar.

## Conclusión

La primera nube del ERP Clínica Principal será una EC2 administrada como VPS, priorizando simplicidad, control y bajo costo inicial.
