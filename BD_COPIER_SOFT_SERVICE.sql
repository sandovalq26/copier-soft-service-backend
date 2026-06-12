-- ============================================================
-- base de datos: copier_soft_service
-- motor: mysql 8.0+
-- conversión desde sql server
-- excluye: mantenimiento, pago_mantenimiento
-- ============================================================

-- eliminar y crear la base de datos
DROP DATABASE IF EXISTS copier_soft_service;
CREATE DATABASE copier_soft_service
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE copier_soft_service;

-- habilitar comprobación de claves foráneas (activo por defecto en mysql)
SET FOREIGN_KEY_CHECKS = 1;


-- ============================================================
-- tabla: almacen
-- nota: like con pattern de sql server → REGEXP en CHECK de mysql 8.0.16+
-- ============================================================
CREATE TABLE almacen (
    cod_almacen  CHAR(5)      NOT NULL,
    nombre       VARCHAR(50)  NOT NULL,
    direccion    VARCHAR(250) NOT NULL,
    CONSTRAINT pk_almacen PRIMARY KEY (cod_almacen),
    CONSTRAINT chk_cod_almacen CHECK (cod_almacen REGEXP '^A[0-9]{4}$')
) ENGINE=innodb;


-- ============================================================
-- tabla: proveedor
-- ============================================================
CREATE TABLE proveedor (
    cod_proveedor CHAR(5)      NOT NULL DEFAULT '',
    ruc_empresa   VARCHAR(11)  NOT NULL,
    razon_social  VARCHAR(150) NOT NULL,
    direccion     VARCHAR(250) NULL,
    correo        VARCHAR(100) NULL,
    CONSTRAINT pk_proveedor  PRIMARY KEY (cod_proveedor),
    CONSTRAINT uq_proveedor  UNIQUE (ruc_empresa),
    CONSTRAINT chk_cod_proveedor CHECK (cod_proveedor REGEXP '^P[0-9]{4}$')
) ENGINE=innodb;


-- ============================================================
-- tabla: empleado
-- nota: len() → char_length() | getdate() → curdate()
--       CHECK compuesto con REGEXP + condiciones por tipo documento
-- ============================================================
CREATE TABLE empleado (
    cod_empleado        CHAR(5)      NOT NULL DEFAULT '',
    tipo_documento      CHAR(3)      NOT NULL,
    numero_documento    VARCHAR(15)  NOT NULL,
    nombres             VARCHAR(50)  NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    cargo               VARCHAR(250) NOT NULL,
    telefono            VARCHAR(30)  NULL,
    correo              VARCHAR(150) NULL,
    usuario             VARCHAR(50)  NOT NULL,
    contrasena          VARCHAR(255) NOT NULL,
    fecha_contratacion  DATE         NOT NULL DEFAULT (curdate()),
    CONSTRAINT pk_empleado PRIMARY KEY (cod_empleado),
    CONSTRAINT uq_usuario_emp UNIQUE (usuario),
    CONSTRAINT chk_cod_empleado
        CHECK (cod_empleado REGEXP '^E[0-9]{4}$'),
    CONSTRAINT chk_tipo_documento_emp
        CHECK (tipo_documento IN ('DNI', 'RUC', 'CEX')),
    -- solo dígitos y longitud según tipo de documento
    CONSTRAINT chk_numero_documento_emp
        CHECK (
            numero_documento REGEXP '^[0-9]+$'
            AND (
                (tipo_documento = 'DNI' AND char_length(numero_documento) = 8)
                OR (tipo_documento = 'RUC' AND char_length(numero_documento) = 11)
                OR (tipo_documento = 'CEX' AND char_length(numero_documento) BETWEEN 8 AND 15)
            )
        )
) ENGINE=innodb;


