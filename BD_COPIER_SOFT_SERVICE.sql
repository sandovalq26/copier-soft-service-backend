-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: db
-- Tiempo de generación: 10-06-2026 a las 01:37:01
-- Versión del servidor: 8.0.45
-- Versión de PHP: 8.3.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `BD_COPIER_SOFT_SERVICE`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ALMACEN`
--

CREATE TABLE `ALMACEN` (
  `COD_ALMACEN` char(5) COLLATE utf8mb4_general_ci NOT NULL,
  `NOMBRE` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `DIRECCION` varchar(250) COLLATE utf8mb4_general_ci NOT NULL
) ;

--
-- Volcado de datos para la tabla `ALMACEN`
--

INSERT INTO `ALMACEN` (`COD_ALMACEN`, `NOMBRE`, `DIRECCION`) VALUES
('A0001', 'PRINCIPAL', 'Av Malecon Checa 385 - Zarate'),
('A0002', 'SUCURSAL 1', 'Av Malecon Checa 385 - Zarate');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ALQUILER`
--

CREATE TABLE `ALQUILER` (
  `COD_ALQUILER` char(6) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `COD_EMPLEADO` char(5) COLLATE utf8mb4_general_ci NOT NULL,
  `COD_CLIENTE` char(5) COLLATE utf8mb4_general_ci NOT NULL,
  `FECHA_INICIO` date NOT NULL,
  `FECHA_FIN` date NOT NULL,
  `PERIODO` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `VALOR_PERIODO` int NOT NULL DEFAULT '1',
  `PRECIO` decimal(11,2) NOT NULL DEFAULT '0.00',
  `ESTADO` varchar(30) COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'REGISTRADO',
  `FECHA_REGISTRO` date NOT NULL DEFAULT (curdate())
) ;

--
-- Volcado de datos para la tabla `ALQUILER`
--

INSERT INTO `ALQUILER` (`COD_ALQUILER`, `COD_EMPLEADO`, `COD_CLIENTE`, `FECHA_INICIO`, `FECHA_FIN`, `PERIODO`, `VALOR_PERIODO`, `PRECIO`, `ESTADO`, `FECHA_REGISTRO`) VALUES
('AQ0001', 'E0001', 'C0001', '2026-04-01', '2026-04-30', 'SEMANA', 4, 200.00, 'FINALIZADO', '2026-04-24'),
('AQ0002', 'E0001', 'C0002', '2026-04-01', '2026-05-30', 'MES', 2, 400.00, 'EN EJECUCION', '2026-04-24'),
('AQ0003', 'E0001', 'C0010', '2026-05-01', '2026-05-30', 'SEMANA', 4, 400.00, 'FINALIZADO', '2026-05-02');

