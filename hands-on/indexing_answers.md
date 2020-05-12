# Indexing

In this hands-on we are going to create an index, insert some data and do some manipulation on the data using the REST API.

For this part of the hands-on we are assuming that you are running an elasticsearch cluster with docker as described [elasticsearch_docker.md](elasticsearch_docker.md).

## Indexing
1. Create your own simple JSON document and POST this document to an non-existing index. (for ease of use, use a self defined id when you create the document)

POST http://localhost:9200/beers/_doc/42
```
{
  "name": "Hertog Jan",
  "description": "it is a beer",
  "type": "beer",
  "alcoholpercentage": 5.1
}
```
2. Retrieve your previously created document.

GET http://localhost:9200/beers/_doc/42

3. Retrieve your previously created document, but this time with a selected field. Also try to retrieve the document while excluding a field.

GET http://localhost:9200/beers/_doc/42?_source=name,alcoholpercentage

GET http://localhost:9200/beers/_doc/42?_source_excludes=alcoholpercentage

4. Update a field in your created document. Also add a new field to the document. Check your result by retrieving the document.

POST http://localhost:9200/beers/_update/42
```
{
  "doc" : {
    "description": "You can drink this",
    "country": "Netherlands"
  }
}
```

5. Delete your document and check if it worked by checking the existance of the document.

DELETE http://localhost:9200/beers/_doc/42

HEAD http://localhost:9200/beers/_doc/42 (check HTTP status code)

6. Using a bulk operation, insert multiple documents into your index.

POST http://localhost:9200/beers/_bulk
```
{ "index" : { "_index" : "beers", "_id" : "1" } },
{"name": "Grolsch", "description": "it is a beer", "type": "beer", "alcoholpercentage": 5.0 },
{ "index" : { "_index" : "beers", "_id" : "2" } },
{"name": "Jupiler", "description": "it is a beer", "type": "beer", "alcoholpercentage": 5.2 },
{ "index" : { "_index" : "beers", "_id" : "3" } },
{"name": "Leffe Blond", "type": "blond", "alcoholpercentage": 6.6 }
```

7. Create an new index which has 5 shard, 1 replica and add your own custom index mapping.

PUT http://localhost:9200/wines
```
{
  "settings" : {
    "number_of_shards" : 5,
    "number_of_replicas" : 1
  },
  "mappings": {
    "properties": {
      "name":   { "type": "text"  },
      "alcoholpercentage": { "type": "float"  }
    }
  }
}
```

8. Check how the ES cluster configures your newly created index over the cluster.

GET http://localhost:9200/_cat/shards?v


9. Compare your index mapping with the auto created index from step 1 (use _mapping on your index)

GET http://localhost:9200/beers/_mappings
GET http://localhost:9200/wines/_mappings
