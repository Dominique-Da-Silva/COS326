INSERT INTO DegreeProgram (degree_code, degree_name, number_of_years, faculty)
VALUES
  ('BSc', 'Bachelor of Science', 3, 'EBIT'),
  ('BIT', 'Bachelor of IT', 4, 'EBIT'),
  ('PhD', 'Philosophiae Doctor', 5, 'EBIT');

--SELECT * FROM DegreeProgram;

INSERT INTO Course (course_code, course_name, department, credits)
VALUES
  ('COS301', 'Software Engineering', 'Computer Science', 40),
  ('COS326', 'Database Systems', 'Computer Science', 20),
  ('MTH301', 'Discrete Mathematics', 'Mathematics', 15),
  ('PHL301', 'Logical Reasoning', 'Philosophy', 15);

--SELECT * FROM Course;

INSERT INTO Undergraduate (student_number, full_name, date_of_birth, degree_code, year_of_study, courseRegistration)
VALUES
  ('140010', ('Ms', 'Good', 'Student'), '1996-01-10', 'BSc', 3, ARRAY['COS301', 'COS326', 'MTH301']),
  ('140015', ('Mr', 'Serious', 'Student'), '1995-05-25', 'BSc', 3, ARRAY['COS301', 'PHL301', 'MTH301']),
  ('131120', ('Mr', 'Funny', 'Student'), '1995-01-30', 'BIT', 3, ARRAY['COS301', 'COS326', 'PHL301']),
  ('131140', ('Ms', 'Fransie', 'Cobalt'), '1996-02-20', 'BIT', 4, ARRAY['COS301', 'COS326', 'MTH301', 'PHL301']);

--SELECT * FROM Undergraduate;

INSERT INTO Postgraduate (student_number, full_name, date_of_birth, degree_code, year_of_study, category, supervisor)
VALUES
  ('101122', ('Ms', 'Over', 'Achiever'), '1987-06-15', 'PhD', 2, 'full time', ('Prof.', 'Very', 'Serious')),
  ('121101', ('Mr', 'Burnt', 'Out'), '1985-04-27', 'PhD', 3, 'part time', ('Prof.', 'Not', 'Helpful'));

--SELECT * FROM Postgraduate;