# HOJA DE RUTA PRINCIPAL

## ERP Clínica Principal — Centro Médico San Fernando Huaraz

> Documento maestro del proyecto. Funciona como memoria escrita, hoja de ruta vigente y base para la documentación técnica final.

**Versión documental:** 1.1
**Última actualización:** 2026-07-23  
**Responsable del proyecto:** Ing. Albert Huerta  
**Estado general:** Desarrollo incremental con infraestructura productiva activa en AWS.

---

# 1. Propósito del documento

Este archivo centraliza el core funcional y técnico, las reglas de negocio aprobadas, las decisiones arquitectónicas, los módulos completados, los avances desplegados en AWS, los pendientes, los cambios aceptados, los riesgos y la hoja de ruta vigente.

También será la memoria escrita del proyecto y la base documental para el informe técnico final.

---

# 2. Regla oficial de cierre de bloques

Un bloque o conjunto funcional solo se considera terminado cuando cumple:

1. Desarrollo completado.
2. Pruebas locales satisfactorias.
3. Respaldo previo cuando corresponda.
4. Build satisfactorio.
5. Commit descriptivo.
6. Push a GitHub.
7. Despliegue en AWS.
8. Pruebas satisfactorias en producción.
9. Confirmación funcional del usuario.
10. Actualización de `HOJA DE RUTA PRINCIPAL.md` y `ARQUITECTURA.md`.

Un bloque no se declarará cerrado si solo funciona localmente.

---

# 3. Core del proyecto

El proyecto implementa un ERP clínico integral para el Centro Médico San Fernando de Huaraz.

La solución debe centralizar:

- Admisión.
- Pacientes.
- Citas.
- Historia Clínica Electrónica.
- Atención médica.
- Especialidades.
- Recetas.
- Farmacia.
- Inventario.
- Kardex.
- Laboratorio.
- Caja y pagos.
- Reportes.
- Auditoría.
- Portal del paciente.
- Aplicación móvil futura.

La arquitectura debe permitir que el sistema sea reutilizable y adaptable posteriormente para otras clínicas, sin perder la configuración particular de San Fernando.

---

# 4. Flujo funcional prioritario vigente

La segunda entrega funcional tiene como núcleo:

```text
Admisión
   ↓
Paciente único + HCE
   ↓
Cita y recepción
   ↓
Médico
   ↓
Consulta clínica
   ↓
Receta por DCI
   ↓
Reserva temporal de stock
   ↓
Farmacia
   ↓
Despacho FEFO
   ↓
Kardex ENTREGA_POR_RECETA
```

Los pagos, caja y facturación se mantienen para una etapa posterior.

---

# 5. Roles principales aprobados

## Administrador del sistema

Gestiona usuarios, roles, parámetros institucionales, sedes, especialidades, servicios y auditoría.

## Asistente logístico

Registra ingresos, gestiona lotes, proveedores y fabricantes, consulta stock, ejecuta salidas autorizadas y consulta Kardex. No puede modificar ni eliminar movimientos históricos.

## Admisión o recepción

Busca o crea pacientes, registra citas, confirma llegada, deriva al especialista y gestiona la cola de atención.

## Médico o especialista

Consulta pacientes en espera, abre el acto médico, revisa la HCE, registra atención y diagnóstico, prescribe por DCI y cierra la atención.

## Asistente de farmacia

Consulta recetas pendientes, valida paciente y receta, confirma reservas, despacha medicamentos aplicando FEFO y genera Kardex `ENTREGA_POR_RECETA`. Farmacia no registra ingresos de stock.

## Paciente

En una etapa futura podrá consultar citas, recetas, resultados y comprobantes, además de actualizar los datos permitidos.

---

# 6. Reglas de negocio vigentes

## Paciente único e HCE

- Un paciente debe tener una identidad única.
- El DNI es el identificador principal cuando corresponda.
- No se debe duplicar un paciente por especialidad.
- Cada paciente debe tener una sola HCE longitudinal.
- Las atenciones cerradas no deben eliminarse.
- Las correcciones clínicas deben quedar auditadas.

## Receta

- El médico prescribe principalmente por DCI.
- La receta registra dosis, frecuencia, duración, vía, cantidad e indicaciones.
- Debe vincularse con paciente, médico y atención.

## Reserva de stock

- La duración inicial aprobada es de 60 minutos.
- La reserva no equivale a una salida definitiva.
- Una reserva expirada libera el stock.
- El despacho confirmado genera la salida definitiva.

## Farmacia

- Farmacia no ingresa stock.
- Farmacia consume stock existente.
- El despacho aplica FEFO.
- Debe evitarse el doble despacho.

## Inventario y Kardex

- El stock existe por lote y se controla en unidad base.
- Las presentaciones comerciales se convierten mediante factor de conversión.
- Las salidas aplican FEFO.
- PostgreSQL es la fuente definitiva del stock.
- Kardex es inmutable: no se permite `UPDATE` ni `DELETE`.

