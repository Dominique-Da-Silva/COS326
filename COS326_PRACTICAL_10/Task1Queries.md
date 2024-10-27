# Task 1: Importing Data into a Neo4j Database
## ---------------------------------------------------------
## Create a new Neo4j database:
## ---------------------------------------------------------
```
CREATE DATABASE Prac10Neo4jB;
```

## ---------------------------------------------------------
## Prepare the import folder:
## ---------------------------------------------------------
```
Navigate to the database folder Prac10Neo4jB.graphdb created by Neo4j.
Inside this folder, create a new sub-folder named import.
Download the actors.csv and movies.csv files from ClickUP and copy them into the import folder.
```

## ---------------------------------------------------------
## Import the data into the Neo4j database:
## ---------------------------------------------------------

```
MATCH (n) 
DETACH DELETE n;
```

### 4. To import data from actors.csv, use the following Cypher query
```
LOAD CSV WITH HEADERS FROM 'file:///actors.csv' AS row
  MERGE (a:Actor {id: toInteger(row.id), name: row.name, birth_year: toInteger(row.birth_year)})
  RETURN
    a.id AS id,
    a.name AS name,
    a.birth_year AS birth_year
```

### 5. To view the contents of the database after importing actors
```
MATCH (n) RETURN n LIMIT 25;
```

### 6. To create relationships between actors and movies using movies.csv
```
LOAD CSV WITH HEADERS FROM 'file:///movies.csv' AS row
  MATCH (a:Actor {id: toInteger(row.actor_id)})
  MERGE (m:Movie {id: toInteger(row.movie_id), title: row.movie_title})
  MERGE (a)-[r:ACTED_IN]->(m)
  ON CREATE SET r.role = row.role
RETURN
  a.id AS actor_id,
  m.id AS movie_id,
  m.title AS movie_title,
  r.role AS role;
```

### 7. To view the database contents again
```
MATCH (n) RETURN n LIMIT 25;
```