--
-- Disparadores `ALQUILER`
--
DELIMITER $$
CREATE TRIGGER `TRG_GEN_COD_ALQUILER` BEFORE INSERT ON `ALQUILER` FOR EACH ROW BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF NEW.COD_ALQUILER = '' THEN
        -- Extraer la parte numérica del código más alto existente
        SELECT COALESCE(MAX(CAST(SUBSTRING(COD_ALQUILER, 3) AS UNSIGNED)), 0)
          INTO v_max
          FROM ALQUILER;

        SET v_next = v_max + 1;

        IF v_next > 9999 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos ALQUILER alcanzado (AQ9999).';
        END IF;

        SET NEW.COD_ALQUILER = CONCAT('AQ', LPAD(v_next, 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ALQUILER_FOTOCOPIADORA`
--

CREATE TABLE `ALQUILER_FOTOCOPIADORA` (
  `COD_ALQUILER` char(6) COLLATE utf8mb4_general_ci NOT NULL,
  `COD_FOTOCOPIADORA` char(5) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ALQUILER_FOTOCOPIADORA`
--

INSERT INTO `ALQUILER_FOTOCOPIADORA` (`COD_ALQUILER`, `COD_FOTOCOPIADORA`) VALUES
('AQ0001', 'F0001'),
('AQ0002', 'F0002'),
('AQ0002', 'F0007');

--
-- Disparadores `ALQUILER_FOTOCOPIADORA`
--
DELIMITER $$
CREATE TRIGGER `TRG_CHK_DISPONIBILIDAD_FOTOC` BEFORE INSERT ON `ALQUILER_FOTOCOPIADORA` FOR EACH ROW BEGIN
    DECLARE v_conflicto INT DEFAULT 0;

    SELECT COUNT(*) INTO v_conflicto
    FROM ALQUILER_FOTOCOPIADORA af
    INNER JOIN ALQUILER a_nuevo      ON a_nuevo.COD_ALQUILER      = NEW.COD_ALQUILER
    INNER JOIN ALQUILER a_existente  ON a_existente.COD_ALQUILER  = af.COD_ALQUILER
    WHERE af.COD_FOTOCOPIADORA    = NEW.COD_FOTOCOPIADORA
      AND af.COD_ALQUILER         <> NEW.COD_ALQUILER
      AND a_nuevo.FECHA_INICIO    <= a_existente.FECHA_FIN
      AND a_existente.FECHA_INICIO <= a_nuevo.FECHA_FIN;

    IF v_conflicto > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La fotocopiadora ya esta asignada a otro alquiler en ese rango de fechas.';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `CLIENTE`
--

CREATE TABLE `CLIENTE` (
  `COD_CLIENTE` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `TIPO_DOCUMENTO` char(3) COLLATE utf8mb4_general_ci NOT NULL,
  `NUMERO_DOCUMENTO` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `NOMBRES` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `APELLIDOS` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `DIRECCION` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `TELEFONO` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CORREO` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `FECHA_REGISTRO` date NOT NULL DEFAULT (curdate())
) ;

--
-- Volcado de datos para la tabla `CLIENTE`
--

INSERT INTO `CLIENTE` (`COD_CLIENTE`, `TIPO_DOCUMENTO`, `NUMERO_DOCUMENTO`, `NOMBRES`, `APELLIDOS`, `DIRECCION`, `TELEFONO`, `CORREO`, `FECHA_REGISTRO`) VALUES
('C0001', 'DNI', '73857658', 'JOSE', 'LANDA AGUILAR', 'LIMA', '987654354', 'landa@gmail.com', '2024-01-10'),
('C0002', 'DNI', '73857659', 'YEISHON', 'MORENO GALLARDO', 'LIMA', '928475683', 'yeishon@gmail.com', '2024-02-14'),
('C0003', 'DNI', '70123456', 'MARIA', 'PEREZ LOPEZ', 'LIMA - SAN JUAN DE LURIGANCHO', '987321456', 'mperez@gmail.com', '2024-03-22'),
('C0004', 'DNI', '71234567', 'CARLOS', 'RAMIREZ TORRES', 'LIMA - LOS OLIVOS', '986654987', 'cramirez@gmail.com', '2024-05-05'),
('C0005', 'DNI', '72345678', 'ANA', 'GONZALES DIAZ', 'LIMA - SAN MIGUEL', '985987123', 'agonzales@gmail.com', '2024-06-30'),
('C0006', 'DNI', '73456789', 'LUIS', 'FLORES QUISPE', 'LIMA - ATE', '984123789', 'lflores@gmail.com', '2024-08-18'),
('C0007', 'DNI', '74567890', 'ROSA', 'CHAVEZ VEGA', 'LIMA - COMAS', '983456321', 'rchavez@gmail.com', '2024-09-03'),
('C0008', 'DNI', '75678901', 'JORGE', 'MEDINA HUAMAN', 'LIMA - SURCO', '982789654', 'jmedina@gmail.com', '2024-10-20'),
('C0009', 'DNI', '76789012', 'PATRICIA', 'SALAZAR CASTRO', 'LIMA - SAN BORJA', '981654987', 'psalazar@gmail.com', '2024-11-11'),
('C0010', 'DNI', '77890123', 'MIGUEL', 'TORRES ROJAS', 'LIMA - LA MOLINA', '980321789', 'mtorres@gmail.com', '2025-01-07');

--
-- Disparadores `CLIENTE`
--
DELIMITER $$
CREATE TRIGGER `TRG_GEN_COD_CLIENTE` BEFORE INSERT ON `CLIENTE` FOR EACH ROW BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF NEW.COD_CLIENTE = '' THEN
        SELECT COALESCE(MAX(CAST(SUBSTRING(COD_CLIENTE, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM CLIENTE;

        SET v_next = v_max + 1;

        IF v_next > 9999 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos CLIENTE alcanzado (C9999).';
        END IF;

        SET NEW.COD_CLIENTE = CONCAT('C', LPAD(v_next, 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `EMPLEADO`
--

CREATE TABLE `EMPLEADO` (
  `COD_EMPLEADO` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `TIPO_DOCUMENTO` char(3) COLLATE utf8mb4_general_ci NOT NULL,
  `NUMERO_DOCUMENTO` varchar(15) COLLATE utf8mb4_general_ci NOT NULL,
  `NOMBRES` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `APELLIDOS` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `CARGO` varchar(250) COLLATE utf8mb4_general_ci NOT NULL,
  `TELEFONO` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CORREO` varchar(150) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `USUARIO` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `CONTRASENA` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `FECHA_CONTRATACION` date NOT NULL DEFAULT (curdate())
) ;

--
-- Volcado de datos para la tabla `EMPLEADO`
--

INSERT INTO `EMPLEADO` (`COD_EMPLEADO`, `TIPO_DOCUMENTO`, `NUMERO_DOCUMENTO`, `NOMBRES`, `APELLIDOS`, `CARGO`, `TELEFONO`, `CORREO`, `USUARIO`, `CONTRASENA`, `FECHA_CONTRATACION`) VALUES
('E0001', 'DNI', '73456789', 'ERICK YAIR', 'MEZA YNGOL', 'TECNICO', '987654001', 'emeza@copiersoft.com', 'emeza', '630ab021ae9a18b8bc6772234dd684b8', '2023-03-15'),
('E0002', 'DNI', '74567890', 'FERNANDO', 'FERNANDEZ LEVANO', 'RECEPCIONISTA', '987654002', 'ffernandez@copiersoft.com', 'ffernandez', '18764048076a848a078ad65d970b7199', '2023-06-01'),
('E0003', 'DNI', '87653546', 'JOSE WILMER', 'SOLSOL YAHUARCANI', 'TECNICO', '987654003', 'jsolsol@copiersoft.com', 'jsolsol', '1c2ae9a98b53b09d29e19ba410340d9b', '2024-01-10'),
('E0004', 'DNI', '87654321', 'ENITH', 'SANDOVAL QUINTERO', 'ADMINISTRADOR', '987654004', 'esandoval@copiersoft.com', 'esandoval', '9948a1844d2b528021950df5ba7d32e6', '2024-08-20');

--
-- Disparadores `EMPLEADO`
--
DELIMITER $$
CREATE TRIGGER `TRG_GEN_COD_EMPLEADO` BEFORE INSERT ON `EMPLEADO` FOR EACH ROW BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF NEW.COD_EMPLEADO = '' THEN
        SELECT COALESCE(MAX(CAST(SUBSTRING(COD_EMPLEADO, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM EMPLEADO;

        SET v_next = v_max + 1;

        IF v_next > 9999 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos EMPLEADO alcanzado (E9999).';
        END IF;

        SET NEW.COD_EMPLEADO = CONCAT('E', LPAD(v_next, 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `FOTOCOPIADORA`
--

CREATE TABLE `FOTOCOPIADORA` (
  `COD_FOTOCOPIADORA` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `COD_ALMACEN` char(5) COLLATE utf8mb4_general_ci NOT NULL,
  `COD_PROVEEDOR` char(5) COLLATE utf8mb4_general_ci NOT NULL,
  `NOMBRE` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `MODELO` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `SERIE` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `MARCA` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
  `ANIO_FABRICACION` int NOT NULL,
  `ANCHO` int NOT NULL,
  `ALTO` int NOT NULL,
  `FONDO` int NOT NULL,
  `ESTADO` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `FECHA_REGISTRO` date NOT NULL DEFAULT (curdate())
) ;

--
-- Volcado de datos para la tabla `FOTOCOPIADORA`
--

INSERT INTO `FOTOCOPIADORA` (`COD_FOTOCOPIADORA`, `COD_ALMACEN`, `COD_PROVEEDOR`, `NOMBRE`, `MODELO`, `SERIE`, `MARCA`, `ANIO_FABRICACION`, `ANCHO`, `ALTO`, `FONDO`, `ESTADO`, `FECHA_REGISTRO`) VALUES
('F0001', 'A0001', 'P0001', 'CANON iR-ADV C3530 Multifuncional Color', 'IR-ADV C3530', 'SN12345678', 'CANON', 2022, 565, 1090, 770, 'DISPONIBLE', '2025-01-10'),
('F0002', 'A0001', 'P0002', 'KYOCERA TASKalfa 5003i Industrial Monocromo', 'TASKalfa 5003i', 'SN87654321', 'KYOCERA', 2021, 602, 1150, 790, 'ALQUILADO', '2025-01-15'),
('F0003', 'A0002', 'P0003', 'RICOH IM C3000 Multifuncional Color', 'IM C3000', 'SN30001234', 'RICOH', 2023, 587, 1130, 780, 'DISPONIBLE', '2025-01-20'),
('F0004', 'A0002', 'P0004', 'XEROX VersaLink C7025 Industrial Color', 'VERSALINK C7025', 'SN40005678', 'XEROX', 2022, 590, 1145, 785, 'ALQUILADO', '2025-01-22'),
('F0005', 'A0001', 'P0005', 'KYOCERA TASKalfa 3212i Multifuncional Mono', 'TASKalfa 3212i', 'SN50007890', 'KYOCERA', 2021, 570, 1100, 760, 'DISPONIBLE', '2025-01-25'),
('F0006', 'A0001', 'P0006', 'SHARP MX-3070N Multifuncional Color A3', 'MX-3070N', 'SN60003456', 'SHARP', 2020, 560, 1085, 750, 'MANTENIMIENTO', '2025-01-26'),
('F0007', 'A0002', 'P0007', 'BROTHER MFC-L8900CDW Laser Color Oficina', 'MFC-L8900CDW', 'SN70004567', 'BROTHER', 2022, 495, 549, 526, 'ALQUILADO', '2025-01-28'),
('F0008', 'A0001', 'P0008', 'EPSON WorkForce WF-C879R Multifuncional', 'WF-C879R', 'SN80006789', 'EPSON', 2023, 613, 1140, 780, 'ALQUILADO', '2025-01-30'),
('F0009', 'A0002', 'P0009', 'HP LaserJet E77830dn Multifuncional Mono', 'E77830dn', 'SN90001234', 'HP', 2021, 610, 1115, 775, 'DISPONIBLE', '2025-02-01'),
('F0010', 'A0001', 'P0010', 'LEXMARK MX622ade Multifuncional Monocromo', 'MX622ade', 'SN10000987', 'LEXMARK', 2021, 605, 1120, 770, 'MANTENIMIENTO', '2025-02-03'),
('F0011', 'A0001', 'P0002', 'Fotocopiadora Basica', 'CN2021', 'SN23723762763', 'Canon', 2025, 100, 100, 100, 'DISPONIBLE', '2026-04-30'),
('F0012', 'A0001', 'P0009', 'sss', 'sss', 'ssss', 'ssss', 2025, 100, 100, 100, 'MANTENIMIENTO', '2026-05-01');

--
-- Disparadores `FOTOCOPIADORA`
--
DELIMITER $$
CREATE TRIGGER `TRG_GEN_COD_FOTOCOPIADORA` BEFORE INSERT ON `FOTOCOPIADORA` FOR EACH ROW BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF NEW.COD_FOTOCOPIADORA = '' THEN
        SELECT COALESCE(MAX(CAST(SUBSTRING(COD_FOTOCOPIADORA, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM FOTOCOPIADORA;

        SET v_next = v_max + 1;

        IF v_next > 9999 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos FOTOCOPIADORA alcanzado (F9999).';
        END IF;

        SET NEW.COD_FOTOCOPIADORA = CONCAT('F', LPAD(v_next, 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `MEDIO_PAGO`
--

CREATE TABLE `MEDIO_PAGO` (
  `COD_MEDIO_PAGO` char(6) COLLATE utf8mb4_general_ci NOT NULL,
  `NOMBRE` varchar(100) COLLATE utf8mb4_general_ci NOT NULL
) ;

--
-- Volcado de datos para la tabla `MEDIO_PAGO`
--

INSERT INTO `MEDIO_PAGO` (`COD_MEDIO_PAGO`, `NOMBRE`) VALUES
('MP0005', 'DEPOSITO'),
('MP0001', 'EFECTIVO'),
('MP0003', 'PLIN'),
('MP0004', 'TRANSFERENCIA'),
('MP0002', 'YAPE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `PAGO`
--

CREATE TABLE `PAGO` (
  `COD_PAGO` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `TIPO_PAGO` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `COD_TIPO_COMPROBANTE` char(2) COLLATE utf8mb4_general_ci NOT NULL,
  `NUMERO_COMPROBANTE` varchar(9) COLLATE utf8mb4_general_ci NOT NULL,
  `FORMA_PAGO` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
  `COD_MEDIO_PAGO` char(6) COLLATE utf8mb4_general_ci NOT NULL,
  `FECHA_EMISION` date NOT NULL,
  `SUB_TOTAL` decimal(11,2) NOT NULL DEFAULT '0.00',
  `DESCUENTO` decimal(11,2) NOT NULL DEFAULT '0.00',
  `IGV` decimal(11,2) NOT NULL DEFAULT '0.00',
  `IMPORTE_TOTAL` decimal(11,2) NOT NULL DEFAULT '0.00',
  `FECHA_REGISTRO` date NOT NULL DEFAULT (curdate())
) ;

--
-- Volcado de datos para la tabla `PAGO`
--

INSERT INTO `PAGO` (`COD_PAGO`, `TIPO_PAGO`, `COD_TIPO_COMPROBANTE`, `NUMERO_COMPROBANTE`, `FORMA_PAGO`, `COD_MEDIO_PAGO`, `FECHA_EMISION`, `SUB_TOTAL`, `DESCUENTO`, `IGV`, `IMPORTE_TOTAL`, `FECHA_REGISTRO`) VALUES
('PG0001', 'ALQUILER', '01', 'F001-001', 'CONTADO', 'MP0001', '2026-04-28', 100.00, 0.00, 18.00, 118.00, '2026-04-29'),
('PG0002', 'ALQUILER', '01', 'F001-002', 'CONTADO', 'MP0002', '2026-04-28', 100.00, 0.00, 18.00, 118.00, '2026-04-29'),
('PG0003', 'ALQUILER', '01', 'F001-003', 'CONTADO', 'MP0001', '2026-04-30', 100.00, 0.00, 18.00, 118.00, '2026-04-30'),
('PG0004', 'ALQUILER', '01', 'F001-004', 'CONTADO', 'MP0002', '2026-05-01', 200.00, 0.00, 36.00, 236.00, '2026-05-01'),
('PG0005', 'ALQUILER', '01', 'F001-010', 'CONTADO', 'MP0004', '2026-05-30', 338.98, 0.00, 61.02, 400.00, '2026-05-02');

--
-- Disparadores `PAGO`
--
DELIMITER $$
CREATE TRIGGER `TRG_GEN_COD_PAGO` BEFORE INSERT ON `PAGO` FOR EACH ROW BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF NEW.COD_PAGO = '' THEN
        -- Extraer la parte numérica del código más alto existente
        SELECT COALESCE(MAX(CAST(SUBSTRING(COD_PAGO, 3) AS UNSIGNED)), 0)
          INTO v_max
          FROM PAGO;

        SET v_next = v_max + 1;

        IF v_next > 9999 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos PAGO alcanzado (PG9999).';
        END IF;

        SET NEW.COD_PAGO = CONCAT('PG', LPAD(v_next, 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `PAGO_ALQUILER`
--

CREATE TABLE `PAGO_ALQUILER` (
  `COD_PAGO` char(6) COLLATE utf8mb4_general_ci NOT NULL,
  `COD_ALQUILER` char(6) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `PAGO_ALQUILER`
--

INSERT INTO `PAGO_ALQUILER` (`COD_PAGO`, `COD_ALQUILER`) VALUES
('PG0001', 'AQ0001'),
('PG0002', 'AQ0001'),
('PG0003', 'AQ0001'),
('PG0004', 'AQ0002'),
('PG0005', 'AQ0003');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `PROVEEDOR`
--

CREATE TABLE `PROVEEDOR` (
  `COD_PROVEEDOR` char(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '',
  `RUC_EMPRESA` varchar(11) COLLATE utf8mb4_general_ci NOT NULL,
  `RAZON_SOCIAL` varchar(150) COLLATE utf8mb4_general_ci NOT NULL,
  `DIRECCION` varchar(250) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `CORREO` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `PROVEEDOR`
--

INSERT INTO `PROVEEDOR` (`COD_PROVEEDOR`, `RUC_EMPRESA`, `RAZON_SOCIAL`, `DIRECCION`, `CORREO`) VALUES
('P0001', '20987654321', 'KONICA MINOLTA', 'LIMA', 'ventas@konica.com'),
('P0002', '20123456789', 'CANON', 'LIMA - PERU', 'comercial@canon.com'),
('P0003', '20456789123', 'RICOH PERU S.A.C.', 'LIMA - SAN ISIDRO', 'ventas@ricoh.com.pe'),
('P0004', '20567891234', 'XEROX DEL PERU S.A.', 'LIMA - MIRAFLORES', 'contacto@xerox.com.pe'),
('P0005', '20678912345', 'KYOCERA DOCUMENT SOLUTIONS', 'LIMA - SURCO', 'ventas@kyocera.com.pe'),
('P0006', '20789123456', 'SHARP ELECTRONICS PERU', 'LIMA - LA MOLINA', 'comercial@sharp.com.pe'),
('P0007', '20891234567', 'BROTHER INTERNATIONAL PERU', 'LIMA - SAN BORJA', 'ventas@brother.com.pe'),
('P0008', '20912345678', 'EPSON PERU S.A.', 'LIMA - SAN MIGUEL', 'ventas@epson.com.pe'),
('P0009', '20111222333', 'HP PERU S.A.C.', 'LIMA - MAGDALENA', 'empresas@hp.com'),
('P0010', '20222333444', 'LEXMARK PERU S.A.', 'LIMA - JESUS MARIA', 'ventas@lexmark.com.pe'),
('P0011', '20747474774', 'prueba fffff', 'Lima', 'prueba@gmail.com');

--
-- Disparadores `PROVEEDOR`
--
DELIMITER $$
CREATE TRIGGER `TRG_GEN_COD_PROVEEDOR` BEFORE INSERT ON `PROVEEDOR` FOR EACH ROW BEGIN
    DECLARE v_max  INT DEFAULT 0;
    DECLARE v_next INT;

    IF NEW.COD_PROVEEDOR = '' THEN
        SELECT COALESCE(MAX(CAST(SUBSTRING(COD_PROVEEDOR, 2) AS UNSIGNED)), 0)
          INTO v_max
          FROM PROVEEDOR;

        SET v_next = v_max + 1;

        IF v_next > 9999 THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Límite máximo de códigos PROVEEDOR alcanzado (P9999).';
        END IF;

        SET NEW.COD_PROVEEDOR = CONCAT('P', LPAD(v_next, 4, '0'));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `TIPO_COMPROBANTE`
--

CREATE TABLE `TIPO_COMPROBANTE` (
  `COD_TIPO_COMPROBANTE` char(2) COLLATE utf8mb4_general_ci NOT NULL,
  `NOMBRE` varchar(100) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `TIPO_COMPROBANTE`
--

INSERT INTO `TIPO_COMPROBANTE` (`COD_TIPO_COMPROBANTE`, `NOMBRE`) VALUES
('00', 'OTROS'),
('01', 'FACTURA'),
('02', 'RECIBO POR HONORARIOS'),
('03', 'BOLETA DE VENTA'),
('04', 'LIQUIDACIÓN DE COMPRA'),
('07', 'NOTA DE CRÉDITO'),
('08', 'NOTA DE DÉBITO');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `ALMACEN`
--
ALTER TABLE `ALMACEN`
  ADD PRIMARY KEY (`COD_ALMACEN`);

--
-- Indices de la tabla `ALQUILER`
--
ALTER TABLE `ALQUILER`
  ADD PRIMARY KEY (`COD_ALQUILER`),
  ADD KEY `FK_COD_EMPLEADO_ALQ` (`COD_EMPLEADO`),
  ADD KEY `FK_COD_CLIENTE_ALQ` (`COD_CLIENTE`);

--
-- Indices de la tabla `ALQUILER_FOTOCOPIADORA`
--
ALTER TABLE `ALQUILER_FOTOCOPIADORA`
  ADD PRIMARY KEY (`COD_ALQUILER`,`COD_FOTOCOPIADORA`),
  ADD KEY `FK_COD_FOTOCOPIADORA_ALQ_FOTOC` (`COD_FOTOCOPIADORA`);

--
-- Indices de la tabla `CLIENTE`
--
ALTER TABLE `CLIENTE`
  ADD PRIMARY KEY (`COD_CLIENTE`);

--
-- Indices de la tabla `EMPLEADO`
--
ALTER TABLE `EMPLEADO`
  ADD PRIMARY KEY (`COD_EMPLEADO`),
  ADD UNIQUE KEY `UQ_USUARIO_EMP` (`USUARIO`);

--
-- Indices de la tabla `FOTOCOPIADORA`
--
ALTER TABLE `FOTOCOPIADORA`
  ADD PRIMARY KEY (`COD_FOTOCOPIADORA`),
  ADD KEY `FK_COD_ALMACEN_FOTOC` (`COD_ALMACEN`),
  ADD KEY `FK_COD_PROVEEDOR_FOTOC` (`COD_PROVEEDOR`);

--
-- Indices de la tabla `MEDIO_PAGO`
--
ALTER TABLE `MEDIO_PAGO`
  ADD PRIMARY KEY (`COD_MEDIO_PAGO`),
  ADD UNIQUE KEY `UQ_NOMBRE_MP` (`NOMBRE`);

--
-- Indices de la tabla `PAGO`
--
ALTER TABLE `PAGO`
  ADD PRIMARY KEY (`COD_PAGO`),
  ADD KEY `FK_COD_TIPO_COMPROBANTE_PG` (`COD_TIPO_COMPROBANTE`),
  ADD KEY `FK_COD_MEDIO_PAGO_PG` (`COD_MEDIO_PAGO`);

--
-- Indices de la tabla `PAGO_ALQUILER`
--
ALTER TABLE `PAGO_ALQUILER`
  ADD PRIMARY KEY (`COD_PAGO`,`COD_ALQUILER`),
  ADD KEY `FK_COD_ALQUILER_PA` (`COD_ALQUILER`);

--
-- Indices de la tabla `PROVEEDOR`
--
ALTER TABLE `PROVEEDOR`
  ADD PRIMARY KEY (`COD_PROVEEDOR`),
  ADD UNIQUE KEY `UQ_PROVEEDOR` (`RUC_EMPRESA`);

--
-- Indices de la tabla `TIPO_COMPROBANTE`
--
ALTER TABLE `TIPO_COMPROBANTE`
  ADD PRIMARY KEY (`COD_TIPO_COMPROBANTE`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `ALQUILER`
--
ALTER TABLE `ALQUILER`
  ADD CONSTRAINT `FK_COD_CLIENTE_ALQ` FOREIGN KEY (`COD_CLIENTE`) REFERENCES `CLIENTE` (`COD_CLIENTE`),
  ADD CONSTRAINT `FK_COD_EMPLEADO_ALQ` FOREIGN KEY (`COD_EMPLEADO`) REFERENCES `EMPLEADO` (`COD_EMPLEADO`);

--
-- Filtros para la tabla `ALQUILER_FOTOCOPIADORA`
--
ALTER TABLE `ALQUILER_FOTOCOPIADORA`
  ADD CONSTRAINT `FK_COD_ALQUILER_ALQ_FOTOC` FOREIGN KEY (`COD_ALQUILER`) REFERENCES `ALQUILER` (`COD_ALQUILER`),
  ADD CONSTRAINT `FK_COD_FOTOCOPIADORA_ALQ_FOTOC` FOREIGN KEY (`COD_FOTOCOPIADORA`) REFERENCES `FOTOCOPIADORA` (`COD_FOTOCOPIADORA`);

--
-- Filtros para la tabla `FOTOCOPIADORA`
--
ALTER TABLE `FOTOCOPIADORA`
  ADD CONSTRAINT `FK_COD_ALMACEN_FOTOC` FOREIGN KEY (`COD_ALMACEN`) REFERENCES `ALMACEN` (`COD_ALMACEN`),
  ADD CONSTRAINT `FK_COD_PROVEEDOR_FOTOC` FOREIGN KEY (`COD_PROVEEDOR`) REFERENCES `PROVEEDOR` (`COD_PROVEEDOR`);

--
-- Filtros para la tabla `PAGO`
--
ALTER TABLE `PAGO`
  ADD CONSTRAINT `FK_COD_MEDIO_PAGO_PG` FOREIGN KEY (`COD_MEDIO_PAGO`) REFERENCES `MEDIO_PAGO` (`COD_MEDIO_PAGO`),
  ADD CONSTRAINT `FK_COD_TIPO_COMPROBANTE_PG` FOREIGN KEY (`COD_TIPO_COMPROBANTE`) REFERENCES `TIPO_COMPROBANTE` (`COD_TIPO_COMPROBANTE`);

--
-- Filtros para la tabla `PAGO_ALQUILER`
--
ALTER TABLE `PAGO_ALQUILER`
  ADD CONSTRAINT `FK_COD_ALQUILER_PA` FOREIGN KEY (`COD_ALQUILER`) REFERENCES `ALQUILER` (`COD_ALQUILER`),
  ADD CONSTRAINT `FK_COD_PAGO_PA` FOREIGN KEY (`COD_PAGO`) REFERENCES `PAGO` (`COD_PAGO`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
