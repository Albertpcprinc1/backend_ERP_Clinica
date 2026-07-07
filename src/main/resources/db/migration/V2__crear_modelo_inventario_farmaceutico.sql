CREATE TABLE inv_dci (
    id BIGSERIAL PRIMARY KEY,
    nombre_generico VARCHAR(180) NOT NULL,
    descripcion VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,
    CONSTRAINT uk_inv_dci_nombre UNIQUE (nombre_generico)
);

CREATE TABLE inv_categoria_medicamento (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(500),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,
    CONSTRAINT uk_inv_categoria_nombre UNIQUE (nombre)
);

CREATE TABLE inv_unidad_medida (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(30) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,
    CONSTRAINT uk_inv_unidad_codigo UNIQUE (codigo)
);

CREATE TABLE inv_laboratorio_fabricante (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(180) NOT NULL,
    ruc VARCHAR(20),
    pais_origen VARCHAR(100),
    telefono VARCHAR(30),
    email VARCHAR(160),
    direccion VARCHAR(300),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,
    CONSTRAINT uk_inv_laboratorio_nombre UNIQUE (nombre)
);

CREATE TABLE inv_proveedor (
    id BIGSERIAL PRIMARY KEY,
    razon_social VARCHAR(180) NOT NULL,
    ruc VARCHAR(20),
    telefono VARCHAR(30),
    email VARCHAR(160),
    direccion VARCHAR(300),
    contacto VARCHAR(160),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,
    CONSTRAINT uk_inv_proveedor_ruc UNIQUE (ruc)
);

CREATE TABLE inv_medicamento_comercial (
    id BIGSERIAL PRIMARY KEY,
    dci_id BIGINT NOT NULL,
    categoria_id BIGINT,
    laboratorio_id BIGINT,
    unidad_medida_id BIGINT,
    nombre_comercial VARCHAR(180) NOT NULL,
    concentracion VARCHAR(120) NOT NULL,
    forma_farmaceutica VARCHAR(120) NOT NULL,
    presentacion_comercial VARCHAR(220) NOT NULL,
    registro_sanitario VARCHAR(80) NOT NULL,
    es_generico BOOLEAN NOT NULL DEFAULT FALSE,
    requiere_receta BOOLEAN NOT NULL DEFAULT FALSE,
    requiere_receta_archivada BOOLEAN NOT NULL DEFAULT FALSE,
    precio_publico NUMERIC(12,2) NOT NULL DEFAULT 0,
    precio_aseguradora NUMERIC(12,2) NOT NULL DEFAULT 0,
    stock_minimo_total NUMERIC(14,2) NOT NULL DEFAULT 0,
    observaciones VARCHAR(600),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,

    CONSTRAINT fk_inv_med_dci FOREIGN KEY (dci_id) REFERENCES inv_dci(id),
    CONSTRAINT fk_inv_med_categoria FOREIGN KEY (categoria_id) REFERENCES inv_categoria_medicamento(id),
    CONSTRAINT fk_inv_med_laboratorio FOREIGN KEY (laboratorio_id) REFERENCES inv_laboratorio_fabricante(id),
    CONSTRAINT fk_inv_med_unidad FOREIGN KEY (unidad_medida_id) REFERENCES inv_unidad_medida(id),

    CONSTRAINT uk_inv_med_registro_sanitario UNIQUE (registro_sanitario),
    CONSTRAINT ck_inv_med_precio_publico CHECK (precio_publico >= 0),
    CONSTRAINT ck_inv_med_precio_aseguradora CHECK (precio_aseguradora >= 0),
    CONSTRAINT ck_inv_med_stock_minimo CHECK (stock_minimo_total >= 0)
);

CREATE TABLE inv_lote_medicamento (
    id BIGSERIAL PRIMARY KEY,
    medicamento_id BIGINT NOT NULL,
    proveedor_id BIGINT,
    numero_lote VARCHAR(100) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    fecha_ingreso DATE NOT NULL DEFAULT CURRENT_DATE,
    documento_ingreso VARCHAR(120),
    guia_remision VARCHAR(120),
    ubicacion_fisica VARCHAR(160),
    costo_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,

    CONSTRAINT fk_inv_lote_medicamento FOREIGN KEY (medicamento_id) REFERENCES inv_medicamento_comercial(id),
    CONSTRAINT fk_inv_lote_proveedor FOREIGN KEY (proveedor_id) REFERENCES inv_proveedor(id),
    CONSTRAINT uk_inv_lote_medicamento_numero UNIQUE (medicamento_id, numero_lote),
    CONSTRAINT ck_inv_lote_costo CHECK (costo_unitario >= 0),
    CONSTRAINT ck_inv_lote_fecha_vencimiento CHECK (fecha_vencimiento > fecha_ingreso)
);

