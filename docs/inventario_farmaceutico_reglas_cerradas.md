# Inventario farmacéutico - Reglas cerradas

## Proyecto

ERP Clínica Principal - Centro Médico San Fernando Huaraz.

## Estado del módulo

El módulo de inventario farmacéutico queda validado como base funcional para la gestión logística de medicamentos, lotes, stock, movimientos Kardex y salidas FEFO.

## Reglas funcionales cerradas

### 1. Catálogo DCI

El sistema maneja un catálogo de DCI o principio activo.

Ejemplos:

- Paracetamol
- Ibuprofeno
- Amoxicilina

Un DCI puede existir aunque todavía no tenga medicamentos comerciales asociados ni stock disponible.

### 2. Medicamento comercial

Cada medicamento comercial pertenece a un DCI.

Ejemplo:

- DCI: Amoxicilina
- Medicamento comercial: Amoxicilina San Fernando
- Concentración: 500 mg
- Forma farmacéutica: Cápsula
- Presentación comercial: Caja x 100 cápsulas
- Unidad base: Cápsula
- Unidad de presentación: Caja
- Factor de conversión: 100

### 3. Control por unidad base

El stock se controla siempre en unidad base.

Ejemplos:

- Cápsula
- Tableta
- Ampolla
- Frasco
- Unidad

La presentación comercial sirve para facilitar el ingreso logístico, pero el inventario se almacena en unidades base.

Ejemplo:

5 cajas x 100 cápsulas = 500 cápsulas.

### 4. Ingreso de stock por presentación

El usuario logístico puede ingresar stock por cajas, blísteres, frascos u otra presentación comercial.

El sistema calcula automáticamente la cantidad en unidad base.

Ejemplo validado:

- Medicamento: Amoxicilina San Fernando
- Lote: LOTE-AMOX-2029-001
- Cantidad ingresada: 5 cajas
- Factor: 100 cápsulas por caja
- Stock calculado: 500 cápsulas
- Movimiento Kardex: INGRESO_PROVEEDOR

### 5. Inventario por lote

El stock se almacena por lote, no directamente sobre el medicamento.

Cada lote tiene:

- Número de lote
- Fecha de ingreso
- Fecha de vencimiento
- Proveedor
- Ubicación física
- Stock actual
- Stock comprometido
- Stock disponible
- Stock mínimo
- Estado de vencimiento

### 6. Regla FEFO

Las salidas de stock aplican FEFO: First Expired, First Out.

El sistema descuenta primero los lotes con fecha de vencimiento más próxima, siempre que tengan stock disponible.

### 7. Salida interna

El sistema permite salidas internas de almacén mediante el tipo:

ENTREGA_INTERNA

Ejemplo validado:

- Medicamento: Amoxicilina San Fernando
- Salida: 21 cápsulas
- Stock anterior: 500
- Stock posterior: 479
- Kardex: ENTREGA_INTERNA

### 8. Salida por receta

El sistema permite salidas asociadas a receta médica mediante el tipo:

ENTREGA_POR_RECETA

Ejemplo validado:

- Medicamento: Amoxicilina San Fernando
- Salida: 9 cápsulas
- Stock anterior: 479
- Stock posterior: 470
- Kardex: ENTREGA_POR_RECETA

Esta regla queda preparada para integrarse posteriormente con el flujo:

Consulta médica -> Receta -> Entrega farmacia -> Kardex.

### 9. Kardex inmutable

El Kardex es inalterable.

No se permite modificar ni eliminar movimientos Kardex ya registrados.

La trazabilidad se garantiza mediante:

- Movimiento
- Medicamento
- DCI
- Lote
- Cantidad
- Stock anterior
- Stock posterior
- Tipo de referencia
- ID de referencia
- Motivo
- Usuario responsable
- Fecha y hora del movimiento

### 10. Tipos de movimiento Kardex permitidos

Actualmente la base de datos permite:

- INGRESO_PROVEEDOR
- DEVOLUCION_PROVEEDOR
- ENTREGA_INTERNA
- ENTREGA_POR_RECETA
- VENTA_FARMACIA
- USO_INSUMO_MEDICO
- AJUSTE_POSITIVO
- AJUSTE_NEGATIVO
- MERMA
- VENCIMIENTO
- ANULACION_MOVIMIENTO

### 11. Roles futuros relacionados

El módulo de inventario será usado principalmente por:

- Asistente logístico
- Administrador del sistema
- Farmacia
- Médicos, solo para consulta o solicitud de medicamentos según reglas futuras

### 12. Integración futura con farmacia

El módulo de farmacia no ingresará stock.

Farmacia consumirá stock existente mediante:

- Venta directa
- Entrega por receta
- Reserva temporal
- Anulación o devolución controlada

### 13. Integración futura con recetas

Cuando el médico emita una receta, el sistema deberá:

1. Validar disponibilidad del medicamento.
2. Separar o comprometer stock temporalmente.
3. Permitir despacho por farmacia.
4. Generar Kardex con tipo ENTREGA_POR_RECETA.
5. Descontar stock aplicando FEFO.

### 14. Estado actual validado

Se validó localmente:

- Catálogo DCI.
- Medicamento comercial.
- Registro real desde Angular.
- Ingreso de stock por cajas.
- Cálculo a unidad base.
- Creación de lote.
- Kardex de ingreso.
- Salida FEFO.
- Kardex de salida interna.
- Kardex de salida por receta.
- Corrección de textos con Unicode seguro.
- Backend y frontend sincronizados con GitHub.

## Conclusión

El módulo de inventario farmacéutico queda consolidado como base logística inicial del ERP Clínica Principal.