## Pagos

- Se desarrollarán después del flujo clínico principal.
- El pago se consolidará al final del flujo de atención.

---

# 7. Tecnologías oficiales

## Backend

- Java 21.
- Spring Boot 3.5.
- Maven.
- Spring Data JPA.
- Flyway.
- PostgreSQL.
- Spring Security y JWT en el siguiente ciclo.

## Frontend

- Angular 18 standalone.
- TypeScript.
- SCSS.
- Intranet unificada con menú y componentes por rol.

## Aplicación móvil futura

- Flutter.
- Consumo de la misma API.
- Orientada principalmente al paciente.

## Infraestructura

- PostgreSQL 16.
- Redis previsto para reservas temporales.
- Docker y Docker Compose.
- Nginx.
- Certbot.
- Fail2ban.
- UFW.

---

# 8. Repositorios y rutas

## Backend

```text
git@github.com-maestria:Albertpcprinc1/backend_ERP_Clinica.git
```

Ruta local:

```text
E:\PROYECTOS\EMP_WANDAL\ERP_CLINICA_PRINCIPAL\SOFT\backend_ERP_Clinica
```

## Frontend Angular

```text
git@github.com-maestria:Albertpcprinc1/frontend_ERP_Clinica_principal.git
```

Ruta local:

```text
E:\PROYECTOS\EMP_WANDAL\ERP_CLINICA_PRINCIPAL\SOFT\frontend_ERP_Clinica_principal
```

## Backups locales

```text
E:\PROYECTOS\EMP_WANDAL\ERP_CLINICA_PRINCIPAL\SOFT\backups
```

---

# 9. Estado productivo de AWS

- Proveedor: AWS.
- Servicio: EC2 administrada como VPS.
- Región: `us-east-1`.
- Sistema operativo: Ubuntu Server 24.04 LTS.
- Elastic IP: `3.213.172.216`.
- Usuario operativo: `deploy`.
- Hostname: `vps-sanfernando-erp-prod`.

Servicios activos:

- Docker Engine.
- Docker Compose.
- PostgreSQL.
- Backend Spring Boot.
- Nginx.
- Certbot.
- Fail2ban.
- UFW.

Rutas productivas:

```text
/opt/sanfernando/backend/repo
/opt/sanfernando/backend/repo/deploy/ec2-vps
/opt/sanfernando/backups/postgres
/var/www/sanfernando/landing
/var/www/sanfernando/intranet
/var/www/sanfernando/paciente
```

Dominios:

```text
https://sanfernandocentromedico.com
https://www.sanfernandocentromedico.com
https://api.sanfernandocentromedico.com
https://intranet.sanfernandocentromedico.com
https://paciente.sanfernandocentromedico.com
```

---

# 10. Seguridad operativa actual

- HTTPS activo.
- Certificado SSL emitido para cinco dominios.
- Renovación automática validada.
- PostgreSQL no expuesto públicamente.
- Backend publicado solo en `127.0.0.1:8085`.
- Nginx actúa como reverse proxy.
- Security Group limita SSH al rango autorizado.
- UFW y Fail2ban activos.
- `.env` excluido del repositorio.
- Deploy Key de la EC2 configurada para GitHub.

Pendiente:

- Login real.
- JWT.
- RBAC.
- Auditoría de accesos.
- Gestión de sesiones.
- Recuperación de contraseña.
- Rate limiting.

---

# 11. Módulo de inventario completado

Funcionalidades desplegadas:

- Catálogo DCI.
- Categorías.
- Unidades de medida.
- Laboratorios y proveedores en backend.
- Medicamentos comerciales.
- Presentaciones y factor de conversión.
- Ingreso por cajas o presentaciones.
- Conversión automática a unidad base.
- Lotes y stock por lote.
- Stock consolidado.
- FEFO.
- Alertas.
- Salidas internas.
- Tipo `ENTREGA_POR_RECETA`.
- Kardex inmutable.
- Dashboard de inventario.
- Angular conectado a la API productiva.

Datos productivos de validación:

- 3 DCI.
- 4 medicamentos activos.
- 4 lotes activos.
- Stock total: 1,805 unidades.
- 7 movimientos Kardex.

---

# 12. Migraciones existentes

No se deben modificar:

```text
V1__crear_tabla_sistema_info.sql
V2__crear_modelo_inventario_farmaceutico.sql
V3__agregar_factor_presentacion_medicamento.sql
V4__agregar_tipo_entrega_por_receta_kardex.sql
```

Todo nuevo modelo debe agregarse desde `V5` en adelante.

---

# 13. Backups

Estado actual:

- Backup base creado.
- Backup post-carga creado.
- Script profesional implementado.
- Compresión e integridad validadas.
- Retención local inicial de 14 días.

