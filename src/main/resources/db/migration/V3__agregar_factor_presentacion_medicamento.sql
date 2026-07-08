ALTER TABLE inv_medicamento_comercial
ADD COLUMN IF NOT EXISTS unidad_presentacion VARCHAR(80) NOT NULL DEFAULT 'Unidad',
ADD COLUMN IF NOT EXISTS factor_conversion_unidad_base NUMERIC(14,2) NOT NULL DEFAULT 1.00;

ALTER TABLE inv_medicamento_comercial
ADD CONSTRAINT chk_inv_medicamento_factor_conversion_positivo
CHECK (factor_conversion_unidad_base > 0);

COMMENT ON COLUMN inv_medicamento_comercial.unidad_presentacion IS
'Unidad comercial usada para ingresar stock: Caja, Blister, Frasco, Sobre, Unidad, etc.';

COMMENT ON COLUMN inv_medicamento_comercial.factor_conversion_unidad_base IS
'Cantidad de unidades base contenidas en una unidad de presentacion. Ejemplo: 1 caja = 100 capsulas.';