/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE TABLE IF NOT EXISTS `microemprendimientos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activo` bit(1) NOT NULL,
  `ciudad` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `gestionado` bit(1) NOT NULL,
  `imagenes` varchar(255) DEFAULT NULL,
  `mas_informacion` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `pais` varchar(255) DEFAULT NULL,
  `provincia` varchar(255) DEFAULT NULL,
  `subcategoria` varchar(255) DEFAULT NULL,
  `id_categoria` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2s9ucw3bp1xjo9f63gk88kxaw` (`id_categoria`),
  CONSTRAINT `FK2s9ucw3bp1xjo9f63gk88kxaw` FOREIGN KEY (`id_categoria`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DELETE FROM `microemprendimientos`;
/*!40000 ALTER TABLE `microemprendimientos` DISABLE KEYS */;
INSERT INTO `microemprendimientos` (`id`, `activo`, `ciudad`, `descripcion`, `gestionado`, `imagenes`, `mas_informacion`, `nombre`, `pais`, `provincia`, `subcategoria`, `id_categoria`) VALUES
	(1, b'1', 'Rosario', 'Un emprendimiento dedicado a promover la sostenibilidad y el bienestar a través de productos orgánicos y prácticas agroecológicas.', b'0', 'null', 'Más información sobre nuestro compromiso con la alimentación saludable y el medio ambiente.', 'Eco Emprendimiento', 'Argentina', 'Santa Fe', 'Productos orgánicos', 2),
	(2, b'1', 'Santiago', 'Emprendimiento enfocado en la economía circular y la reducción de residuos.', b'0', 'null', 'Innovamos para reducir, reutilizar y reciclar, generando impacto positivo en el medio ambiente.', 'Verde Innovación', 'Chile', 'Metropolitana', 'Reciclaje creativo', 4),
	(3, b'1', 'Lima', 'Promovemos la inclusión financiera y el desarrollo local a través de microfinanzas.', b'0', 'null', 'Facilitamos el acceso a servicios financieros para comunidades desatendidas.', 'Impacto Positivo', 'Perú', 'Lima', 'Microfinanzas', 1),
	(4, b'1', 'Manaus', 'Fomentamos la regeneración de ecosistemas a través de proyectos de reforestación.', b'0', 'null', 'Nuestros proyectos buscan restaurar la biodiversidad y mitigar el cambio climático.', 'Sostenibilidad Activa', 'Brasil', 'Amazonas', 'Reforestación', 3),
	(5, b'1', 'Montevideo', 'Dedicados a la producción de alimentos orgánicos y saludables.', b'0', 'null', 'Proveemos productos frescos y libres de químicos para una vida más saludable.', 'Naturaleza Viva', 'Uruguay', 'Montevideo', 'Huertos urbanos', 2),
	(6, b'1', 'Medellín', 'Empresa dedicada a la limpieza y conservación de áreas naturales.', b'0', 'null', 'Realizamos actividades de limpieza de playas, ríos y bosques para preservar la naturaleza.', 'Ecosistema Limpio', 'Colombia', 'Antioquia', 'Limpieza ambiental', 3),
	(7, b'1', 'Guadalajara', 'Impulsamos la producción y consumo de productos orgánicos.', b'0', 'null', 'Ofrecemos una variedad de productos naturales que contribuyen a una vida más saludable.', 'Vida Orgánica', 'México', 'Jalisco', 'Alimentos orgánicos', 2),
	(8, b'1', 'Barcelona', 'Promovemos prácticas de economía circular en la gestión de residuos.', b'0', 'null', 'Desarrollamos soluciones innovadoras para transformar residuos en recursos.', 'Ciclo Verde', 'España', 'Cataluña', 'Gestión de residuos', 4),
	(9, b'1', 'San José', 'Empresa enfocada en la conservación de recursos naturales y la educación ambiental.', b'0', 'null', 'Realizamos talleres y actividades educativas para promover la conciencia ecológica.', 'Futuro Ecológico', 'Costa Rica', 'San José', 'Educación ambiental', 3),
	(10, b'1', 'Quito', 'Promovemos el desarrollo local a través de proyectos de inclusión financiera.', b'0', 'null', 'Nuestros programas están diseñados para empoderar a comunidades a través de servicios financieros accesibles.', 'Crecimiento Sostenible', 'Ecuador', 'Pichincha', 'Empoderamiento comunitario', 1);
/*!40000 ALTER TABLE `microemprendimientos` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
