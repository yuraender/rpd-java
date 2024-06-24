CREATE DATABASE rpd_db;

SHOW DATABASES LIKE 'rpd_db';
use rpd_db;
-- таблица ролей
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       disabled BOOLEAN NOT NULL DEFAULT 0
);

INSERT INTO roles (name, disabled) VALUES ('administrator', 0);
-- таблица пользователей
CREATE TABLE app_users (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           role_id INT,
                           username VARCHAR(100) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           disabled BOOLEAN NOT NULL DEFAULT 0,
                           FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO app_users (role_id, username, password, disabled)
VALUES (1, 'Администратор', '$2a$10$1TxF82jKJwpmHEPAKcY1..rxo8VXd51PE.qCKlJU35KhlmJOTJCpi', 0);

-- таблица сотрудников
CREATE TABLE employees (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           last_name VARCHAR(100),
                           first_name VARCHAR(100),
                           middle_name VARCHAR(100),
                           name_type_one VARCHAR(100),
                           name_type_two VARCHAR(100),
                           disabled BOOLEAN DEFAULT FALSE
);

ALTER TABLE employees DROP COLUMN password;
ALTER TABLE employees DROP COLUMN username;
-- таблица должности
CREATE TABLE employee_positions (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    position_name VARCHAR(255) NOT NULL,
                                    disabled BOOLEAN DEFAULT FALSE
);

-- таблица институтов
CREATE TABLE institutes (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            director_id INT,
                            city VARCHAR(100),
                            approval_text TEXT,
                            footer_text TEXT,
                            disabled BOOLEAN DEFAULT FALSE,
                            FOREIGN KEY (director_id) REFERENCES employees(id)
);

-- таблица кафедр
CREATE TABLE departaments (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              code VARCHAR(200) NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              abbreviation VARCHAR(50),
                              institute_id INT,
                              manager_id INT,
                              disabled BOOLEAN DEFAULT FALSE,
                              FOREIGN KEY (institute_id) REFERENCES institutes(id),
                              FOREIGN KEY (manager_id) REFERENCES employees(id)
);

-- Удалить таблицу, если она существует
DROP TABLE IF EXISTS teachers;

-- Создать таблицу преподавателей
CREATE TABLE teachers (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          employee_id INT,
                          departament_id INT,
                          employee_positions_id INT,
                          disabled BOOLEAN DEFAULT FALSE,
                          FOREIGN KEY (employee_id) REFERENCES employees(id),
                          FOREIGN KEY (departament_id) REFERENCES departaments(id),
                          FOREIGN KEY (employee_positions_id) REFERENCES employee_positions(id)
);

DESCRIBE teachers;

-- Создание таблицы направление
CREATE TABLE directions (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            encryption VARCHAR(20) NOT NULL,
                            name VARCHAR(255) NOT NULL,
                            departament_id INT,
                            disabled BOOLEAN DEFAULT FALSE,
                            FOREIGN KEY (departament_id) REFERENCES departaments(id)
);

-- Создание таблицы профиль
CREATE TABLE profiles (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          direction_id INT,
                          disabled BOOLEAN DEFAULT FALSE,
                          FOREIGN KEY (direction_id) REFERENCES directions(id)
);

-- Создание таблицы аудитория
CREATE TABLE audiences (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           institute_id INT,
                           number_audience VARCHAR(50) NOT NULL,
                           tech TEXT,
                           software_license TEXT,
                           disabled BOOLEAN DEFAULT FALSE,
                           FOREIGN KEY (institute_id) REFERENCES institutes(id)
);

-- Создание таблицы дисциплины
CREATE TABLE disciplines (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             index_discipline VARCHAR(50) NOT NULL,
                             name VARCHAR(255) NOT NULL,
                             developer_rp_id INT,
                             departament_id INT,
                             disabled BOOLEAN DEFAULT FALSE,
                             FOREIGN KEY (developer_rp_id) REFERENCES teachers(id),
                             FOREIGN KEY (departament_id) REFERENCES departaments(id)
);

-- Создание таблицы тех обеспечение
CREATE TABLE tech_supports (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               discipline_id INT,
                               audiences_id INT,
                               disabled BOOLEAN DEFAULT FALSE,
                               FOREIGN KEY (discipline_id) REFERENCES disciplines(id),
                               FOREIGN KEY (audiences_id) REFERENCES audiences(id)
);

-- Создание таблицы типы обучения
CREATE TABLE education_types (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 learning_period INT,
                                 text TEXT,
                                 disabled BOOLEAN DEFAULT FALSE
);

-- Создание таблицы основная обучающая программа (ООП)
CREATE TABLE basic_educational_programs (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            profile_id INT,
                                            academic_year INT NOT NULL,
                                            education_type_id INT,
                                            disabled BOOLEAN DEFAULT FALSE,
                                            FOREIGN KEY (profile_id) REFERENCES profiles(id),
                                            FOREIGN KEY (education_type_id) REFERENCES education_types(id)
);

-- Создание таблицы ДисциплиныОП
CREATE TABLE disciplines_educational_programs (
                                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                                  discipline_id INT,
                                                  basic_educational_program_id INT,
                                                  disabled BOOLEAN DEFAULT FALSE,
                                                  FOREIGN KEY (discipline_id) REFERENCES disciplines(id),
                                                  FOREIGN KEY (basic_educational_program_id) REFERENCES basic_educational_programs(id)
);

-- Создание таблицы Компетенции
CREATE TABLE competencies (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              code VARCHAR(100) NOT NULL,
                              essence TEXT NOT NULL,
                              know TEXT,
                              be_able TEXT,
                              own TEXT,
                              disabled BOOLEAN DEFAULT FALSE
);

-- Создание таблицы Компетенции Дисциплин ОП
CREATE TABLE competencies_disciplines_educational_programs (
                                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                                               discipline_educational_program_id INT,
                                                               competence_id INT,
                                                               disabled BOOLEAN DEFAULT FALSE,
                                                               FOREIGN KEY (discipline_educational_program_id) REFERENCES disciplines_educational_programs(id),
                                                               FOREIGN KEY (competence_id) REFERENCES competencies(id)
);

-- Создание таблицы ФайлыРПД
CREATE TABLE files_rpd (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           discipline_educational_program_id INT,
                           section_1 BLOB,
                           section_1_isLoad BOOLEAN,
                           section_2 BLOB,
                           section_2_isLoad BOOLEAN,
                           section_3 BLOB,
                           section_3_isLoad BOOLEAN,
                           section_4 BLOB,
                           section_4_isLoad BOOLEAN,
                           section_5 BLOB,
                           section_5_isLoad BOOLEAN,
                           section_6 BLOB,
                           section_6_isLoad BOOLEAN,
                           disabled BOOLEAN DEFAULT FALSE,
                           FOREIGN KEY (discipline_educational_program_id) REFERENCES disciplines_educational_programs(id)
);

-- создание тестовых полей в таблицах

-- Вставка записи в таблицу employees
INSERT INTO employees (last_name, first_name, middle_name, name_type_one, name_type_two)
VALUES ('Петров', 'Иван', 'Степанович', 'ПетровИС', 'ИСПетров');
-- Вставка записи в таблицу employees (1)
INSERT INTO employees (last_name, first_name, middle_name, name_type_one, name_type_two)
VALUES ('Иванов', 'Алексей', 'Петрович', 'ИвановАП', 'АПИванов');
-- Вставка записи в таблицу employees (2)
INSERT INTO employees (last_name, first_name, middle_name, name_type_one, name_type_two)
VALUES ('Смирнова', 'Ольга', 'Сергеевна', 'СмирноваОС', 'ОССмирнова');

-- Вставка записи в таблицу institutes
INSERT INTO institutes (name, director_id, city, approval_text, footer_text)
VALUES ('ИМХИТ', 1, 'Егорьевск', 'тестовый текст утвержд', 'подвальный текст');
INSERT INTO institutes (name, director_id, city, approval_text, footer_text)
VALUES ('МИССиС', 1, 'Москва', 'тестовый текст утвержд2', 'подвальный текст2');
INSERT INTO institutes (name, director_id, city, approval_text, footer_text)
VALUES ('МИССиС', 2, 'Новосибирск', 'тестовый текст утвержд3', 'подвальный текст3');
INSERT INTO institutes (name, director_id, city, approval_text, footer_text)
VALUES ('МАИ', 2, 'Дмитров', 'тестовый текст утверждтестовый текст утверждтестовый текст утверждтестовый текст утвержд', 'подвальный текст');
INSERT INTO institutes (name, director_id, city, approval_text, footer_text)
VALUES ('ГАИ', 1, 'Подольск', 'тестовый текст утвержд2тестовый текст утверждтестовый текст утверждтестовый текст утверждтестовый текст утверждтестовый текст утвержд', 'подвальный текст2');
INSERT INTO institutes (name, director_id, city, approval_text, footer_text)
VALUES ('МФЮА', 2, 'Санкт-Петербург', 'тестовый текст тестовый текст утверждтестовый текст утверждтестовый текст утверждтестовый текст утверждутвержд3', 'подвальный текст3');


alter table institutes modify column id bigint not null auto_increment;
ALTER TABLE departaments DROP FOREIGN KEY departaments_ibfk_1;
ALTER TABLE audiences DROP FOREIGN KEY audiences_ibfk_1;
ALTER TABLE app_users DROP FOREIGN KEY app_users_ibfk_1;



INSERT INTO departaments (code, name, abbreviation, institute_id, manager_id) VALUES
                                                                                  ('CS101', 'Кафедра компьютерных наук', 'CS', 1, 1),
                                                                                  ('ME201', 'Кафедра машиностроения', 'ME', 2, 2),
                                                                                  ('EE301', 'Кафедра электротехники', 'EE', 3, 3),
                                                                                  ('BA401', 'Кафедра бизнес-анализа', 'BA', 1, 4),
                                                                                  ('MT501', 'Кафедра материаловедения', 'MT', 2, 3),
                                                                                  ('CH601', 'Кафедра химии', 'CH', 3, 2);


INSERT INTO directions (encryption, name, departament_id) VALUES
                                                              ('CS101', 'направление компьютерных наук', 1),
                                                              ('D234', 'направление машиностроения', 2),
                                                              ('EE3R', 'направление электротехники', 3);

INSERT INTO profiles (name, direction_id) VALUES
                                              ('Профиль 1', 1),
                                              ('Профиль 2', 2),
                                              ('Профиль 3', 3),
                                              ('Профиль 4', 1),
                                              ('Профиль 5', 2),
                                              ('Профиль 6', 3);

INSERT INTO education_types (name, learning_period, text) VALUES
                                                              ('Очный тип', 4, 'Тестовый текст'),
                                                              ('Заочный тип', 5, 'Тестовый текст2');

INSERT INTO employee_positions (position_name) VALUES
                                                   ('Преподаватель'),
                                                   ('Директор'),
                                                   ('Заведующий кафедрой');

INSERT INTO teachers (employee_id, departament_id, employee_positions_id) VALUES
                                                                              (1, 1, 1),
                                                                              (2, 2, 2),
                                                                              (3, 3, 3);

ALTER TABLE disciplines DROP FOREIGN KEY disciplines_ibfk_1;

INSERT INTO disciplines (index_discipline, name, developer_rp_id, departament_id) VALUES
                                                                                      ('1234', 'технологии машинного обучения', 1, 1),
                                                                                      ('3454', 'менеджмент', 2, 2),
                                                                                      ('5467', 'экономика', 3, 3);

select * from teachers;
select * from employees;
select * from departaments;

INSERT INTO disciplines (index_discipline, name, developer_rp_id, departament_id) VALUES
                                                                                      ('2344', 'технологии машинного обучения 2', 1, 2),
                                                                                      ('4324', 'технологии машинного обучения 3', 1, 2);

INSERT INTO competencies (code, essence, know, be_able, own) VALUES
                                                                 ('1234', 'какой-то текст1', 'знать что-то1', 'уметь что-то1', 'владеть 1'),
                                                                 ('3454', 'какой-то текст1', 'знать что-то2', 'уметь что-то2', 'владеть 2'),
                                                                 ('5467', 'какой-то текст1', 'знать что-то3', 'уметь что-то3', 'владеть 3');

INSERT INTO audiences (institute_id, number_audience, tech, software_license) VALUES
                                                                                  ('1', '34', 'современные компьютеры1', 'текст лицензии'),
                                                                                  ('2', '223', 'современные компьютеры2', 'текст лицензии2'),
                                                                                  ('3', '19', 'современные компьютеры3', 'текст лицензии3');

INSERT INTO tech_supports (discipline_id, audiences_id) VALUES
                                                            (1, 1),
                                                            (2, 2),
                                                            (3, 3);

INSERT INTO tech_supports (discipline_id, audiences_id) VALUES
                                                            (2, 1),
                                                            (3, 1);

INSERT INTO tech_supports (profile_id, academic_year) VALUES
                                                          (1, 1),
                                                          (2, 2),
                                                          (3, 3);

INSERT INTO basic_educational_programs (profile_id, academic_year, education_type_id) VALUES
                                                                                          (1, 2023, 1),
                                                                                          (3, 2023, 1),
                                                                                          (2, 2024, 2);

INSERT INTO disciplines_educational_programs (discipline_id, basic_educational_program_id) VALUES
                                                                                               (1, 4),
                                                                                               (2, 5),
                                                                                               (3, 6);

INSERT INTO competencies_disciplines_educational_programs (discipline_educational_program_id, competence_id) VALUES
                                                                                                                 (13, 1),
                                                                                                                 (14, 2),
                                                                                                                 (15, 3);

INSERT INTO files_rpd (discipline_educational_program_id, section_1, section_1_isLoad, section_2, section_2_isLoad, section_3, section_3_isLoad, section_4, section_4_isLoad, section_5, section_5_isLoad, section_6, section_6_isLoad) VALUES
                                                                                                                                                                                                                                            (13, null, 0, null, 0, null, 0, null, 0, null, 0, null, 0),
                                                                                                                                                                                                                                            (14, null, 0, null, 0, null, 0, null, 0, null, 0, null, 0),
                                                                                                                                                                                                                                            (15, null, 0, null, 0, null, 0, null, 0, null, 0, null, 0);


select * from tech_supports;
select * from institutes;
select * from departaments;
-- techSupport.getDiscipline().getDepartment()


select * from competencies;

