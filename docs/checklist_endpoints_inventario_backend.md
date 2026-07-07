# Checklist de Endpoints - Backend Inventario

## Salud del backend

- [x] GET /api/public/health

## Catalogos

- [x] GET /api/inventory/catalogs

## DCI / Generico

- [x] POST /api/inventory/dci
- [x] GET /api/inventory/dci
- [x] GET /api/inventory/dci/{id}
- [x] PUT /api/inventory/dci/{id}
- [x] DELETE /api/inventory/dci/{id} eliminado logico

## Laboratorios / Fabricantes

- [x] POST /api/inventory/laboratories
- [x] GET /api/inventory/laboratories
- [x] GET /api/inventory/laboratories/{id}
- [x] PUT /api/inventory/laboratories/{id}
- [ ] DELETE /api/inventory/laboratories/{id} pendiente de prueba explicita

## Proveedores

- [x] POST /api/inventory/providers
- [x] GET /api/inventory/providers
- [x] GET /api/inventory/providers/{id}
- [x] PUT /api/inventory/providers/{id}
- [ ] DELETE /api/inventory/providers/{id} pendiente de prueba explicita

## Medicamentos comerciales

- [x] POST /api/inventory/medicines
- [x] GET /api/inventory/medicines
- [x] GET /api/inventory/medicines/{id}
- [x] PUT /api/inventory/medicines/{id}
- [ ] DELETE /api/inventory/medicines/{id} pendiente de prueba explicita

## Ingreso de stock

- [x] POST /api/inventory/stock-entries
- [x] Crea lote
- [x] Crea inventario por lote
- [x] Registra Kardex INGRESO_PROVEEDOR

## Salida de stock

- [x] POST /api/inventory/stock-outputs
- [x] Aplica FEFO con un lote
- [x] Aplica FEFO con multiples lotes
- [x] Bloquea stock insuficiente con 409
- [x] Registra Kardex por cada lote afectado

## Inventario

- [x] GET /api/inventory/stocks
- [x] Orden FEFO por fecha de vencimiento ASC
- [x] Stock bajo
- [x] Estado de vencimiento

## Kardex

- [x] GET /api/inventory/kardex
- [x] Filtro por medicamentoId
- [x] Filtro por loteId
- [x] Filtro por tipoMovimiento
- [x] Filtro por rango de fechas
- [x] Trigger PostgreSQL bloquea UPDATE
- [x] Trigger PostgreSQL bloquea DELETE

## Dashboard

- [x] GET /api/inventory/dashboard-summary
- [x] Total medicamentos activos
- [x] Total lotes activos
- [x] Lotes agotados
- [x] Lotes con stock bajo
- [x] Lotes por vencer
- [x] Lotes vencidos
- [x] Stock total disponible
- [x] Ultimos movimientos Kardex

## Pendientes para siguiente fase

- [ ] Seguridad JWT / roles
- [ ] Login backend
- [ ] Angular intranet
- [ ] Modulo Almacen / Logistica Angular
- [ ] Permisos por rol
- [ ] Auditoria usuario real
- [ ] Integracion receta / farmacia
- [ ] Redis para stock comprometido por receta