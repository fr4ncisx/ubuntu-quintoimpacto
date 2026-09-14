/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `countries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

DELETE FROM `countries`;
/*!40000 ALTER TABLE `countries` DISABLE KEYS */;
INSERT INTO `countries` (`id`, `name`) VALUES
	(1, 'Argentina'),
	(2, 'Brasil'),
	(3, 'Uruguay');
/*!40000 ALTER TABLE `countries` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `provinces` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `country_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp37jnxd9htn7o8v195jm4uvre` (`country_id`),
  CONSTRAINT `FKp37jnxd9htn7o8v195jm4uvre` FOREIGN KEY (`country_id`) REFERENCES `countries` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

DELETE FROM `provinces`;
/*!40000 ALTER TABLE `provinces` DISABLE KEYS */;
INSERT INTO `provinces` (`id`, `name`, `country_id`) VALUES
	(1, 'Buenos Aires', 1),
	(2, 'Córdoba', 1),
	(3, 'Santa Fe', 1),
	(4, 'Entre Ríos', 1),
	(5, 'Mendoza', 1),
	(6, 'San Juan', 1),
	(7, 'San Luis', 1),
	(8, 'La Rioja', 1),
	(9, 'Catamarca', 1),
	(10, 'Tucumán', 1),
	(11, 'Salta', 1),
	(12, 'Jujuy', 1),
	(13, 'Chaco', 1),
	(14, 'Corrientes', 1),
	(15, 'Misiones', 1),
	(16, 'Formosa', 1),
	(17, 'Neuquén', 1),
	(18, 'Río Negro', 1),
	(19, 'Chubut', 1),
	(20, 'Santa Cruz', 1),
	(21, 'Tierra del Fuego', 1),
	(22, 'La Pampa', 1),
	(23, 'Ciudad Autónoma de Buenos Aires', 1),
	(24, 'Sao Paulo', 2),
	(25, 'Rio de Janeiro', 2),
	(26, 'Minas Gerais', 2),
	(27, 'Montevideo', 3),
	(28, 'Salto', 3),
	(29, 'Paysandu', 3),
	(30, 'Maldonado', 3),
	(31, 'Rivera', 3);
/*!40000 ALTER TABLE `provinces` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
