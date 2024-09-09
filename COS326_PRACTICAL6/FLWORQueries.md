# PRACTICAL 6
## Question 1 
List the names of students enrolled in the "Database Systems" course.
```
for $s in doc("students.xml")//student
where $s/enrollments/course/@code = 'COS326'
return $s/name
```

## Question 2 
Count the total number of students enrolled in "Discrete Structures" course.
```
let $courses := doc("courses.xml")//course
let $students := doc("students.xml")//student
let $discreteStructuresCode := $courses[name="Discrete Structures"]/code/text()
let $enrolledStudents := for $student in $students
                         let $enrollments := $student/enrollments/course/@code
                         where $discreteStructuresCode = $enrollments
                         return $student
return <course>
         <name>Discrete Structures</name>
         <total_count>{count($enrolledStudents)}</total_count>
       </course>
```

## Question 3 
List all courses offered by the "Mathematics" department.
```
for $c in doc("courses.xml")//course
where $c/department = 'Mathematics'
return $c/name
```


## Question 4 
Find the names of instructors teaching courses taken by "Marijke Jooste"
```
let $student := doc("students.xml")//student[name = 'Marijke Jooste']
for $course_code in $student/enrollments/course/@code
let $instructor := doc("courses.xml")//course[code = $course_code]/instructor
return <instructor>{distinct-values($instructor)}</instructor>
```

## Question 5
List the course codes of all courses that have exactly 18 credits.
```
for $c in doc("courses.xml")//course
where $c/credits = 18
return $c/code
```

## Question 6 
List the names of students who are enrolled in both "Introduction to Computer Science" and "Mathematics 124" courses.
```
let $courses := doc("courses.xml")//course
let $students := doc("students.xml")//student
let $cos151Code := $courses[name="Introduction to Computer Science"]/code/text()
let $wtw124Code := $courses[name="Mathematics 124"]/code/text()
let $studentsWithBothCourses := 
    for $student in $students
    let $enrolledCourses := $student/enrollments/course/@code
    where ($cos151Code = $enrolledCourses) and ($wtw124Code = $enrolledCourses)
    return $student/name/text()
return  for $name in $studentsWithBothCourses
        return <name>{$name}</name>
```

## Question 7 
List the names of students who are not enrolled in any course offered by the "Mathematics" department.
```
let $courses := doc("courses.xml")//course
let $students := doc("students.xml")//student
let $mathCourses := $courses[department="Mathematics"]/code/text()
let $studentsNotInMathCourses := 
    for $student in $students
    let $enrolledCourses := $student/enrollments/course/@code
    where not($mathCourses = $enrolledCourses)
    return $student/name/text()
return for $name in $studentsNotInMathCourses
       return <name>{$name}</name>
```

## Question 8
List the names of courses that have more than 3 students enrolled.
```
for $c in doc("courses.xml")//course
let $students_enrolled := count(doc("students.xml")//student/enrollments/course[@code = $c/code])
where $students_enrolled > 3
return $c/name
```

## Question 9 
Find the names of instructors whose courses are being taken by more than two students.
```
let $courses := doc("courses.xml")//course
let $students := doc("students.xml")//student
let $courseEnrollments := 
    for $course in $courses
    let $courseCode := $course/code/text()
    let $enrollmentCount := count($students[enrollments/course/@code = $courseCode])
    where $enrollmentCount > 2
    return <course>
              <code>{$courseCode}</code>
              <instructor>{$course/instructor/text()}</instructor>
           </course>

let $instructorsWithManyStudents :=
    for $course in distinct-values($courseEnrollments/instructor)
    return $course

return for $instructor in $instructorsWithManyStudents
            return <instructor>{$instructor}</instructor>
```

## Question 10 
List the names of students studying first year courses offered by the Department of Computer Science in descending order.
```
let $courses := doc("courses.xml")//course
let $students := doc("students.xml")//student

let $firstYearCsCourses := $courses[
    department="Computer Science" and
    starts-with(code, "COS1")
]/code

let $studentsInFirstYearCsCourses :=
    for $student in $students
    let $enrolledCourses := $student/enrollments/course/@code
    where some $course in $enrolledCourses satisfies $course = $firstYearCsCourses
    return $student/name/text()

let $sortedStudents :=
    for $name in distinct-values($studentsInFirstYearCsCourses)
    order by $name descending
    return $name

return 
            for $name in $sortedStudents
            return <name>{$name}</name>
```
