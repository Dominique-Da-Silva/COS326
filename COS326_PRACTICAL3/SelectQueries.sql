-- 1.student personal details
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber,
    personFullNames(full_name) AS personfullnames,
    ageInYears(date_of_birth) AS ageinyears
FROM Student;

-- 2.undergraduate students registration details
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber, 
    personFullNames(full_name) AS personfullnames,
    degree_code AS studentdegreecode, 
    year_of_study AS studentyearofstudy,
    courseRegistration AS courseregistration
FROM Undergraduate;

-- 3.postgraduate students registration details
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber,
    personFullNames(full_name) AS personfullnames,
    degree_code AS studentdegreecode, 
    year_of_study AS studentyearofstudy,
	category AS postgraduatecategory,
	personFullNames(supervisor) AS personfullnames
FROM Postgraduate;

-- 4.undergraduate students registration details for final years
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber, 
    personFullNames(full_name) AS personfullnames,
    degree_code AS studentdegreecode, 
    year_of_study AS studentyearofstudy,
    courseRegistration AS courseregistration
FROM Undergraduate
WHERE isFinalYearStudent(year_of_study,(SELECT number_of_years FROM DegreeProgram WHERE degree_code = Undergraduate.degree_code LIMIT 1));

-- 5.undergraduate students registration details for students registered
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber, 
    personFullNames(full_name) AS personfullnames,
    degree_code AS studentdegreecode, 
    year_of_study AS studentyearofstudy,
    courseRegistration AS courseregistration
FROM Undergraduate
WHERE isRegisteredFor(courseRegistration, 'COS326');

-- 6.full-time postgraduate students registration details
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber,
    personFullNames(full_name) AS personfullnames,
    degree_code AS studentdegreecode, 
    year_of_study AS studentyearofstudy,
	category AS postgraduatecategory,
	personFullNames(supervisor) AS personfullnames
FROM Postgraduate
WHERE isFullTime(category);

-- 7.part-time postgraduate students registration details
SELECT 
    student_key AS student_id, 
    student_number AS studentnumber,
    personFullNames(full_name) AS personfullnames,
    degree_code AS studentdegreecode, 
    year_of_study AS studentyearofstudy,
	category AS postgraduatecategory,
	personFullNames(supervisor) AS personfullnames
FROM Postgraduate
WHERE isPartTime(category);
