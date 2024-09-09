# PRACTICAL 6
## Question 1 
List the names of students enrolled in the "Database Systems" course.
```
for $s in //student
where $s/enrollments/course/@code = 'COS326'
return $s/name
```

## Question 2 
Count the total number of students enrolled in "Discrete Structures" course.
```
let $course_name := 'Discrete Structures'
let $total_count := count(
  for $s in //student
  where $s/enrollments/course/@name = $course_name
  return $s
)
return <course>
          <name>{$course_name}</name>
          <total_count>{$total_count}</total_count>
        </course>
```

## Question 3 
List all courses offered by the "Mathematics" department.
```
for $c in //course
where $c/department = 'Mathematics'
return $c/name
```


## Question 4 
Find the names of instructors teaching courses taken by "Marijke Jooste"
```

```

## Question 5
List the course codes of all courses that have exactly 18 credits.
```

```

## Question 6 
List the names of students who are enrolled in both "Introduction to Computer Science" and "Mathematics 124" courses.
```

```

## Question 7 
List the names of students who are not enrolled in any course offered by the "Mathematics" department.
```

```

## Question 8
List the names of courses that have more than 3 students enrolled.
```

```

## Question 9 
Find the names of instructors whose courses are being taken by more than two students.
```

```

## Question 10 
List the names of students studying first year courses offered by the Department of Computer Science in descending order.
```

```
