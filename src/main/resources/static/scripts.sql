/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for rpd_db
CREATE DATABASE IF NOT EXISTS `rpd_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `rpd_db`;

-- Dumping structure for table rpd_db.audiences
CREATE TABLE IF NOT EXISTS `audiences` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `number_audience` varchar(50) NOT NULL,
  `software_license` text,
  `tech` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.audiences: ~0 rows (approximately)
DELETE FROM `audiences`;
/*!40000 ALTER TABLE `audiences` DISABLE KEYS */;
/*!40000 ALTER TABLE `audiences` ENABLE KEYS */;

-- Dumping structure for table rpd_db.basic_educational_programs
CREATE TABLE IF NOT EXISTS `basic_educational_programs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `academic_year` int(11) DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `education_type_id` int(11) NOT NULL,
  `profile_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfets8o6qmg1bf27e4s3q63p3d` (`education_type_id`),
  KEY `FK7nakdkd065su4kayv9dhwj8re` (`profile_id`),
  CONSTRAINT `FK7nakdkd065su4kayv9dhwj8re` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKfets8o6qmg1bf27e4s3q63p3d` FOREIGN KEY (`education_type_id`) REFERENCES `education_types` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.basic_educational_programs: ~0 rows (approximately)
DELETE FROM `basic_educational_programs`;
/*!40000 ALTER TABLE `basic_educational_programs` DISABLE KEYS */;
/*!40000 ALTER TABLE `basic_educational_programs` ENABLE KEYS */;

-- Dumping structure for table rpd_db.competencies
CREATE TABLE IF NOT EXISTS `competencies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `be_able` text,
  `code` varchar(100) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `essence` text NOT NULL,
  `know` text,
  `own` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.competencies: ~0 rows (approximately)
DELETE FROM `competencies`;
/*!40000 ALTER TABLE `competencies` DISABLE KEYS */;
/*!40000 ALTER TABLE `competencies` ENABLE KEYS */;

-- Dumping structure for table rpd_db.competencies_disciplines_educational_programs
CREATE TABLE IF NOT EXISTS `competencies_disciplines_educational_programs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `competence_id` int(11) NOT NULL,
  `discipline_educational_program_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtk5wyy78d93b4so8vo0pudvbe` (`competence_id`),
  KEY `FKb7okmd8efnpshqgb7xyuy54g4` (`discipline_educational_program_id`),
  CONSTRAINT `FKb7okmd8efnpshqgb7xyuy54g4` FOREIGN KEY (`discipline_educational_program_id`) REFERENCES `disciplines_educational_programs` (`id`),
  CONSTRAINT `FKtk5wyy78d93b4so8vo0pudvbe` FOREIGN KEY (`competence_id`) REFERENCES `competencies` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.competencies_disciplines_educational_programs: ~0 rows (approximately)
DELETE FROM `competencies_disciplines_educational_programs`;
/*!40000 ALTER TABLE `competencies_disciplines_educational_programs` DISABLE KEYS */;
/*!40000 ALTER TABLE `competencies_disciplines_educational_programs` ENABLE KEYS */;

-- Dumping structure for table rpd_db.departaments
CREATE TABLE IF NOT EXISTS `departaments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `abbreviation` varchar(50) DEFAULT NULL,
  `code` varchar(10) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `manager_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnh7vla4sfeagob4c8i95nmdrw` (`manager_id`),
  CONSTRAINT `FKnh7vla4sfeagob4c8i95nmdrw` FOREIGN KEY (`manager_id`) REFERENCES `employees` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.departaments: ~0 rows (approximately)
DELETE FROM `departaments`;
/*!40000 ALTER TABLE `departaments` DISABLE KEYS */;
/*!40000 ALTER TABLE `departaments` ENABLE KEYS */;

-- Dumping structure for table rpd_db.directions
CREATE TABLE IF NOT EXISTS `directions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `encryption` varchar(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `departament_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK253s4oh4ih46qna06548l4i6v` (`departament_id`),
  CONSTRAINT `FK253s4oh4ih46qna06548l4i6v` FOREIGN KEY (`departament_id`) REFERENCES `departaments` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.directions: ~0 rows (approximately)
DELETE FROM `directions`;
/*!40000 ALTER TABLE `directions` DISABLE KEYS */;
/*!40000 ALTER TABLE `directions` ENABLE KEYS */;

-- Dumping structure for table rpd_db.disciplines
CREATE TABLE IF NOT EXISTS `disciplines` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `discipline_index` varchar(10) NOT NULL,
  `name` varchar(100) NOT NULL,
  `departament_id` int(11) NOT NULL,
  `developer_rp_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc5u8bqm1m2na2em6i6e9nww14` (`departament_id`),
  KEY `FKl44gx2u2y9a96acvdridb3alx` (`developer_rp_id`),
  CONSTRAINT `FKc5u8bqm1m2na2em6i6e9nww14` FOREIGN KEY (`departament_id`) REFERENCES `departaments` (`id`),
  CONSTRAINT `FKl44gx2u2y9a96acvdridb3alx` FOREIGN KEY (`developer_rp_id`) REFERENCES `teachers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.disciplines: ~0 rows (approximately)
