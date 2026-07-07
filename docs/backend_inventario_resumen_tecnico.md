# ERP_CLINICA_PRINCIPAL - Backend Inventario Farmaceutico Base

Cliente inicial:
Centro Medico San Fernando Huaraz

Modulo inicial:
Gestion Logistica de Medicinas / Inventario Farmaceutico

Backend:
Spring Boot 3.5.14
Java 21
PostgreSQL
Flyway
JPA / Hibernate

Puerto local:
8085

Base de datos:
erp_clinica_principal

Reglas funcionales implementadas:
1. El stock pertenece al lote, no al medicamento.
2. Medicamento comercial pertenece a un DCI / generico.
3. Registro Sanitario es unico por medicamento comercial.
4. Lote pertenece a medicamento comercial.
5. Inventario pertenece al lote.
6. Kardex registra todos los movimientos de stock.
7. Kardex es inalterable por trigger PostgreSQL.
8. Salidas de stock aplican FEFO: primero vence, primero sale.
9. El sistema bloquea salida si no hay stock suficiente.
10. Se generan errores JSON estandarizados para frontend Angular/Flutter.

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

Migraciones Flyway validadas:
- V1__crear_tabla_sistema_info.sql
- V2__crear_modelo_inventario_farmaceutico.sql

Endpoints principales validados:
- GET  /api/public/health
- GET  /api/inventory/catalogs
- CRUD /api/inventory/dci
- CRUD /api/inventory/laboratories
- CRUD /api/inventory/providers
- CRUD /api/inventory/medicines
- POST /api/inventory/stock-entries
- POST /api/inventory/stock-outputs
- GET  /api/inventory/stocks
- GET  /api/inventory/kardex
- GET  /api/inventory/dashboard-summary

Estado actual de prueba:
- Medicamento: Ibuprofeno AC Farma
- DCI: Ibuprofeno
- Lotes activos: 2
- Stock disponible total: 330
- Kardex: 5 movimientos
- Dashboard inventario: validado