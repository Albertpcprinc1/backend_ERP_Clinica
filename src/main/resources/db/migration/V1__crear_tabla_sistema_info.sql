CREATE TABLE IF NOT EXISTS sistema_info (
    id BIGSERIAL PRIMARY KEY,
    nombre_proyecto VARCHAR(120) NOT NULL,
    cliente VARCHAR(160) NOT NULL,
    version_sistema VARCHAR(30) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO sistema_info (nombre_proyecto, cliente, version_sistema)
VALUES ('ERP_CLINICA_PRINCIPAL', 'Centro Medico San Fernando Huaraz', '0.0.1');