DELETE FROM `disciplines`;
/*!40000 ALTER TABLE `disciplines` DISABLE KEYS */;
/*!40000 ALTER TABLE `disciplines` ENABLE KEYS */;

-- Dumping structure for table rpd_db.disciplines_educational_programs
CREATE TABLE IF NOT EXISTS `disciplines_educational_programs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `basic_educational_program_id` int(11) NOT NULL,
  `discipline_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfhcouyye5c7aca8gnrrel0dnc` (`basic_educational_program_id`),
  KEY `FKafs07f6107vehp5nc1s6xw6ca` (`discipline_id`),
  CONSTRAINT `FKafs07f6107vehp5nc1s6xw6ca` FOREIGN KEY (`discipline_id`) REFERENCES `disciplines` (`id`),
  CONSTRAINT `FKfhcouyye5c7aca8gnrrel0dnc` FOREIGN KEY (`basic_educational_program_id`) REFERENCES `basic_educational_programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.disciplines_educational_programs: ~0 rows (approximately)
DELETE FROM `disciplines_educational_programs`;
/*!40000 ALTER TABLE `disciplines_educational_programs` DISABLE KEYS */;
/*!40000 ALTER TABLE `disciplines_educational_programs` ENABLE KEYS */;

-- Dumping structure for table rpd_db.education_types
CREATE TABLE IF NOT EXISTS `education_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `learning_period` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `text` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
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
  `name_type_one` varchar(105) NOT NULL,
  `name_type_two` varchar(105) NOT NULL,
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
  `position_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `position_name` (`position_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.employee_positions: ~0 rows (approximately)
DELETE FROM `employee_positions`;
/*!40000 ALTER TABLE `employee_positions` DISABLE KEYS */;
/*!40000 ALTER TABLE `employee_positions` ENABLE KEYS */;

-- Dumping structure for table rpd_db.files_rpd
CREATE TABLE IF NOT EXISTS `files_rpd` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
  KEY `FK52r7yqr2bm8i86c67o6lqw1sf` (`discipline_educational_program_id`),
  CONSTRAINT `FK52r7yqr2bm8i86c67o6lqw1sf` FOREIGN KEY (`discipline_educational_program_id`) REFERENCES `disciplines_educational_programs` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.files_rpd: ~0 rows (approximately)
DELETE FROM `files_rpd`;
/*!40000 ALTER TABLE `files_rpd` DISABLE KEYS */;
/*!40000 ALTER TABLE `files_rpd` ENABLE KEYS */;

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
  `departament_id` int(11) NOT NULL,
  `employee_id` int(11) NOT NULL,
  `employee_positions_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK24jsgsgfxo6einlvd2jugfq6v` (`departament_id`),
  KEY `FKcr8lwab4nll2jdn7y2t9r6hu3` (`employee_id`),
  KEY `FKpvpy63rn40xovdex23mwf7tu6` (`employee_positions_id`),
  CONSTRAINT `FK24jsgsgfxo6einlvd2jugfq6v` FOREIGN KEY (`departament_id`) REFERENCES `departaments` (`id`),
  CONSTRAINT `FKcr8lwab4nll2jdn7y2t9r6hu3` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`),
  CONSTRAINT `FKpvpy63rn40xovdex23mwf7tu6` FOREIGN KEY (`employee_positions_id`) REFERENCES `employee_positions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.teachers: ~0 rows (approximately)
DELETE FROM `teachers`;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;

-- Dumping structure for table rpd_db.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disabled` bit(1) NOT NULL,
  `role_id` int(11) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  KEY `FKp56c1712k691lhsyewcssf40f` (`role_id`),
  CONSTRAINT `FKp56c1712k691lhsyewcssf40f` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table rpd_db.users: ~2 rows (approximately)
DELETE FROM `users`;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `disabled`, `role_id`, `password`, `username`) VALUES
	(1, b'0', 1, '$2a$10$X7P45PLNGAjXTlxAjY.kjeOnpEeaJXcGaarNFCqsGW9GGUgwcSm.O', 'Администратор'),
	(2, b'0', 2, '$2a$10$j3MvKjp0siFjLyk5CTdBQO3LnIHfwScm6bY7Dv3RAxZ7NDt8E.DXa', 'Пользователь');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
