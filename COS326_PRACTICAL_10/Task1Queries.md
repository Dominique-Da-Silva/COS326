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

### 4. To import data from actors.csv, use the following Cypher query
```
LOADR CSV WITH HEADERS FOM 'file:///actors.csv' AS row
CREATE (:Actor {id: row.id, name: row.name});
```

### 5. To view the contents of the database after importing actors
```
MATCH (n) RETURN n LIMIT 25;
```

### 6. To create relationships between actors and movies using movies.csv
```
LOAD CSV WITH HEADERS FROM 'file:///movies.csv' AS row
MATCH (a:Actor {id: row.actor_id})
CREATE (m:Movie {title: row.title})
CREATE (a)-[:ACTED_IN]->(m);
```

### 7. To view the database contents again
```
MATCH (n) RETURN n LIMIT 25;
```