-- ============================================================
-- tabla: cliente
-- ============================================================
CREATE TABLE cliente (
    cod_cliente      CHAR(5)      NOT NULL DEFAULT '',
    tipo_documento   CHAR(3)      NOT NULL,
    numero_documento VARCHAR(15)  NOT NULL,
    nombres          VARCHAR(50)  NOT NULL,
    apellidos        VARCHAR(100) NOT NULL,
    direccion        VARCHAR(250) NULL,
    telefono         VARCHAR(30)  NULL,
    correo           VARCHAR(150) NULL,
    fecha_registro   DATE         NOT NULL DEFAULT (curdate()),
    CONSTRAINT pk_cliente PRIMARY KEY (cod_cliente),
    CONSTRAINT chk_cod_cliente
        CHECK (cod_cliente REGEXP '^C[0-9]{4}$'),
    -- el script original tiene 'Dni' para cliente; se normaliza a 'DNI'
    CONSTRAINT chk_tipo_documento_cli
        CHECK (tipo_documento IN ('DNI', 'RUC', 'CEX')),
    CONSTRAINT chk_numero_documento_cli
        CHECK (
            numero_documento REGEXP '^[0-9]+$'
            AND (
                (tipo_documento = 'DNI' AND char_length(numero_documento) = 8)
                OR (tipo_documento = 'RUC' AND char_length(numero_documento) = 11)
                OR (tipo_documento = 'CEX' AND char_length(numero_documento) BETWEEN 8 AND 15)
            )
        )
) ENGINE=innodb;


-- ============================================================
-- tabla: fotocopiadora
-- nota: INT para año/dimensiones; CHECK de rango numérico directo
-- ============================================================
CREATE TABLE fotocopiadora (
    cod_fotocopiadora CHAR(5)      NOT NULL DEFAULT '',
    cod_almacen       CHAR(5)      NOT NULL,
    cod_proveedor     CHAR(5)      NOT NULL,
    nombre            VARCHAR(150) NOT NULL,
    modelo            VARCHAR(100) NOT NULL,
    serie             VARCHAR(30)  NOT NULL,
    marca             VARCHAR(50)  NOT NULL,
    anio_fabricacion  INT          NOT NULL,
    ancho             INT          NOT NULL,
    alto              INT          NOT NULL,
    fondo             INT          NOT NULL,
    estado            VARCHAR(30)  NOT NULL,
    fecha_registro    DATE         NOT NULL DEFAULT (curdate()),
    CONSTRAINT pk_fotocopiadora PRIMARY KEY (cod_fotocopiadora),
    CONSTRAINT chk_cod_fotocopiadora
        CHECK (cod_fotocopiadora REGEXP '^F[0-9]{4}$'),
    CONSTRAINT fk_cod_almacen_fotoc
        FOREIGN KEY (cod_almacen)   REFERENCES almacen (cod_almacen),
    CONSTRAINT fk_cod_proveedor_fotoc
        FOREIGN KEY (cod_proveedor) REFERENCES proveedor (cod_proveedor),
    CONSTRAINT chk_anio_fabricacion_fotoc
        CHECK (anio_fabricacion >= 1 AND anio_fabricacion <= 9999),
    CONSTRAINT chk_ancho_fotoc
        CHECK (ancho >= 1 AND ancho <= 9999),
    CONSTRAINT chk_alto_fotoc
        CHECK (alto  >= 1 AND alto  <= 9999),
    CONSTRAINT chk_fondo_fotoc
        CHECK (fondo >= 1 AND fondo <= 9999),
    CONSTRAINT chk_estado_fotoc
        CHECK (estado IN ('DISPONIBLE', 'ALQUILADO', 'MANTENIMIENTO'))
) ENGINE=innodb;


