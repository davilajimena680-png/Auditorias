-- ============================================================
-- Sistema de Auditoría de Igualdad Laboral y No Discriminación
-- Script de creación de base de datos y datos semilla
-- Motor: PostgreSQL 15+
-- ============================================================
CREATE DATABASE auditoria_igualdad;
-- Conéctate a la base recién creada antes de continuar:
-- \c auditoria_igualdad
-- ---------- Catálogo de categorías ----------
CREATE TABLE categorias (
id_categoria SERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL UNIQUE,
descripcion TEXT
);
-- ---------- Empresas auditadas ----------
CREATE TABLE empresas (
id_empresa SERIAL PRIMARY KEY,
razon_social VARCHAR(150) NOT NULL,
rfc VARCHAR(13) UNIQUE,
sector VARCHAR(80),
num_empleados INTEGER CHECK (num_empleados &gt;= 0),
contacto_email VARCHAR(120)
);
-- ---------- Auditores ----------
CREATE TABLE auditores (
id_auditor SERIAL PRIMARY KEY,
nombre_completo VARCHAR(150) NOT NULL,
cedula_profesional VARCHAR(20),
email VARCHAR(120) UNIQUE
);
-- ---------- Auditorías ----------
CREATE TABLE auditorias (
id_auditoria SERIAL PRIMARY KEY,
id_empresa INTEGER NOT NULL REFERENCES empresas(id_empresa),
id_auditor INTEGER NOT NULL REFERENCES auditores(id_auditor),
fecha_inicio DATE NOT NULL,
fecha_fin DATE,
estatus VARCHAR(20) NOT NULL DEFAULT &#39;En proceso&#39;
CHECK (estatus IN (&#39;En proceso&#39;,&#39;Concluida&#39;,&#39;Cancelada&#39;))
);
-- ---------- Criterios de evaluación ----------
CREATE TABLE criterios (
id_criterio SERIAL PRIMARY KEY,
id_categoria INTEGER NOT NULL REFERENCES categorias(id_categoria),
descripcion TEXT NOT NULL,
peso NUMERIC(4,2) NOT NULL DEFAULT 1.00 CHECK (peso &gt;= 0 AND peso &lt;= 1)
);

-- ---------- Evaluaciones (captura por auditoría y criterio) ----------
CREATE TABLE evaluaciones (
id_evaluacion SERIAL PRIMARY KEY,
id_auditoria INTEGER NOT NULL REFERENCES auditorias(id_auditoria) ON DELETE CASCADE,
id_criterio INTEGER NOT NULL REFERENCES criterios(id_criterio),
cumplimiento VARCHAR(20) NOT NULL
CHECK (cumplimiento IN (&#39;Cumple&#39;,&#39;Parcial&#39;,&#39;No cumple&#39;)),
observaciones TEXT,
evidencia_ref VARCHAR(255),
fecha_captura TIMESTAMP NOT NULL DEFAULT NOW(),
UNIQUE (id_auditoria, id_criterio)
);
-- ---------- Hallazgos derivados de una evaluación ----------
CREATE TABLE hallazgos (
id_hallazgo SERIAL PRIMARY KEY,
id_evaluacion INTEGER NOT NULL REFERENCES evaluaciones(id_evaluacion) ON DELETE CASCADE,
tipo VARCHAR(30) NOT NULL
CHECK (tipo IN (&#39;No conformidad&#39;,&#39;Oportunidad de mejora&#39;)),
nivel_riesgo VARCHAR(10) NOT NULL CHECK (nivel_riesgo IN (&#39;Alto&#39;,&#39;Medio&#39;,&#39;Bajo&#39;)),
descripcion TEXT NOT NULL
);
-- ---------- Planes de acción correctivos ----------
CREATE TABLE planes_accion (
id_plan SERIAL PRIMARY KEY,
id_hallazgo INTEGER NOT NULL REFERENCES hallazgos(id_hallazgo) ON DELETE CASCADE,
responsable VARCHAR(120) NOT NULL,
fecha_compromiso DATE,
estatus VARCHAR(20) NOT NULL DEFAULT &#39;Pendiente&#39;
CHECK (estatus IN (&#39;Pendiente&#39;,&#39;En proceso&#39;,&#39;Cerrado&#39;))
);
-- Índices de apoyo para las consultas del dashboard
CREATE INDEX idx_evaluaciones_auditoria ON evaluaciones(id_auditoria);
CREATE INDEX idx_hallazgos_evaluacion ON hallazgos(id_evaluacion);
CREATE INDEX idx_planes_hallazgo ON planes_accion(id_hallazgo);
-- ============================================================
-- DATOS SEMILLA
-- ============================================================
INSERT INTO categorias (nombre, descripcion) VALUES
(&#39;Igualdad de género&#39;, &#39;Criterios relacionados con equidad de género en contratación, salarios
y ascensos&#39;),
(&#39;No discriminación&#39;, &#39;Criterios sobre prevención de discriminación por origen, edad,
discapacidad, religión, etc.&#39;),
(&#39;Accesibilidad&#39;, &#39;Condiciones físicas y de proceso que garantizan accesibilidad para personas
con discapacidad&#39;),
(&#39;Conciliación vida-trabajo&#39;, &#39;Políticas de horarios flexibles, permisos y licencias&#39;),
(&#39;Clima laboral&#39;, &#39;Mecanismos de denuncia, prevención del hostigamiento y ambiente laboral&#39;);
INSERT INTO criterios (id_categoria, descripcion, peso) VALUES
(1, &#39;Existe una política formal de igualdad salarial entre hombres y mujeres para el mismo
puesto&#39;, 1.00),
(1, &#39;Los procesos de contratación y ascenso cuentan con criterios objetivos documentados&#39;,
0.80),
(1, &#39;Se cuenta con registro y seguimiento de la brecha salarial de género&#39;, 0.70),
(2, &#39;Existe un código de conducta que prohíbe explícitamente la discriminación&#39;, 1.00),
(2, &#39;El personal directivo ha recibido capacitación en no discriminación en el último año&#39;,
0.80),
(2, &#39;Existen mecanismos de denuncia anónima por discriminación&#39;, 0.90),
(3, &#39;Las instalaciones cuentan con accesos adaptados para personas con discapacidad&#39;, 0.80),
(3, &#39;Los procesos de reclutamiento incluyen formatos accesibles&#39;, 0.60),
(4, &#39;Existen políticas de horario flexible o trabajo remoto documentadas&#39;, 0.70),
(4, &#39;Se otorgan permisos de paternidad/maternidad por encima del mínimo legal&#39;, 0.60),
(5, &#39;Existe un protocolo de atención a quejas por hostigamiento y acoso laboral o sexual&#39;,
1.00),
(5, &#39;Se realiza al menos una medición de clima laboral al año&#39;, 0.70);
INSERT INTO empresas (razon_social, rfc, sector, num_empleados, contacto_email) VALUES
(&#39;Manufacturas del Bajío S.A. de C.V.&#39;, &#39;MBA850101ABC&#39;, &#39;Manufactura&#39;, 320,
&#39;rh@manufacturasbajio.mx&#39;),
(&#39;Servicios Digitales Guanajuato S.C.&#39;, &#39;SDG120305XYZ&#39;, &#39;Tecnología&#39;, 45,
&#39;contacto@sdguanajuato.mx&#39;);
INSERT INTO auditores (nombre_completo, cedula_profesional, email) VALUES
(&#39;M.T.I. Eduardo Barrientos Avalos&#39;, &#39;12345678&#39;, &#39;jjbarrientos@utng.edu.mx&#39;),
(&#39;M.T.I. Anastacio Rodríguez García&#39;, &#39;87654321&#39;, &#39;tacho.rodriguez@utng.edu.mx&#39;);
INSERT INTO auditorias (id_empresa, id_auditor, fecha_inicio, fecha_fin, estatus) VALUES
(1, 1, &#39;2026-06-01&#39;, NULL, &#39;En proceso&#39;),
(2, 2, &#39;2026-05-10&#39;, &#39;2026-05-28&#39;, &#39;Concluida&#39;);
