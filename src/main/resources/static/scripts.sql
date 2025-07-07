/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for rpd_db
CREATE DATABASE IF NOT EXISTS `rpd_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `rpd_db`;

-- Dumping structure for table rpd_db.academic_degrees
CREATE TABLE IF NOT EXISTS `academic_degrees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `name` varchar(50) NOT NULL,
  `short_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.academic_degrees: ~2 rows (approximately)
DELETE FROM `academic_degrees`;
/*!40000 ALTER TABLE `academic_degrees` DISABLE KEYS */;
INSERT INTO `academic_degrees` (`id`, `disabled`, `name`, `short_name`) VALUES
	(1, b'0', 'Кандидат наук', 'Кандидат наук'),
	(2, b'0', 'Доктор наук', 'Доктор наук');
/*!40000 ALTER TABLE `academic_degrees` ENABLE KEYS */;

-- Dumping structure for table rpd_db.academic_ranks
CREATE TABLE IF NOT EXISTS `academic_ranks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.academic_ranks: ~2 rows (approximately)
DELETE FROM `academic_ranks`;
/*!40000 ALTER TABLE `academic_ranks` DISABLE KEYS */;
INSERT INTO `academic_ranks` (`id`, `disabled`, `name`) VALUES
	(1, b'0', 'Доцент'),
	(2, b'0', 'Профессор');
/*!40000 ALTER TABLE `academic_ranks` ENABLE KEYS */;

-- Dumping structure for table rpd_db.auditoriums
CREATE TABLE IF NOT EXISTS `auditoriums` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(100) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `equipment` text,
  `software` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.auditoriums: ~0 rows (approximately)
DELETE FROM `auditoriums`;
/*!40000 ALTER TABLE `auditoriums` DISABLE KEYS */;
/*!40000 ALTER TABLE `auditoriums` ENABLE KEYS */;

-- Dumping structure for table rpd_db.basic_educational_programs
CREATE TABLE IF NOT EXISTS `basic_educational_programs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `academic_year` int(11) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `education_type_id` int(11) NOT NULL,
  `profile_id` int(11) NOT NULL,
  `protocol_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfets8o6qmg1bf27e4s3q63p3d` (`education_type_id`),
  KEY `FK7nakdkd065su4kayv9dhwj8re` (`profile_id`),
  KEY `FKt96l08xxkru1ivttit22492o1` (`protocol_id`),
  CONSTRAINT `FK7nakdkd065su4kayv9dhwj8re` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKfets8o6qmg1bf27e4s3q63p3d` FOREIGN KEY (`education_type_id`) REFERENCES `education_types` (`id`),
  CONSTRAINT `FKt96l08xxkru1ivttit22492o1` FOREIGN KEY (`protocol_id`) REFERENCES `protocols` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.basic_educational_programs: ~0 rows (approximately)
DELETE FROM `basic_educational_programs`;
/*!40000 ALTER TABLE `basic_educational_programs` DISABLE KEYS */;
/*!40000 ALTER TABLE `basic_educational_programs` ENABLE KEYS */;

-- Dumping structure for table rpd_db.basic_educational_program_disciplines
CREATE TABLE IF NOT EXISTS `basic_educational_program_disciplines` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `discipline_index` varchar(20) NOT NULL,
  `basic_educational_program_id` int(11) NOT NULL,
  `discipline_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4gl9yd9nvigd9yxyfjgsa77kh` (`basic_educational_program_id`),
  KEY `FK9hsp4k13w4jry46dq7u6c4sfs` (`discipline_id`),
  CONSTRAINT `FK4gl9yd9nvigd9yxyfjgsa77kh` FOREIGN KEY (`basic_educational_program_id`) REFERENCES `basic_educational_programs` (`id`),
  CONSTRAINT `FK9hsp4k13w4jry46dq7u6c4sfs` FOREIGN KEY (`discipline_id`) REFERENCES `disciplines` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.basic_educational_program_disciplines: ~0 rows (approximately)