-- ============================================================
-- tabla: alquiler
-- nota: DEFAULT expresiones con paréntesis (mysql 8.0.13+)
-- ============================================================
CREATE TABLE alquiler (
    cod_alquiler    CHAR(6)        NOT NULL DEFAULT '',
    cod_empleado    CHAR(5)        NOT NULL,
    cod_cliente     CHAR(5)        NOT NULL,
    fecha_inicio    DATE           NOT NULL,
    fecha_fin       DATE           NOT NULL,
    periodo         VARCHAR(15)    NOT NULL,
    valor_periodo   INT            NOT NULL DEFAULT 1,
    precio          DECIMAL(11,2)  NOT NULL DEFAULT 0.0,
    estado          VARCHAR(30)    NOT NULL DEFAULT 'REGISTRADO',
    fecha_registro  DATE           NOT NULL DEFAULT (curdate()),
    CONSTRAINT pk_alquiler PRIMARY KEY (cod_alquiler),
    CONSTRAINT chk_cod_alquiler
        CHECK (cod_alquiler REGEXP '^AQ[0-9]{4}$'),
    CONSTRAINT fk_cod_empleado_alq
        FOREIGN KEY (cod_empleado) REFERENCES empleado (cod_empleado),
    CONSTRAINT fk_cod_cliente_alq
        FOREIGN KEY (cod_cliente)  REFERENCES cliente  (cod_cliente),
    CONSTRAINT chk_periodo_alq
        CHECK (periodo IN ('DIA', 'SEMANA', 'MES')),
    CONSTRAINT chk_valor_periodo_alq
        CHECK (valor_periodo >= 1 AND valor_periodo <= 9999),
    CONSTRAINT chk_estado_alq
        CHECK (estado IN ('REGISTRADO', 'EN EJECUCION', 'FINALIZADO'))
) ENGINE=innodb;


-- ============================================================
-- tabla: alquiler_fotocopiadora  (tabla pivote n:m)
-- ============================================================
CREATE TABLE alquiler_fotocopiadora (
    cod_alquiler      CHAR(6) NOT NULL,
    cod_fotocopiadora CHAR(5) NOT NULL,
    CONSTRAINT pk_alquiler_fotocopiadora
        PRIMARY KEY (cod_alquiler, cod_fotocopiadora),
    CONSTRAINT fk_cod_alquiler_alq_fotoc
        FOREIGN KEY (cod_alquiler)      REFERENCES alquiler      (cod_alquiler),
    CONSTRAINT fk_cod_fotocopiadora_alq_fotoc
        FOREIGN KEY (cod_fotocopiadora) REFERENCES fotocopiadora (cod_fotocopiadora)
) ENGINE=innodb;


-- ============================================================
-- tabla: medio_pago
-- ============================================================
CREATE TABLE medio_pago (
    cod_medio_pago CHAR(6)      NOT NULL,
    nombre         VARCHAR(100) NOT NULL,
    CONSTRAINT pk_medio_pago  PRIMARY KEY (cod_medio_pago),
    CONSTRAINT uq_nombre_mp   UNIQUE (nombre),
    CONSTRAINT chk_cod_medio_pago
        CHECK (cod_medio_pago REGEXP '^MP[0-9]{4}$')
) ENGINE=innodb;


-- ============================================================
-- tabla: tipo_comprobante
-- ============================================================
CREATE TABLE tipo_comprobante (
    cod_tipo_comprobante CHAR(2)      NOT NULL,
    nombre               VARCHAR(100) NOT NULL,
    CONSTRAINT pk_tipo_comprobante PRIMARY KEY (cod_tipo_comprobante)
) ENGINE=innodb;


-- ============================================================
-- tabla: pago
-- nota: tipo_pago solo 'ALQUILER' (mantenimiento excluido)
-- ============================================================
CREATE TABLE pago (
    cod_pago              CHAR(6)       NOT NULL DEFAULT '',
    tipo_pago             VARCHAR(30)   NOT NULL,
    cod_tipo_comprobante  CHAR(2)       NOT NULL,
    numero_comprobante    VARCHAR(9)    NOT NULL,
    forma_pago            VARCHAR(30)   NOT NULL,
    cod_medio_pago        CHAR(6)       NOT NULL,
    fecha_emision         DATE          NOT NULL,
    sub_total             DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    descuento             DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    igv                   DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    importe_total         DECIMAL(11,2) NOT NULL DEFAULT 0.0,
    fecha_registro        DATE          NOT NULL DEFAULT (curdate()),
    CONSTRAINT pk_pago PRIMARY KEY (cod_pago),
    CONSTRAINT chk_cod_pago
        CHECK (cod_pago REGEXP '^PG[0-9]{4}$'),
    CONSTRAINT chk_tipo_pago_pg
        CHECK (tipo_pago = 'ALQUILER'),
    CONSTRAINT fk_cod_tipo_comprobante_pg
        FOREIGN KEY (cod_tipo_comprobante) REFERENCES tipo_comprobante (cod_tipo_comprobante),
    CONSTRAINT chk_forma_pago_pg
        CHECK (forma_pago IN ('CREDITO', 'CONTADO')),
    CONSTRAINT fk_cod_medio_pago_pg
        FOREIGN KEY (cod_medio_pago) REFERENCES medio_pago (cod_medio_pago)
) ENGINE=innodb;


