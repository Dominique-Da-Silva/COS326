-- Test invalid degree code insertion
INSERT INTO Undergraduate (student_number, full_name, date_of_birth, degree_code, year_of_study, courseRegistration)
VALUES ('150001', ('Mr', 'Test', 'Student'), '1998-01-01', 'InvalidDegree', 1, ARRAY['COS301', 'COS326']);

-- Test invalid course code insertion
INSERT INTO Undergraduate (student_number, full_name, date_of_birth, degree_code, year_of_study, courseRegistration)
VALUES ('150002', ('Ms', 'Another', 'Student'), '1999-02-02', 'BSc', 2, ARRAY['COS301', 'InvalidCourse']);

-- Test invalid degree code update
UPDATE Undergraduate SET degree_code = 'InvalidDegree' WHERE student_number = '140010';

-- Test invalid course code update
UPDATE Undergraduate SET courseRegistration = ARRAY['COS301', 'InvalidCourse'] WHERE student_number = '140010';

-- Test invalid degree code for postgraduate
INSERT INTO Postgraduate (student_number, full_name, date_of_birth, degree_code, year_of_study, category, supervisor)
VALUES ('150003', ('Mr', 'Post', 'Grad'), '1990-03-03', 'InvalidDegree', 1, 'full time', ('Dr', 'Super', 'Visor'));

-- Test delete undergraduate (should trigger audit)
DELETE FROM Undergraduate WHERE student_number = '140015';

-- Test delete postgraduate (should trigger audit)
DELETE FROM Postgraduate WHERE student_number = '101122';

-- Verify audited deletions
SELECT * FROM DeletedUndergrad;
SELECT * FROM DeletedPostgrad;