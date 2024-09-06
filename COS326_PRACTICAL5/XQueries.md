# TASK 1 XPATH EXPRESSIONS
## Question 1 
List the names of all the musicians in the database.
```
//musicians/musician/name/text()
```

## Question 2 
For a specific musician, list the album names and their years.
```
//musicians/musician[name= 'Billy Talent']/albums/album
```

## Question 3 
List the names of all the musicians who have release an album titled "Lateralus".
```
//musicians/musician[albums/album='Lateralus']/name/text()
```


## Question 4 
List all the musicians who have the word "Pop" anywhere in their genre.
```
//musicians/musician[contains(genre, 'Pop')]/name/text()
```

## Question 5
List all the solo musicians.
```
//musicians/musician[@type="solo"]/name/text()
```

## Question 6 
Show the name and genre of the second solo musician.
```
//musicians/musician[@type="solo"][2]/name/text() | //musicians/musician[@type="solo"][2]/genre/text()
```

## Question 7 
Display the fourth album by the musician "Ben Folds".
```
//musicians/musician[name='Ben Folds']/albums/album[4]/text()
```

## Question 8
List the names of albums that were released in 2008 o later.
```
//musicians/musician/albums/album[@year >= 2008]/text()
```



# TASK 2 FLWOR EXPRESSIONS


## Question 9 
Write a FLWOR query to determine the rate at which a specific musician produces albums.
List all the musician names and their corresponding rates, ordering the results from the most frequent to the least frequent. 
For example, assume that a specific artist has released three albums in 2001, 2002 and 2004. To calculate the rate, divide the difference in years between the first and last album by the number of albums. 
The calculation for this example is then: (2004-2001+1)/3 = 1.3 years. 
Make sure that each artist is displayed on a new line.
```
for $musician in //musician
let $albums := $musician/albums/album
let $years := $albums/@year
let $rate := (max($years) - min($years) + 1) div count($albums)
order by $rate ascending
return concat($musician/name, ", rate: ", 
    if ($rate = xs:integer($rate)) 
    then $rate 
    else format-number($rate, '#.00'))
```

## Question 10 
List all musicians who are in a band and have released at least 2 albums (prints in descending order from most albums), along with the names of the members and how many members are in the band.
This query shows all bands (i.e., musicians of type "band") who have released more than one album as well as the albums that have been released, and it also lists the members of the band.
```
for $musician in //musician[@type="band"]
let $albums := $musician/albums/album
let $members := $musician/members/member
where count($albums) >= 2
order by count($albums) descending
return
  concat(
    "Band name: ", $musician/name, "&#10;",
    
    "Members (", count($members), "):&#10;", 
    string-join(
      for $member in $members
      return concat("  ", $member/first_name, " ", $member/last_name), 
      "&#10;"
    ), "&#10;",
    
    "Album Count: ", count($albums), "&#10;", 
    
    "Albums:&#10;", 
    string-join(
      for $album in $albums
      return concat("  ", $album), 
      "&#10;"
    ), "&#10;",
    "==================", "&#10;"
  )
```