-- ============================================================
-- tabla: pago_alquiler  (tabla pivote pago ↔ alquiler)
-- ============================================================
CREATE TABLE pago_alquiler (
    cod_pago     CHAR(6) NOT NULL,
    cod_alquiler CHAR(6) NOT NULL,
    CONSTRAINT pk_pago_alquiler PRIMARY KEY (cod_pago, cod_alquiler),
    CONSTRAINT fk_cod_pago_pa
        FOREIGN KEY (cod_pago)     REFERENCES pago     (cod_pago),
    CONSTRAINT fk_cod_alquiler_pa
        FOREIGN KEY (cod_alquiler) REFERENCES alquiler (cod_alquiler)
) ENGINE=innodb;


-- ============================================================
-- TRIGGER: auto-generar cod_alquiler con formato aq0001…aq9999
-- ============================================================
DROP TRIGGER IF EXISTS trg_gen_cod_alquiler;
DELIMITER $$
CREATE TRIGGER trg_gen_cod_alquiler
BEFORE INSERT ON alquiler
FOR EACH ROW
BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF new.cod_alquiler = '' then
        -- extraer la parte numérica del código más alto existente
        SELECT COALESCE(MAX(CAST(SUBSTRING(cod_alquiler, 3) AS UNSIGNED)), 0)
          INTO v_max
          FROM alquiler;

        SET v_next = v_max + 1;

        IF v_next > 9999 then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos ALQUILER alcanzado (AQ9999).';
        END IF;

        SET new.cod_alquiler = CONCAT('AQ', LPAD(v_next, 4, '0'));
    END IF;
END$$
DELIMITER ;


-- ============================================================
-- TRIGGER: auto-generar cod_pago con formato pg0001…pg9999
-- ============================================================
DROP TRIGGER IF EXISTS trg_gen_cod_pago;
DELIMITER $$
CREATE TRIGGER trg_gen_cod_pago
BEFORE INSERT ON pago
FOR EACH ROW
BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF new.cod_pago = '' then
        -- extraer la parte numérica del código más alto existente
        SELECT COALESCE(MAX(CAST(SUBSTRING(cod_pago, 3) AS UNSIGNED)), 0)
          INTO v_max
          FROM pago;

        SET v_next = v_max + 1;

        IF v_next > 9999 then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos PAGO alcanzado (PG9999).';
        END IF;

        SET new.cod_pago = CONCAT('PG', LPAD(v_next, 4, '0'));
    END IF;
END$$
DELIMITER ;