Script:

```text
deploy/ec2-vps/scripts/02_backup_postgres.sh
```

Último commit relevante:

```text
31c6e62 fix: robustecer script de backup postgres en EC2
```

Pendientes:

- Programar cron.
- Copia externa fuera de la EC2.
- Prueba formal de restauración.
- Cifrado externo.

---

# 14. Hitos completados

## Hito 1 — Base del proyecto

Backend, PostgreSQL, Flyway, GitHub y frontend Angular institucional.

## Hito 2 — Inventario y logística

DCI, medicamentos, lotes, stock, FEFO, Kardex, ingresos, salidas y dashboard.

## Hito 3 — Producción AWS

EC2, Elastic IP, Docker, PostgreSQL productivo, backend, Nginx, DNS, SSL y Angular productivo.

## Hito 4 — Continuidad operativa inicial

Backups, script robusto, retención, validación de integridad y acceso SSH controlado.

---

# 15. Segunda entrega funcional aprobada

## MVP 2 — Atención médica y receta integrada

Flujo demostrable:

```text
Admisión
→ Registro o búsqueda de paciente
→ Historia clínica única
→ Cita y recepción
→ Cola médica
→ Consulta general
→ Diagnóstico
→ Receta por DCI
→ Reserva temporal
→ Farmacia
→ Despacho FEFO
→ Kardex ENTREGA_POR_RECETA
```

Cuentas demostrativas previstas:

```text
admin.demo
admision.demo
medico.demo
farmacia.demo
```

Criterio final de aceptación:

1. Admisión registra o encuentra al paciente.
2. Se crea o consulta una HCE única.
3. Se registra una cita.
4. El paciente pasa a espera.
5. El médico abre la atención.
6. Registra consulta y diagnóstico.
7. Genera una receta por DCI.
8. El sistema reserva stock.
9. Farmacia visualiza la receta.
10. Farmacia confirma el despacho.
11. Se aplica FEFO.
12. Se actualiza el stock.
13. Se genera Kardex `ENTREGA_POR_RECETA`.
14. La operación queda trazable.

---

# 16. Hoja de ruta actualizada — Bloques 50 al 66

## BLOQUE 50 — Seguridad mínima productiva

Usuarios, BCrypt, login real, JWT, sesión Angular, usuario administrador inicial, despliegue AWS y actualización documental.

## BLOQUE 51 — Roles y permisos

Administrador, admisión, médico, farmacia y logística; RBAC backend, guards Angular, menú por rol, endpoints protegidos y usuarios demo.

## BLOQUE 52 — Catálogos clínicos mínimos

Especialidades, médicos, servicios, consultorios, horarios y medicina general inicial.

## BLOQUE 53 — Paciente único y HCE

Paciente por DNI, prevención de duplicados, datos personales, contacto de emergencia, número único de historia clínica e historial básico.

## BLOQUE 54 — Admisión y citas

Búsqueda, registro de cita, especialidad, médico, fecha, hora, motivo, confirmación, recepción y estados.

Estados mínimos:

```text
PROGRAMADA
CONFIRMADA
EN_ESPERA
EN_ATENCION
ATENDIDA
CANCELADA
NO_ASISTIO
```

## BLOQUE 55 — Cola médica y triaje básico

Cola de pacientes, orden de llegada, signos vitales, peso, talla, temperatura, presión, saturación e IMC.

## BLOQUE 56 — Consulta médica general

Apertura del acto médico, motivo, anamnesis, examen, diagnóstico, indicaciones, próximo control y cierre.

## BLOQUE 57 — Receta electrónica

Prescripción por DCI, concentración, dosis, frecuencia, duración, vía, cantidad, indicaciones, identificador y vinculación con atención.

## BLOQUE 58 — Redis y reserva de stock

Redis en Docker Compose, reserva de 60 minutos, stock físico, comprometido y disponible, expiración, cancelación y prevención de doble reserva.

## BLOQUE 59 — Farmacia y despacho

Recetas pendientes, validación, reserva, despacho parcial o completo, selección FEFO y confirmación.

## BLOQUE 60 — Integración con Kardex

Movimiento `ENTREGA_POR_RECETA`, referencias, paciente, médico, farmacia, lote, stock anterior, stock posterior y liberación de reserva.

## BLOQUE 61 — Dashboard operativo y AWS

Pacientes, citas, espera, atención, finalizadas, recetas pendientes, recetas entregadas y stock comprometido; despliegue y validación productiva.

## BLOQUE 62 — Preparación de demostración

Datos controlados, usuarios demo, paciente, médico, especialidad, guion, prueba integral, evidencias, backup y despliegue final.

La segunda entrega funcional finaliza en este bloque.

## BLOQUE 63 — Caja y pagos

Cuenta del paciente, servicios, medicamentos entregados, totales, formas de pago y cierre de caja.

