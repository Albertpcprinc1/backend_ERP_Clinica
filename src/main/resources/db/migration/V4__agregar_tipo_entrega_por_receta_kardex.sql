ALTER TABLE inv_kardex_movimiento
DROP CONSTRAINT IF EXISTS ck_inv_kardex_tipo;

ALTER TABLE inv_kardex_movimiento
ADD CONSTRAINT ck_inv_kardex_tipo CHECK (
    tipo_movimiento IN (
        'INGRESO_PROVEEDOR',
        'DEVOLUCION_PROVEEDOR',
        'ENTREGA_INTERNA',
        'ENTREGA_POR_RECETA',
        'VENTA_FARMACIA',
        'USO_INSUMO_MEDICO',
        'AJUSTE_POSITIVO',
        'AJUSTE_NEGATIVO',
        'MERMA',
        'VENCIMIENTO',
        'ANULACION_MOVIMIENTO'
    )
);

COMMENT ON CONSTRAINT ck_inv_kardex_tipo ON inv_kardex_movimiento IS
'Tipos permitidos de movimiento Kardex. Incluye ENTREGA_POR_RECETA para integración futura con recetas médicas.';