CREATE TABLE inv_inventario_lote (
    id BIGSERIAL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    stock_actual NUMERIC(14,2) NOT NULL DEFAULT 0,
    stock_comprometido NUMERIC(14,2) NOT NULL DEFAULT 0,
    stock_minimo NUMERIC(14,2) NOT NULL DEFAULT 0,
    stock_disponible NUMERIC(14,2) GENERATED ALWAYS AS (stock_actual - stock_comprometido) STORED,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP,

    CONSTRAINT fk_inv_stock_lote FOREIGN KEY (lote_id) REFERENCES inv_lote_medicamento(id),
    CONSTRAINT uk_inv_stock_lote UNIQUE (lote_id),
    CONSTRAINT ck_inv_stock_actual CHECK (stock_actual >= 0),
    CONSTRAINT ck_inv_stock_comprometido CHECK (stock_comprometido >= 0),
    CONSTRAINT ck_inv_stock_minimo CHECK (stock_minimo >= 0),
    CONSTRAINT ck_inv_stock_comprometido_actual CHECK (stock_comprometido <= stock_actual)
);

CREATE TABLE inv_kardex_movimiento (
    id BIGSERIAL PRIMARY KEY,
    medicamento_id BIGINT NOT NULL,
    lote_id BIGINT NOT NULL,
    tipo_movimiento VARCHAR(60) NOT NULL,
    cantidad NUMERIC(14,2) NOT NULL,
    stock_anterior NUMERIC(14,2) NOT NULL,
    stock_posterior NUMERIC(14,2) NOT NULL,
    referencia_tipo VARCHAR(80),
    referencia_id BIGINT,
    motivo VARCHAR(600),
    usuario_responsable VARCHAR(160),
    fecha_movimiento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_inv_kardex_medicamento FOREIGN KEY (medicamento_id) REFERENCES inv_medicamento_comercial(id),
    CONSTRAINT fk_inv_kardex_lote FOREIGN KEY (lote_id) REFERENCES inv_lote_medicamento(id),
    CONSTRAINT ck_inv_kardex_cantidad CHECK (cantidad > 0),
    CONSTRAINT ck_inv_kardex_stock_anterior CHECK (stock_anterior >= 0),
    CONSTRAINT ck_inv_kardex_stock_posterior CHECK (stock_posterior >= 0),
    CONSTRAINT ck_inv_kardex_tipo CHECK (
        tipo_movimiento IN (
            'INGRESO_PROVEEDOR',
            'VENTA_FARMACIA',
            'ENTREGA_INTERNA',
            'DEVOLUCION',
            'MERMA',
            'VENCIMIENTO',
            'TRANSFERENCIA',
            'AJUSTE_AUTORIZADO',
            'USO_INSUMO_MEDICO'
        )
    )
);

CREATE INDEX idx_inv_medicamento_dci ON inv_medicamento_comercial(dci_id);
CREATE INDEX idx_inv_medicamento_registro ON inv_medicamento_comercial(registro_sanitario);
CREATE INDEX idx_inv_lote_medicamento ON inv_lote_medicamento(medicamento_id);
CREATE INDEX idx_inv_lote_vencimiento ON inv_lote_medicamento(fecha_vencimiento);
CREATE INDEX idx_inv_inventario_lote ON inv_inventario_lote(lote_id);
CREATE INDEX idx_inv_kardex_lote ON inv_kardex_movimiento(lote_id);
CREATE INDEX idx_inv_kardex_medicamento ON inv_kardex_movimiento(medicamento_id);
CREATE INDEX idx_inv_kardex_fecha ON inv_kardex_movimiento(fecha_movimiento);

CREATE OR REPLACE FUNCTION fn_inv_bloquear_modificacion_kardex()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'El Kardex es inalterable. No se permite modificar ni eliminar movimientos.';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inv_kardex_no_update
BEFORE UPDATE ON inv_kardex_movimiento
FOR EACH ROW
EXECUTE FUNCTION fn_inv_bloquear_modificacion_kardex();

CREATE TRIGGER trg_inv_kardex_no_delete
BEFORE DELETE ON inv_kardex_movimiento
FOR EACH ROW
EXECUTE FUNCTION fn_inv_bloquear_modificacion_kardex();

INSERT INTO inv_categoria_medicamento (nombre, descripcion)
VALUES
('Medicamento', 'Producto farmaceutico de uso general'),
('Vacuna', 'Producto biologico con control de lote y vencimiento'),
('Insumo medico', 'Material o insumo usado en procedimientos medicos')
ON CONFLICT DO NOTHING;

INSERT INTO inv_unidad_medida (codigo, nombre)
VALUES
('UND', 'Unidad'),
('TAB', 'Tableta'),
('CAP', 'Capsula'),
('AMP', 'Ampolla'),
('FCO', 'Frasco'),
('ML', 'Mililitro'),
('MG', 'Miligramo'),
('G', 'Gramo')
ON CONFLICT DO NOTHING;