DELETE FROM `basic_educational_program_disciplines`;
/*!40000 ALTER TABLE `basic_educational_program_disciplines` DISABLE KEYS */;
/*!40000 ALTER TABLE `basic_educational_program_disciplines` ENABLE KEYS */;

-- Dumping structure for table rpd_db.basic_educational_program_discipline_auditoriums
CREATE TABLE IF NOT EXISTS `basic_educational_program_discipline_auditoriums` (
  `basic_educational_program_discipline_id` int(11) NOT NULL,
  `auditorium_id` int(11) NOT NULL,
  PRIMARY KEY (`basic_educational_program_discipline_id`,`auditorium_id`),
  KEY `FK6hbo9uj028mnikj9q3wdtd0lr` (`auditorium_id`),
  CONSTRAINT `FK6hbo9uj028mnikj9q3wdtd0lr` FOREIGN KEY (`auditorium_id`) REFERENCES `auditoriums` (`id`),
  CONSTRAINT `FKbi11ditjgf23hkjarnia7a84b` FOREIGN KEY (`basic_educational_program_discipline_id`) REFERENCES `basic_educational_program_disciplines` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.basic_educational_program_discipline_auditoriums: ~0 rows (approximately)
DELETE FROM `basic_educational_program_discipline_auditoriums`;
/*!40000 ALTER TABLE `basic_educational_program_discipline_auditoriums` DISABLE KEYS */;
/*!40000 ALTER TABLE `basic_educational_program_discipline_auditoriums` ENABLE KEYS */;

-- Dumping structure for table rpd_db.basic_educational_program_discipline_indicators
CREATE TABLE IF NOT EXISTS `basic_educational_program_discipline_indicators` (
  `basic_educational_program_discipline_id` int(11) NOT NULL,
  `indicator_id` int(11) NOT NULL,
  PRIMARY KEY (`basic_educational_program_discipline_id`,`indicator_id`),
  KEY `FKo9uayn496mragrgsq9u1xll0f` (`indicator_id`),
  CONSTRAINT `FKd7nd2gvc6rnt81cc5nb48hspw` FOREIGN KEY (`basic_educational_program_discipline_id`) REFERENCES `basic_educational_program_disciplines` (`id`),
  CONSTRAINT `FKo9uayn496mragrgsq9u1xll0f` FOREIGN KEY (`indicator_id`) REFERENCES `indicators` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.basic_educational_program_discipline_indicators: ~0 rows (approximately)
DELETE FROM `basic_educational_program_discipline_indicators`;
/*!40000 ALTER TABLE `basic_educational_program_discipline_indicators` DISABLE KEYS */;
/*!40000 ALTER TABLE `basic_educational_program_discipline_indicators` ENABLE KEYS */;

-- Dumping structure for table rpd_db.basic_educational_program_discipline_protocols
CREATE TABLE IF NOT EXISTS `basic_educational_program_discipline_protocols` (
  `basic_educational_program_discipline_id` int(11) NOT NULL,
  `protocol_id` int(11) NOT NULL,
  PRIMARY KEY (`basic_educational_program_discipline_id`,`protocol_id`),
  KEY `FKr0117tdy6xuv3hd9sf7uoovp0` (`protocol_id`),
  CONSTRAINT `FKofhug64ddfhqtoxk9nk7svpby` FOREIGN KEY (`basic_educational_program_discipline_id`) REFERENCES `basic_educational_program_disciplines` (`id`),
  CONSTRAINT `FKr0117tdy6xuv3hd9sf7uoovp0` FOREIGN KEY (`protocol_id`) REFERENCES `protocols` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.basic_educational_program_discipline_protocols: ~0 rows (approximately)
DELETE FROM `basic_educational_program_discipline_protocols`;
/*!40000 ALTER TABLE `basic_educational_program_discipline_protocols` DISABLE KEYS */;
/*!40000 ALTER TABLE `basic_educational_program_discipline_protocols` ENABLE KEYS */;

-- Dumping structure for table rpd_db.competences
CREATE TABLE IF NOT EXISTS `competences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `essence` text NOT NULL,
  `competence_index` varchar(100) NOT NULL,
  `type` enum('OP','P','U') NOT NULL,
  `basic_educational_program_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtk4y9m3yd008yf19de86hk3t5` (`basic_educational_program_id`),
  CONSTRAINT `FKtk4y9m3yd008yf19de86hk3t5` FOREIGN KEY (`basic_educational_program_id`) REFERENCES `basic_educational_programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.competences: ~0 rows (approximately)
DELETE FROM `competences`;
/*!40000 ALTER TABLE `competences` DISABLE KEYS */;
/*!40000 ALTER TABLE `competences` ENABLE KEYS */;

-- Dumping structure for table rpd_db.departments
CREATE TABLE IF NOT EXISTS `departments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `abbreviation` varchar(50) DEFAULT NULL,
  `code` varchar(10) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `head_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbl8pi22vx8dv15r86mf4m9gdp` (`head_id`),
  CONSTRAINT `FKbl8pi22vx8dv15r86mf4m9gdp` FOREIGN KEY (`head_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.departments: ~7 rows (approximately)
DELETE FROM `departments`;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` (`id`, `abbreviation`, `code`, `disabled`, `name`, `head_id`) VALUES
	(1, 'Гражданского Права', '1', b'0', 'Кафедра гражданского права', 1),
	(2, 'ЭиПМ', '2', b'0', 'Кафедра экономики и промышленного менеджмента', 1),
	(3, 'АП и ИТ', '3', b'0', 'Кафедра автоматизации производства и информационных технологий', 1),
	(4, 'ТМС и САПР', '4', b'0', 'Кафедра технологии машиностроения и систем автоматизированного проектирования', 1),
	(5, 'СП', '5', b'0', 'Кафедра строительного производства', 1),
	(6, 'ДВС', '6', b'0', 'Кафедра двигателей внутреннего сгорания', 1),
	(7, 'ЕНД', '7', b'0', 'Кафедра естественно-научных дисциплин', 1);
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;

-- Dumping structure for table rpd_db.directions
CREATE TABLE IF NOT EXISTS `directions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `department_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgl6bqa70qeennadxla0kbn4fh` (`department_id`),
  CONSTRAINT `FKgl6bqa70qeennadxla0kbn4fh` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.directions: ~0 rows (approximately)
DELETE FROM `directions`;
/*!40000 ALTER TABLE `directions` DISABLE KEYS */;
/*!40000 ALTER TABLE `directions` ENABLE KEYS */;

-- Dumping structure for table rpd_db.disciplines
CREATE TABLE IF NOT EXISTS `disciplines` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.disciplines: ~0 rows (approximately)
DELETE FROM `disciplines`;
/*!40000 ALTER TABLE `disciplines` DISABLE KEYS */;
/*!40000 ALTER TABLE `disciplines` ENABLE KEYS */;

-- Dumping structure for table rpd_db.education_types
CREATE TABLE IF NOT EXISTS `education_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `learning_period` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `text` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.education_types: ~0 rows (approximately)
DELETE FROM `education_types`;
/*!40000 ALTER TABLE `education_types` DISABLE KEYS */;
/*!40000 ALTER TABLE `education_types` ENABLE KEYS */;

-- Dumping structure for table rpd_db.employees
CREATE TABLE IF NOT EXISTS `employees` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `middle_name` varchar(100) DEFAULT NULL,
  `employee_position_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf408nylqjiwajq7165sa8ml4f` (`employee_position_id`),
  CONSTRAINT `FKf408nylqjiwajq7165sa8ml4f` FOREIGN KEY (`employee_position_id`) REFERENCES `employee_positions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.employees: ~0 rows (approximately)
DELETE FROM `employees`;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;

-- Dumping structure for table rpd_db.employee_positions
CREATE TABLE IF NOT EXISTS `employee_positions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `name` varchar(50) NOT NULL,
  `type` enum('ACADEMIC','ADMINISTRATIVE') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.employee_positions: ~5 rows (approximately)
DELETE FROM `employee_positions`;
/*!40000 ALTER TABLE `employee_positions` DISABLE KEYS */;
INSERT INTO `employee_positions` (`id`, `disabled`, `name`, `type`) VALUES
	(1, b'0', 'Директор', 'ADMINISTRATIVE'),
	(2, b'0', 'Заведующий кафедрой', 'ADMINISTRATIVE'),
	(3, b'0', 'Преподаватель', 'ACADEMIC'),
	(4, b'0', 'Старший преподаватель', 'ACADEMIC'),
	(5, b'0', 'Доцент', 'ACADEMIC'),
	(6, b'0', 'Профессор', 'ACADEMIC');
/*!40000 ALTER TABLE `employee_positions` ENABLE KEYS */;

-- Dumping structure for table rpd_db.files_rpd
CREATE TABLE IF NOT EXISTS `files_rpd` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `academic_year` int(11) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `section_0` longblob,
  `section_0_is_load` bit(1) NOT NULL,
  `section_1` longblob,
  `section_1_is_load` bit(1) NOT NULL,
  `section_2` longblob,
  `section_2_is_load` bit(1) NOT NULL,
  `section_3` longblob,
  `section_3_is_load` bit(1) NOT NULL,
  `section_4` longblob,
  `section_4_is_load` bit(1) NOT NULL,
  `section_5` longblob,
  `section_5_is_load` bit(1) NOT NULL,
  `section_6` longblob,
  `section_6_is_load` bit(1) NOT NULL,
  `section_7` longblob,
  `section_7_is_load` bit(1) NOT NULL,
  `section_8` longblob,
  `section_8_is_load` bit(1) NOT NULL,
  `section_9` longblob,
  `section_9_is_load` bit(1) NOT NULL,
  `discipline_educational_program_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7d8ngs056fjlue5rdnr0d6c7k` (`discipline_educational_program_id`),
  CONSTRAINT `FK7d8ngs056fjlue5rdnr0d6c7k` FOREIGN KEY (`discipline_educational_program_id`) REFERENCES `basic_educational_program_disciplines` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.files_rpd: ~0 rows (approximately)
DELETE FROM `files_rpd`;
/*!40000 ALTER TABLE `files_rpd` DISABLE KEYS */;
/*!40000 ALTER TABLE `files_rpd` ENABLE KEYS */;

-- Dumping structure for table rpd_db.indicators
CREATE TABLE IF NOT EXISTS `indicators` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `text` text NOT NULL,
  `type` enum('BE_ABLE','KNOW','OWN') NOT NULL,
  `competence_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkayjx58k0s64s6ixtdi1xoooc` (`competence_id`),
  CONSTRAINT `FKkayjx58k0s64s6ixtdi1xoooc` FOREIGN KEY (`competence_id`) REFERENCES `competences` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.indicators: ~0 rows (approximately)
DELETE FROM `indicators`;
/*!40000 ALTER TABLE `indicators` DISABLE KEYS */;
/*!40000 ALTER TABLE `indicators` ENABLE KEYS */;

-- Dumping structure for table rpd_db.placeholders
CREATE TABLE IF NOT EXISTS `placeholders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `text` text,
  `type` enum('SQL','TEXT') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.placeholders: ~0 rows (approximately)
DELETE FROM `placeholders`;
/*!40000 ALTER TABLE `placeholders` DISABLE KEYS */;
/*!40000 ALTER TABLE `placeholders` ENABLE KEYS */;

-- Dumping structure for table rpd_db.profiles
CREATE TABLE IF NOT EXISTS `profiles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `direction_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc038k5tfah7u6d6jpxqyam19l` (`direction_id`),
  CONSTRAINT `FKc038k5tfah7u6d6jpxqyam19l` FOREIGN KEY (`direction_id`) REFERENCES `directions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.profiles: ~0 rows (approximately)
DELETE FROM `profiles`;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;

-- Dumping structure for table rpd_db.protocols
CREATE TABLE IF NOT EXISTS `protocols` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date1` date NOT NULL DEFAULT '2000-01-01',
  `date2` date NOT NULL DEFAULT '2000-01-01',
  `disabled` bit(1) NOT NULL,
  `number1` int(11) NOT NULL,
  `number2` int(11) NOT NULL,
  `type` enum('ACTUALIZE','APPROVE') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.protocols: ~0 rows (approximately)
DELETE FROM `protocols`;
/*!40000 ALTER TABLE `protocols` DISABLE KEYS */;
/*!40000 ALTER TABLE `protocols` ENABLE KEYS */;

-- Dumping structure for table rpd_db.protocol_developers
CREATE TABLE IF NOT EXISTS `protocol_developers` (
  `protocol_id` int(11) NOT NULL,
  `developer_id` int(11) NOT NULL,
  PRIMARY KEY (`protocol_id`,`developer_id`),
  KEY `FKn7qda3pdk6a11nmrxa22m3vi5` (`developer_id`),
  CONSTRAINT `FKn7qda3pdk6a11nmrxa22m3vi5` FOREIGN KEY (`developer_id`) REFERENCES `teachers` (`id`),
  CONSTRAINT `FKphn0q3037eg0qbp8aya3gmk2r` FOREIGN KEY (`protocol_id`) REFERENCES `protocols` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.protocol_developers: ~0 rows (approximately)
DELETE FROM `protocol_developers`;
/*!40000 ALTER TABLE `protocol_developers` DISABLE KEYS */;
/*!40000 ALTER TABLE `protocol_developers` ENABLE KEYS */;

-- Dumping structure for table rpd_db.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.roles: ~2 rows (approximately)
DELETE FROM `roles`;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`id`, `disabled`, `name`) VALUES
	(1, b'0', 'administrator'),
	(2, b'0', 'user');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

-- Dumping structure for table rpd_db.teachers
CREATE TABLE IF NOT EXISTS `teachers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `academic_degree_id` int(11) DEFAULT NULL,
  `academic_rank_id` int(11) DEFAULT NULL,
  `department_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `employee_position_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmwjycea4inm6837t4ap0qbgph` (`academic_degree_id`),
  KEY `FKdmrjxp1372ph0nm8kfv5qkjyl` (`academic_rank_id`),
  KEY `FKrgr03njnvpwuktc0mntf8t6o0` (`department_id`),
  KEY `FKcr8lwab4nll2jdn7y2t9r6hu3` (`employee_id`),
  KEY `FKh74tl97wnjwujckkp8hqy0hw4` (`employee_position_id`),
  CONSTRAINT `FKcr8lwab4nll2jdn7y2t9r6hu3` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
  CONSTRAINT `FKdmrjxp1372ph0nm8kfv5qkjyl` FOREIGN KEY (`academic_rank_id`) REFERENCES `academic_ranks` (`id`),
  CONSTRAINT `FKh74tl97wnjwujckkp8hqy0hw4` FOREIGN KEY (`employee_position_id`) REFERENCES `employee_positions` (`id`),
  CONSTRAINT `FKmwjycea4inm6837t4ap0qbgph` FOREIGN KEY (`academic_degree_id`) REFERENCES `academic_degrees` (`id`),
  CONSTRAINT `FKrgr03njnvpwuktc0mntf8t6o0` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.teachers: ~0 rows (approximately)
DELETE FROM `teachers`;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;

-- Dumping structure for table rpd_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(100) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.users: ~2 rows (approximately)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `disabled`, `password`, `username`, `role_id`) VALUES
	(1, b'0', '$2a$10$X7P45PLNGAjXTlxAjY.kjeOnpEeaJXcGaarNFCqsGW9GGUgwcSm.O', 'Администратор', 1),
	(2, b'0', '$2a$10$j3MvKjp0siFjLyk5CTdBQO3LnIHfwScm6bY7Dv3RAxZ7NDt8E.DXa', 'Пользователь', 2);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
