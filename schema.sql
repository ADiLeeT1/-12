-- University database: 4 related tables
CREATE DATABASE IF NOT EXISTS university_db;
USE university_db;

DROP TABLE IF EXISTS grades;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS teachers;

CREATE TABLE teachers (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE students (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    student_group VARCHAR(20)
);

CREATE TABLE subjects (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(100) NOT NULL,
    teacher_id INT NOT NULL,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

CREATE TABLE grades (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    score      INT NOT NULL CHECK (score BETWEEN 0 AND 100),
    FOREIGN KEY (student_id) REFERENCES students(id),
    FOREIGN KEY (subject_id) REFERENCES subjects(id)
);

INSERT INTO teachers (name) VALUES
    ('Aigul Bekova'),
    ('Marat Omarov');

INSERT INTO students (name, student_group) VALUES
    ('Aliya Nurova', 'A-101'),
    ('Daniyar Sadykov', 'A-101'),
    ('Madina Kenzhebayeva', 'A-102');

INSERT INTO subjects (title, teacher_id) VALUES
    ('Java', 1),
    ('Databases', 2),
    ('Digital Marketing', 1);

INSERT INTO grades (student_id, subject_id, score) VALUES
    (1, 1, 85),
    (1, 2, 90),
    (2, 1, 70),
    (3, 3, 95);
