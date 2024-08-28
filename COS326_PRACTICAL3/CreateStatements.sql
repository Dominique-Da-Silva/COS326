CREATE DOMAIN student_number AS CHAR(6) CHECK (VALUE ~ '^[0-9]{6}$');

CREATE TYPE name_type AS (
  title TEXT, 
  first_name TEXT, 
  surname TEXT
);

CREATE TYPE title_enum AS ENUM (
  'Ms', 'Mev', 'Miss', 'Mrs', 'Mr', 'Mnr', 'Dr', 'Prof'
);

CREATE TYPE category_enum AS ENUM ('part time', 'full time');

CREATE SEQUENCE student_seq START 1;
CREATE SEQUENCE degree_program_seq START 1;
CREATE SEQUENCE course_seq START 1;


CREATE TABLE Student (
  student_key INT PRIMARY KEY DEFAULT nextval('student_seq'),
  student_number student_number,
  full_name name_type,
  date_of_birth DATE,
  degree_code TEXT,
  year_of_study INT
);

CREATE TABLE Undergraduate (
  courseRegistration TEXT[]
) INHERITS (Student);

CREATE TABLE Postgraduate (
  category category_enum,
  supervisor name_type
) INHERITS (Student);

CREATE TABLE DegreeProgram (
  degree_key INT PRIMARY KEY DEFAULT nextval('degree_program_seq'),
  degree_code TEXT,
  degree_name TEXT,
  number_of_years INT,
  faculty TEXT
);

CREATE TABLE Course (
  course_key INT PRIMARY KEY DEFAULT nextval('course_seq'),
  course_code TEXT,
  course_name TEXT,
  department TEXT,
  credits INT
);

CREATE FUNCTION personFullNames(name name_type) 
RETURNS TEXT AS $$
SELECT CONCAT(name.title, ' ', name.first_name, ' ', name.surname);
$$ LANGUAGE SQL;

CREATE FUNCTION ageInYears(birth_date DATE) 
RETURNS INT AS $$
SELECT EXTRACT(YEAR FROM age(birth_date))::INT;
$$ LANGUAGE SQL;

CREATE FUNCTION isRegisteredFor(courses TEXT[], course_code TEXT) 
RETURNS BOOLEAN AS $$
SELECT course_code = ANY(courses);
$$ LANGUAGE SQL;

CREATE FUNCTION isFinalYearStudent(student_year INT, degree_years INT) 
RETURNS BOOLEAN AS $$
SELECT student_year = degree_years;
$$ LANGUAGE SQL;

CREATE FUNCTION isFullTime(category category_enum) 
RETURNS BOOLEAN AS $$
SELECT category = 'full time';
$$ LANGUAGE SQL;

CREATE FUNCTION isPartTime(category category_enum) 
RETURNS BOOLEAN AS $$
SELECT category = 'part time';
$$ LANGUAGE SQL;