## BLOQUE 64 — Laboratorio

Catálogo, orden, muestra, resultado, validación e integración con HCE.

## BLOQUE 65 — Portal web del paciente

Login, perfil, citas, recetas, resultados, historial permitido y notificaciones.

## BLOQUE 66 — Flutter y ampliaciones

Aplicación móvil, reportes, notificaciones push y funciones móviles.

---

# 17. Funcionalidades postergadas deliberadamente

- Caja completa.
- Pago electrónico.
- Facturación SUNAT.
- Portal completo del paciente.
- Flutter.
- Laboratorio completo.
- Firma digital avanzada.
- Todas las especialidades.
- Reportería avanzada.
- Convenios y aseguradoras completos.

Esta decisión protege el alcance del MVP 2.

---

# 18. Riesgos y controles

## Alterar inventario desplegado

- Nuevas migraciones desde V5.
- No modificar V1 a V4.
- Backup antes de despliegues.
- Pruebas locales y despliegue incremental.

## Exponer datos clínicos sin seguridad

- Implementar autenticación antes de pacientes y HCE.
- Proteger endpoints y aplicar RBAC.

## Inconsistencia entre reserva y stock

- PostgreSQL como fuente definitiva.
- Redis solo para reserva temporal.
- Operaciones transaccionales e idempotentes.

## Pérdida de datos

- Backups, copia externa pendiente, restauración probada y volumen persistente.

## Alcance excesivo

- Mantener el MVP 2 y postergar pagos y especialidades avanzadas.

---

# 19. Decisiones vigentes

1. AWS EC2 se administra inicialmente como VPS.
2. PostgreSQL vive en Docker dentro de la EC2.
3. Angular Intranet es la aplicación central del personal.
4. Flutter será una extensión futura.
5. El paciente tendrá una HCE única.
6. El médico prescribe por DCI.
7. La reserva inicial durará 60 minutos.
8. Farmacia no ingresa stock.
9. Farmacia aplica FEFO.
10. Kardex es inmutable.
11. Los pagos se desarrollarán después del flujo clínico.
12. La segunda entrega será: `Admisión → Paciente → Médico → Receta → Farmacia → Kardex`.

---

# 20. Próximo bloque oficial

```text
BLOQUE 50 — Seguridad mínima productiva
```

Antes de modificar código:

1. Revisar el estado local.
2. Crear respaldo.
3. Diseñar entidades y migración V5.
4. Definir endpoints y política JWT.
5. Probar localmente.
6. Versionar.
7. Desplegar en AWS.
8. Actualizar ambos documentos maestros.

---

# 21. Historial del documento

## Versión 1.0

- Creación inicial del documento maestro.
- Consolidación del despliegue AWS.
- Consolidación del inventario y Kardex.
- Inclusión de la segunda entrega funcional.
- Reorganización de los bloques 50 al 66.
- Pagos postergados.
- Incorporación de la regla documental obligatoria.

---

# 22. Cierre del hito documental previo al Bloque 50 funcional

## Bloque 49.5 - Documentación viva y continuidad operativa

**Estado:** completado, versionado, desplegado y validado en AWS.

## Resultados alcanzados

- Se creó `docs/HOJA DE RUTA PRINCIPAL.md`.
- Se creó `docs/ARQUITECTURA.md`.
- Ambos documentos fueron versionados en GitHub.
- Ambos documentos fueron descargados y validados en la EC2 productiva.
- Se confirmó la codificación UTF-8 y el formato Linux.
- Se robusteció el script de backup PostgreSQL.
- El script puede ejecutarse desde cualquier directorio.
- El script carga automáticamente el archivo `.env`.
- El script valida los contenedores.
- El script genera, comprime y valida el backup.
- La retención local inicial quedó configurada en 14 días.
- El permiso ejecutable quedó registrado en Git.
- El permiso productivo quedó normalizado a `755`.

## Commits relevantes

```text
31c6e62 fix: robustecer script de backup postgres en EC2
5406025 docs: crear hoja de ruta principal y arquitectura viva
019f475 chore: marcar script de backup postgres como ejecutable
```

## Evidencia productiva

```text
SINTAXIS_SCRIPT_OK
SCRIPT_EJECUTABLE_OK
BACKUP_OK
PostgreSQL: healthy
Backend: Up
Repositorio AWS: limpio
```

## Último backup validado

```text
/opt/sanfernando/backups/postgres/erp_clinica_principal_20260723_035521.sql.gz
```

## Próximo bloque oficial

```text
BLOQUE 50 - Seguridad mínima productiva
```

Alcance inicial:

- Modelo de usuarios.
- Roles mínimos.
- Contraseñas con BCrypt.
- Login real.
- JWT.
- Protección de endpoints.
- Sesión Angular.
- Despliegue y validación en AWS.