-- ============================================================
-- TRIGGER: auto-generar cod_fotocopiadora con formato f0001…f9999
-- ============================================================
DROP TRIGGER IF EXISTS trg_gen_cod_fotocopiadora;
DELIMITER $$
CREATE TRIGGER trg_gen_cod_fotocopiadora
BEFORE INSERT ON fotocopiadora
FOR EACH ROW
BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF new.cod_fotocopiadora = '' then
        SELECT COALESCE(MAX(CAST(SUBSTRING(cod_fotocopiadora, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM fotocopiadora;

        SET v_next = v_max + 1;

        IF v_next > 9999 then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos FOTOCOPIADORA alcanzado (F9999).';
        END IF;

        SET new.cod_fotocopiadora = CONCAT('F', LPAD(v_next, 4, '0'));
    END IF;
END$$
DELIMITER ;


-- ============================================================
-- TRIGGER: auto-generar cod_proveedor con formato p0001…p9999
-- ============================================================
DROP TRIGGER IF EXISTS trg_gen_cod_proveedor;
DELIMITER $$
CREATE TRIGGER trg_gen_cod_proveedor
BEFORE INSERT ON proveedor
FOR EACH ROW
BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF new.cod_proveedor = '' then
        SELECT COALESCE(MAX(CAST(SUBSTRING(cod_proveedor, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM proveedor;

        SET v_next = v_max + 1;

        IF v_next > 9999 then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos PROVEEDOR alcanzado (P9999).';
        END IF;

        SET new.cod_proveedor = CONCAT('P', LPAD(v_next, 4, '0'));
    END IF;
END$$
DELIMITER ;


-- ============================================================
-- TRIGGER: auto-generar cod_empleado con formato e0001…e9999
-- ============================================================
DROP TRIGGER IF EXISTS trg_gen_cod_empleado;
DELIMITER $$
CREATE TRIGGER trg_gen_cod_empleado
BEFORE INSERT ON empleado
FOR EACH ROW
BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF new.cod_empleado = '' then
        SELECT COALESCE(MAX(CAST(SUBSTRING(cod_empleado, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM empleado;

        SET v_next = v_max + 1;

        IF v_next > 9999 then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos EMPLEADO alcanzado (E9999).';
        END IF;

        SET new.cod_empleado = CONCAT('E', LPAD(v_next, 4, '0'));
    END IF;
END$$
DELIMITER ;


-- ============================================================
-- TRIGGER: auto-generar cod_cliente con formato c0001…c9999
-- ============================================================
DROP TRIGGER IF EXISTS trg_gen_cod_cliente;
DELIMITER $$
CREATE TRIGGER trg_gen_cod_cliente
BEFORE INSERT ON cliente
FOR EACH ROW
BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF new.cod_cliente = '' then
        SELECT COALESCE(MAX(CAST(SUBSTRING(cod_cliente, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM cliente;

        SET v_next = v_max + 1;

        IF v_next > 9999 then
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos CLIENTE alcanzado (C9999).';
        END IF;

        SET new.cod_cliente = CONCAT('C', LPAD(v_next, 4, '0'));
    END IF;
END$$
DELIMITER ;


-- ============================================================
-- datos de prueba
-- ============================================================

INSERT INTO almacen (cod_almacen, nombre, direccion) VALUES
('A0001', 'PRINCIPAL',  'Av Malecon Checa 385 - Zarate'),
('A0002', 'SUCURSAL 1', 'Av Malecon Checa 385 - Zarate');


INSERT INTO proveedor (cod_proveedor, ruc_empresa, razon_social, direccion, correo) VALUES
('P0001', '20987654321', 'KONICA MINOLTA',               'LIMA',                'ventas@konica.com'),
('P0002', '20123456789', 'CANON',                        'LIMA - PERU',         'comercial@canon.com'),
('P0003', '20456789123', 'RICOH PERU S.A.C.',            'LIMA - SAN ISIDRO',   'ventas@ricoh.com.pe'),
('P0004', '20567891234', 'XEROX DEL PERU S.A.',          'LIMA - MIRAFLORES',   'contacto@xerox.com.pe'),
('P0005', '20678912345', 'KYOCERA DOCUMENT SOLUTIONS',   'LIMA - SURCO',        'ventas@kyocera.com.pe'),
('P0006', '20789123456', 'SHARP ELECTRONICS PERU',       'LIMA - LA MOLINA',    'comercial@sharp.com.pe'),
('P0007', '20891234567', 'BROTHER INTERNATIONAL PERU',   'LIMA - SAN BORJA',    'ventas@brother.com.pe'),
('P0008', '20912345678', 'EPSON PERU S.A.',              'LIMA - SAN MIGUEL',   'ventas@epson.com.pe'),
('P0009', '20111222333', 'HP PERU S.A.C.',               'LIMA - MAGDALENA',    'empresas@hp.com'),
('P0010', '20222333444', 'LEXMARK PERU S.A.',            'LIMA - JESUS MARIA',  'ventas@lexmark.com.pe');


INSERT INTO empleado (cod_empleado, tipo_documento, numero_documento, nombres, apellidos, cargo, telefono, correo, usuario, contrasena, fecha_contratacion) VALUES
('E0001', 'DNI', '73456789', 'ERICK YAIR',  'MEZA YNGOL',        'SUPERVISOR',          '987654001', 'emeza@copiersoft.com',      'emeza',      md5('emeza123'),      '2023-03-15'),
('E0002', 'DNI', '74567890', 'FERNANDO',    'FERNANDEZ LEVANO',  'ATENCION AL CLIENTE', '987654002', 'ffernandez@copiersoft.com', 'ffernandez', md5('ffernandez123'), '2023-06-01'),
('E0003', 'DNI', '87653546', 'JOSE WILMER', 'SOLSOL YAHUARCANI', 'TECNICO',             '987654003', 'jsolsol@copiersoft.com',    'jsolsol',    md5('jsolsol123'),    '2024-01-10'),
('E0004', 'DNI', '87654321', 'ENITH',       'SANDOVAL QUINTERO', 'ADMINISTRACION',      '987654004', 'esandoval@copiersoft.com',  'esandoval',  md5('esandoval123'),  '2024-08-20');


-- nota: el script original tiene numero_documento duplicado ('73857658') en c0001 y c0002.
--       se corrige c0002 para cumplir la unicidad lógica (no hay UNIQUE definido, pero es un dato erróneo).
INSERT INTO cliente (cod_cliente, tipo_documento, numero_documento, nombres, apellidos, direccion, telefono, correo, fecha_registro) VALUES
('C0001', 'DNI', '73857658', 'JOSE',     'LANDA AGUILAR',    'LIMA',                          '987654354', 'landa@gmail.com',     '2024-01-10'),
('C0002', 'DNI', '73857659', 'YEISHON',  'MORENO GALLARDO',  'LIMA',                          '928475683', 'yeishon@gmail.com',   '2024-02-14'),
('C0003', 'DNI', '70123456', 'MARIA',    'PEREZ LOPEZ',      'LIMA - SAN JUAN DE LURIGANCHO', '987321456', 'mperez@gmail.com',    '2024-03-22'),
('C0004', 'DNI', '71234567', 'CARLOS',   'RAMIREZ TORRES',   'LIMA - LOS OLIVOS',             '986654987', 'cramirez@gmail.com',  '2024-05-05'),
('C0005', 'DNI', '72345678', 'ANA',      'GONZALES DIAZ',    'LIMA - SAN MIGUEL',             '985987123', 'agonzales@gmail.com', '2024-06-30'),
('C0006', 'DNI', '73456789', 'LUIS',     'FLORES QUISPE',    'LIMA - ATE',                    '984123789', 'lflores@gmail.com',   '2024-08-18'),
('C0007', 'DNI', '74567890', 'ROSA',     'CHAVEZ VEGA',      'LIMA - COMAS',                  '983456321', 'rchavez@gmail.com',   '2024-09-03'),
('C0008', 'DNI', '75678901', 'JORGE',    'MEDINA HUAMAN',    'LIMA - SURCO',                  '982789654', 'jmedina@gmail.com',   '2024-10-20'),
('C0009', 'DNI', '76789012', 'PATRICIA', 'SALAZAR CASTRO',   'LIMA - SAN BORJA',              '981654987', 'psalazar@gmail.com',  '2024-11-11'),
('C0010', 'DNI', '77890123', 'MIGUEL',   'TORRES ROJAS',     'LIMA - LA MOLINA',              '980321789', 'mtorres@gmail.com',   '2025-01-07');


INSERT INTO fotocopiadora (cod_fotocopiadora, cod_almacen, cod_proveedor, nombre, modelo, serie, marca, anio_fabricacion, ancho, alto, fondo, estado, fecha_registro) VALUES
('F0001', 'A0001', 'P0001', 'CANON iR-ADV C3530 Multifuncional Color',     'IR-ADV C3530',   'SN12345678', 'CANON',   2022, 565, 1090, 770, 'DISPONIBLE',    '2025-01-10'),
('F0002', 'A0001', 'P0002', 'KYOCERA TASKalfa 5003i Industrial Monocromo', 'TASKalfa 5003i', 'SN87654321', 'KYOCERA', 2021, 602, 1150, 790, 'DISPONIBLE',    '2025-01-15'),
('F0003', 'A0002', 'P0003', 'RICOH IM C3000 Multifuncional Color',         'IM C3000',       'SN30001234', 'RICOH',   2023, 587, 1130, 780, 'DISPONIBLE',    '2025-01-20'),
('F0004', 'A0002', 'P0004', 'XEROX VersaLink C7025 Industrial Color',      'VERSALINK C7025','SN40005678', 'XEROX',   2022, 590, 1145, 785, 'ALQUILADO',     '2025-01-22'),
('F0005', 'A0001', 'P0005', 'KYOCERA TASKalfa 3212i Multifuncional Mono',  'TASKalfa 3212i', 'SN50007890', 'KYOCERA', 2021, 570, 1100, 760, 'DISPONIBLE',    '2025-01-25'),
('F0006', 'A0001', 'P0006', 'SHARP MX-3070N Multifuncional Color A3',      'MX-3070N',       'SN60003456', 'SHARP',   2020, 560, 1085, 750, 'MANTENIMIENTO', '2025-01-26'),
('F0007', 'A0002', 'P0007', 'BROTHER MFC-L8900CDW Laser Color Oficina',    'MFC-L8900CDW',   'SN70004567', 'BROTHER', 2022, 495,  549, 526, 'DISPONIBLE',    '2025-01-28'),
('F0008', 'A0001', 'P0008', 'EPSON WorkForce WF-C879R Multifuncional',     'WF-C879R',       'SN80006789', 'EPSON',   2023, 613, 1140, 780, 'ALQUILADO',     '2025-01-30'),
('F0009', 'A0002', 'P0009', 'HP LaserJet E77830dn Multifuncional Mono',    'E77830dn',       'SN90001234', 'HP',      2021, 610, 1115, 775, 'DISPONIBLE',    '2025-02-01'),
('F0010', 'A0001', 'P0010', 'LEXMARK MX622ade Multifuncional Monocromo',   'MX622ade',       'SN10000987', 'LEXMARK', 2020, 605, 1120, 770, 'MANTENIMIENTO', '2025-02-03');


-- aq0001-aq0005 finalizado (tienen pago registrado); aq0006-aq0010 registrado (pendientes)
INSERT INTO alquiler (cod_alquiler, cod_empleado, cod_cliente, fecha_inicio, fecha_fin, periodo, valor_periodo, precio, estado, fecha_registro) VALUES
('AQ0001', 'E0001', 'C0001', '2025-11-01', '2025-12-20', 'MES',    1, 1500.00, 'FINALIZADO', '2025-11-01'),
('AQ0002', 'E0002', 'C0002', '2025-01-05', '2025-02-05', 'MES',    1, 1400.00, 'FINALIZADO', '2025-01-05'),
('AQ0003', 'E0003', 'C0003', '2025-02-10', '2025-03-10', 'MES',    1, 1600.00, 'FINALIZADO', '2025-02-10'),
('AQ0004', 'E0004', 'C0004', '2025-03-01', '2025-03-31', 'MES',    1, 1550.00, 'FINALIZADO', '2025-03-01'),
('AQ0005', 'E0001', 'C0005', '2025-04-05', '2025-04-12', 'SEMANA', 1,  480.00, 'FINALIZADO', '2025-04-05'),
('AQ0006', 'E0002', 'C0006', '2025-05-01', '2025-05-31', 'MES',    1, 1700.00, 'REGISTRADO', '2025-05-01'),
('AQ0007', 'E0003', 'C0007', '2025-06-01', '2025-06-30', 'MES',    1, 1650.00, 'REGISTRADO', '2025-06-01'),
('AQ0008', 'E0004', 'C0008', '2025-07-10', '2025-07-17', 'SEMANA', 1,  520.00, 'REGISTRADO', '2025-07-10'),
('AQ0009', 'E0001', 'C0009', '2025-08-01', '2025-08-01', 'DIA',    1,  120.00, 'REGISTRADO', '2025-08-01'),
('AQ0010', 'E0002', 'C0010', '2025-09-01', '2025-12-01', 'MES',    3, 4500.00, 'REGISTRADO', '2025-09-01');


INSERT INTO alquiler_fotocopiadora (cod_alquiler, cod_fotocopiadora) VALUES
('AQ0001', 'F0001'),
('AQ0002', 'F0001'),
('AQ0002', 'F0002'),
('AQ0003', 'F0003'),
('AQ0004', 'F0004'),
('AQ0004', 'F0005'),
('AQ0005', 'F0006'),
('AQ0005', 'F0007'),
('AQ0005', 'F0008'),
('AQ0006', 'F0002'),
('AQ0007', 'F0009'),
('AQ0007', 'F0010'),
('AQ0008', 'F0001'),
('AQ0008', 'F0003'),
('AQ0008', 'F0005'),
('AQ0009', 'F0004'),
('AQ0010', 'F0006'),
('AQ0010', 'F0008'),
('AQ0010', 'F0010');


INSERT INTO medio_pago (cod_medio_pago, nombre) VALUES
('MP0001', 'EFECTIVO'),
('MP0002', 'YAPE'),
('MP0003', 'PLIN'),
('MP0004', 'TRANSFERENCIA'),
('MP0005', 'DEPOSITO');


INSERT INTO tipo_comprobante (cod_tipo_comprobante, nombre) VALUES
('00', 'OTROS'),
('01', 'FACTURA'),
('02', 'RECIBO POR HONORARIOS'),
('03', 'BOLETA DE VENTA'),
('04', 'LIQUIDACIÓN DE COMPRA'),
('07', 'NOTA DE CRÉDITO'),
('08', 'NOTA DE DÉBITO');


-- pagos alineados con precio de cada alquiler (igv 18%). fecha_emision = fecha_fin del alquiler.
INSERT INTO pago (cod_pago, tipo_pago, cod_tipo_comprobante, numero_comprobante, forma_pago, cod_medio_pago, fecha_emision, sub_total, descuento, igv,    importe_total, fecha_registro) VALUES
('PG0001', 'ALQUILER', '03', 'B001-123', 'CONTADO', 'MP0002', '2025-12-20', 1500.00, 0.00, 270.00, 1770.00, '2025-12-20'),  -- aq0001: 1500 x 1.18
('PG0002', 'ALQUILER', '03', 'B001-124', 'CONTADO', 'MP0001', '2025-02-05', 1400.00, 0.00, 252.00, 1652.00, '2025-02-05'),  -- aq0002: 1400 x 1.18
('PG0003', 'ALQUILER', '01', 'F001-046', 'CONTADO', 'MP0004', '2025-03-10', 1600.00, 0.00, 288.00, 1888.00, '2025-03-10'),  -- aq0003: 1600 x 1.18
('PG0004', 'ALQUILER', '03', 'B001-125', 'CONTADO', 'MP0001', '2025-03-31', 1550.00, 0.00, 279.00, 1829.00, '2025-03-31'),  -- aq0004: 1550 x 1.18
('PG0005', 'ALQUILER', '03', 'B001-126', 'CONTADO', 'MP0003', '2025-04-12',  480.00, 0.00,  86.40,  566.40, '2025-04-12'); -- aq0005: 480  x 1.18


INSERT INTO pago_alquiler (cod_pago, cod_alquiler) VALUES
('PG0001', 'AQ0001'),
('PG0002', 'AQ0002'),
('PG0003', 'AQ0003'),
('PG0004', 'AQ0004'),
('PG0005', 'AQ0005');

