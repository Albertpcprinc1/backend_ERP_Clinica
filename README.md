# ERP Clínica Principal - Backend

Backend principal del sistema ERP_CLINICA_PRINCIPAL, desarrollado inicialmente para el Centro Médico San Fernando Huaraz.

Este backend forma parte de una arquitectura ERP clínica modular, orientada a gestión médica, logística farmacéutica, admisión, laboratorio, farmacia, caja y futuras integraciones.

## Estado actual

Primera versión estable del módulo de Gestión Logística de Medicinas / Inventario Farmacéutico.

## Tecnologías

- Java 21
- Spring Boot 3.5.14
- Maven
- PostgreSQL
- Flyway
- Spring Data JPA / Hibernate
- Bean Validation
- API REST JSON

## Reglas principales implementadas

- El stock pertenece al lote, no al medicamento.
- Un DCI o genérico puede tener varios medicamentos comerciales.
- Cada medicamento comercial tiene presentación, registro sanitario, precios y configuración de receta.
- Cada lote tiene fecha de vencimiento, proveedor, documento de ingreso y ubicación física.
- El inventario se controla por lote.
- Las salidas aplican FEFO: First Expired, First Out.
- El Kardex se genera automáticamente por cada movimiento de stock.
- El Kardex está protegido contra UPDATE y DELETE mediante trigger PostgreSQL.
- El backend responde errores JSON estandarizados para Angular y Flutter.

## Base de datos

Nombre local sugerido:

erp_clinica_principal

Tablas principales:

- inv_dci
- inv_categoria_medicamento
- inv_unidad_medida
- inv_laboratorio_fabricante
- inv_proveedor
- inv_medicamento_comercial
- inv_lote_medicamento
- inv_inventario_lote
- inv_kardex_movimiento

## Configuración

Archivo principal:

src/main/resources/application.properties

Variables soportadas:

- SERVER_PORT
- DB_URL
- DB_USERNAME
- DB_PASSWORD

Ejemplo local PowerShell:

$env:SERVER_PORT="8085"
$env:DB_URL="jdbc:postgresql://localhost:5432/erp_clinica_principal"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"

También existe archivo de referencia:

.env.example

## Ejecución local

Desde la carpeta del backend:

mvn clean package
mvn spring-boot:run

URL base local:

http://localhost:8085

## Endpoint de salud

GET /api/public/health

## Endpoints principales

Catálogos:

GET /api/inventory/catalogs

DCI / Genéricos:

POST   /api/inventory/dci
GET    /api/inventory/dci
GET    /api/inventory/dci/{id}
PUT    /api/inventory/dci/{id}
DELETE /api/inventory/dci/{id}

Laboratorios:

POST   /api/inventory/laboratories
GET    /api/inventory/laboratories
GET    /api/inventory/laboratories/{id}
PUT    /api/inventory/laboratories/{id}
DELETE /api/inventory/laboratories/{id}

Proveedores:

POST   /api/inventory/providers
GET    /api/inventory/providers
GET    /api/inventory/providers/{id}
PUT    /api/inventory/providers/{id}
DELETE /api/inventory/providers/{id}

Medicamentos comerciales:

POST   /api/inventory/medicines
GET    /api/inventory/medicines
GET    /api/inventory/medicines/{id}
PUT    /api/inventory/medicines/{id}
DELETE /api/inventory/medicines/{id}

Ingreso de stock:

POST /api/inventory/stock-entries

Salida de stock FEFO:

POST /api/inventory/stock-outputs

Consulta de inventario:

GET /api/inventory/stocks

Consulta de Kardex:

GET /api/inventory/kardex
GET /api/inventory/kardex?medicamentoId=1
GET /api/inventory/kardex?loteId=1
GET /api/inventory/kardex?tipoMovimiento=ENTREGA_INTERNA
GET /api/inventory/kardex?fechaDesde=2026-07-07T00:00:00&fechaHasta=2026-07-07T23:59:59

Dashboard de inventario:

GET /api/inventory/dashboard-summary

## Pruebas rápidas

Existe un script de prueba GET que no modifica stock:

Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
.\scripts\test_backend_inventario_get.ps1

## Estructura principal

src/main/java/com/sanfernando/erpclinica

- controller
- exception
- modules/inventory/controller
- modules/inventory/dto
- modules/inventory/entity
- modules/inventory/repository
- modules/inventory/service

## Migraciones Flyway

- V1__crear_tabla_sistema_info.sql
- V2__crear_modelo_inventario_farmaceutico.sql

## Pendientes técnicos

- Seguridad JWT.
- Login backend.
- Roles y permisos.
- Auditoría con usuario real autenticado.
- Angular intranet.
- Módulo Almacén / Logística en Angular.
- Integración receta médica / farmacia.
- Redis para stock comprometido por receta.
- Módulos médicos por especialidad.