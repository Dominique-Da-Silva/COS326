-- PRACTICAL 3 DOMAINS
CREATE DOMAIN student_number AS CHAR(6) CHECK (VALUE ~ '^[0-9]{6}$');

-- PRACTICAL 3 TYPES
CREATE TYPE name_type AS (
  title TEXT, 
  first_name TEXT, 
  surname TEXT
);

CREATE TYPE title_enum AS ENUM (
  'Ms', 'Mev', 'Miss', 'Mrs', 'Mr', 'Mnr', 'Dr', 'Prof'
);

CREATE TYPE category_enum AS ENUM ('part time', 'full time');

-- PRACTICAL 3 SEQUENCES
CREATE SEQUENCE student_seq START 1; --surrogate keys for student, undergrads and postgrads tables
CREATE SEQUENCE degree_program_seq START 1; --surrogate keys for degree program table
CREATE SEQUENCE course_seq START 1; --surrogate keys for course table


-- PRACTICAL 3 TABLES
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

-- #####################################
-- PRACTICAL 4 TABLES
CREATE TABLE DeletedUndergrad (
    student_key INT,
    student_number student_number,
    full_name name_type,
    date_of_birth DATE,
    degree_code TEXT,
    year_of_study INT,
    courseRegistration TEXT[],
    deletion_date TIMESTAMP,
    deleted_by TEXT
);

CREATE TABLE DeletedPostgrad (
    student_key INT,
    student_number student_number,
    full_name name_type,
    date_of_birth DATE,
    degree_code TEXT,
    year_of_study INT,
    category category_enum,
    supervisor name_type,
    deletion_date TIMESTAMP,
    deleted_by TEXT
);


-- PRACTICAL 3 FUNCTIONS
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


-- #####################################
-- PRACTICAL 4 FUNCTIONS
CREATE OR REPLACE FUNCTION personFullNames(name name_type) 
RETURNS TEXT AS $$
BEGIN
    RETURN name.title || ' ' || name.first_name || ' ' || name.surname;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION ageInYears(birth_date DATE) 
RETURNS INT AS $$
BEGIN
    RETURN EXTRACT(YEAR FROM age(birth_date))::INT;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isRegisteredFor(courses TEXT[], course_code TEXT) 
RETURNS BOOLEAN AS $$
BEGIN
    RETURN course_code = ANY(courses);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isFinalYearStudent(student_year INT, degree_years INT) 
RETURNS BOOLEAN AS $$
BEGIN
    RETURN student_year = degree_years;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isFullTime(category category_enum) 
RETURNS BOOLEAN AS $$
BEGIN
    RETURN category = 'full time';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isPartTime(category category_enum) 
RETURNS BOOLEAN AS $$
BEGIN
    RETURN category = 'part time';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isValidCourseCode(code TEXT)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (SELECT 1 FROM Course WHERE course_code = code);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hasValidCourseCodes(courses TEXT[])
RETURNS BOOLEAN AS $$
DECLARE
    course TEXT;
BEGIN
    FOREACH course IN ARRAY courses
    LOOP
        IF NOT isValidCourseCode(course) THEN
            RETURN FALSE;
        END IF;
    END LOOP;
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION courseCodeFrequency(courses TEXT[], code TEXT)
RETURNS INT AS $$
DECLARE
    count INT := 0;
    course TEXT;
BEGIN
    FOREACH course IN ARRAY courses
    LOOP
        IF course = code THEN
            count := count + 1;
        END IF;
    END LOOP;
    RETURN count;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION hasDuplicateCourseCodes(courses TEXT[])
RETURNS BOOLEAN AS $$
DECLARE
    i INT;
    j INT;
BEGIN
    FOR i IN 1..array_length(courses, 1)
    LOOP
        FOR j IN i+1..array_length(courses, 1)
        LOOP
            IF courses[i] = courses[j] THEN
                RETURN TRUE;
            END IF;
        END LOOP;
    END LOOP;
    RETURN FALSE;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isValidDegreeCode(code TEXT)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (SELECT 1 FROM DegreeProgram WHERE degree_code = code);
END;
$$ LANGUAGE plpgsql;


-- #####################################
-- PRACTICAL 4 TRIGGER PROCEDURES
CREATE OR REPLACE FUNCTION check_valid_degree_code()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT isValidDegreeCode(NEW.degree_code) THEN
        RAISE EXCEPTION 'Invalid degree code: %', NEW.degree_code;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_valid_course_codes()
RETURNS TRIGGER AS $$
BEGIN
    IF NOT hasValidCourseCodes(NEW.courseRegistration) THEN
        RAISE EXCEPTION 'Invalid course code in courseRegistration';
    END IF;
    IF hasDuplicateCourseCodes(NEW.courseRegistration) THEN
        RAISE EXCEPTION 'Duplicate course codes in courseRegistration';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION record_delete_undergrad()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO DeletedUndergrad
    VALUES (OLD.*, current_timestamp, current_user);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION record_delete_postgrad()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO DeletedPostgrad
    VALUES (OLD.*, current_timestamp, current_user);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;


-- #####################################
-- PRACTICAL 4 TRIGGERS
CREATE TRIGGER check_valid_degree_student
BEFORE INSERT OR UPDATE ON Student
FOR EACH ROW EXECUTE FUNCTION check_valid_degree_code();

CREATE TRIGGER check_valid_degree_undergraduate
BEFORE INSERT OR UPDATE ON Undergraduate
FOR EACH ROW EXECUTE FUNCTION check_valid_degree_code();

CREATE TRIGGER check_valid_degree_postgraduate
BEFORE INSERT OR UPDATE ON Postgraduate
FOR EACH ROW EXECUTE FUNCTION check_valid_degree_code();

CREATE TRIGGER check_valid_course_registration
BEFORE INSERT OR UPDATE ON Undergraduate
FOR EACH ROW EXECUTE FUNCTION check_valid_course_codes();

CREATE TRIGGER audit_delete_undergrad
BEFORE DELETE ON Undergraduate
FOR EACH ROW EXECUTE FUNCTION record_delete_undergrad();

CREATE TRIGGER audit_delete_postgrad
BEFORE DELETE ON Postgraduate
FOR EACH ROW EXECUTE FUNCTION record_delete_postgrad();