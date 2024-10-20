To Reset:
```
MATCH (n) 
DETACH DELETE n;
```

To View:
```
MATCH (n) RETURN n;
```

# TASK 1: CREATE AND QUERY A GRAPH DATABASE

## 1. Creating the Neo4j Database: Create a new database named ThreatIntel.graphdb in Neo4j.

## 2. Cypher Queries for Graph Creation:
### a
#### i. Create ThreatActor, Incident, and Strategy nodes with LAUNCHED relationships:
```
CREATE (:ThreatActor {name: 'APT29', origin: 'Russia', motivation: 'Espionage'});
CREATE (:ThreatActor {name: 'Lazarus Group', origin: 'North Korea', motivation: 'Financial Gain'});

CREATE (:Incident {name: 'Phishing Campaign', type: 'Phishing Campaign', severity: 'High', date: '2023-09-01'});
CREATE (:Incident {name: 'Ransomware Attack', type: 'Ransomware Attack', severity: 'Critical', date: '2023-07-15'});

CREATE (:Strategy {name: 'User Awareness Training', type: 'Preventive', effectiveness: 85});
CREATE (:Strategy {name: 'Network Segmentation', type: 'Containment', effectiveness: 90});

MATCH (t:ThreatActor {name: 'APT29'}), (i:Incident {type: 'Phishing Campaign'})
CREATE (t)-[:LAUNCHED]->(i);

MATCH (t:ThreatActor {name: 'Lazarus Group'}), (i:Incident {type: 'Ransomware Attack'})
CREATE (t)-[:LAUNCHED]->(i);	
```

#### ii. Show the current nodes and relationships:
```
MATCH (n) RETURN n;
```

#### iii. Create MITIGATED_BY relationships between incidents and strategies:
```
MATCH (i:Incident {type: 'Phishing Campaign'}), (s:Strategy {name: 'User Awareness Training'})
CREATE (i)-[:MITIGATED_BY]->(s);
MATCH (i:Incident {type: 'Ransomware Attack'}), (s:Strategy {name: 'Network Segmentation'})
CREATE (i)-[:MITIGATED_BY]->(s);	
```

#### iv. Show the updated contents of the graph:
```
MATCH (n) RETURN n;	
```

### b
#### i. List all unique node labels:
```
CALL db.labels();	
```

#### ii. List all threat actors, sorted by name:
```
MATCH (t:ThreatActor)
RETURN t.name
ORDER BY t.name ASC;	
```

#### iii. List all incidents by severity in descending order:
```
MATCH (i:Incident)
RETURN i.type, i.severity
ORDER BY i.severity DESC;	
```

#### iv. List the relationship types present in the graph:
```
CALL db.relationshipTypes();	
```

#### v. List all threat actors and the incidents they launched:
```
MATCH (t:ThreatActor)-[:LAUNCHED]->(i:Incident)
RETURN t.name, i.type;	
```
 
#### vi. List all incidents and their corresponding mitigation strategies:
```
MATCH (i:Incident)-[:MITIGATED_BY]->(s:Strategy)
RETURN i.type, s.name;	
```

#### vii. Find the most effective strategy for mitigating incidents:
```
MATCH (s:Strategy)
RETURN s.name, s.effectiveness
ORDER BY s.effectiveness DESC
LIMIT 1;	
```
