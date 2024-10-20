# TASK 2: AGGREGATION AND PATH QUERIES

### 1. Path Queries:
#### a. Find incidents that are 1 or 2 links away from APT29:
```
MATCH (t:ThreatActor {name: 'APT29'})-[*1..2]-(i:Incident)
RETURN DISTINCT i;	
```

#### b. Show the nodes in the shortest path from APT29 to the strategy 'User Awareness Training':
```
MATCH p=shortestPath((t:ThreatActor {name: 'APT29'})-[*]-(s:Strategy {name: 'User Awareness Training'}))
RETURN p;	
```
 
#### c. Report whether each incident has an associated mitigation strategy:
```
MATCH (i:Incident)
OPTIONAL MATCH (i)-[:MITIGATED_BY]->(s:Strategy)
RETURN i.type AS IncidentType, 
       CASE 
         WHEN s IS NULL THEN 'False' 
         ELSE 'True' 
       END AS HasStrategy;	
```

#### d. For all paths of length 2, list the node names and path length:
```
MATCH p=(n)-[r1]->(m)-[r2]->(o)
RETURN [n.name, m.name, o.name] AS NodeNames, length(p) AS PathLength;	
```


### 2. Aggregation Queries:
#### a. Count the number of nodes in the graph:
```
MATCH (n)
RETURN COUNT(n) AS TotalNodes;	
```

#### b. Count the number of incidents launched by each threat actor:
```
MATCH (t:ThreatActor)-[:LAUNCHED]->(i:Incident)
RETURN t.name, COUNT(i) AS IncidentsLaunched;	
```

#### c. Count the number of mitigation strategies applied to incidents:
```
MATCH (i:Incident)-[:MITIGATED_BY]->(s:Strategy)
RETURN COUNT(s) AS TotalStrategiesApplied;	
```

#### d. Identify the threat actor responsible for the most incidents:
```
MATCH (t:ThreatActor)-[:LAUNCHED]->(i:Incident)
RETURN t.name, COUNT(i) AS IncidentCount
ORDER BY IncidentCount DESC
LIMIT 1;	
```
