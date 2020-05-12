# Indexing

In this hands-on we are going to create an index, insert some data and do some manipulation on the data using the REST API.

For this part of the hands-on we are assuming that you are running an elasticsearch cluster with docker as described [elasticsearch_docker.md](elasticsearch_docker.md).


## Cluster info
Getting information about the cluster is done using GET commands, therefore all commands can be easily done in the browser (Postman, curl, etc..)
1. Check the current status of the recipes index using http://localhost:9200/_cat/shards?v
2. Stop one of the two docker containers on which the recipes index is running, make sure it is NOT es01 you stop, but es02 or es03, since es01 is the node which exposes port 9200 which we use to access our ES cluster. To stop a node, open a terminal and type 'docker container stop es02' or 'docker container stop es03' (without quotes)
3. Check the current status of the recipes index again using the command in step 1. You should see one of the recipes shards is UNASSIGNED. Eventually the ES cluster will create a new shard on the other still running node. You can also check the health of the index using http://localhost:9200/_cat/indices?v, which should be yellow during the creationg of the new shard.
4. Start the stopped docker container again using, 'docker container start es02' or 'docker container start es03'
5. Check the status of the shards and indices again. Nothing should have changed and no recipes shard should exist on the node you started in the previous step.


## Indexing
This part of this hands-on is not using the recipes index.
1. Create your own simple JSON document with a string field and number field and POST this document to an non-existing index. (for ease of use, use a self defined id when you create the document)
2. Retrieve your previously created document.
3. Retrieve your previously created document, but this time with a selected field. Also try to retrieve the document while excluding a field.
4. Update a field in your created document. Also add a new field to the document. Check your result by retrieving the document.
5. Delete your document and check if it worked by checking the existance of the document.
6. Using a bulk operation, insert multiple documents into your index. (hint: use Content-type: application/x-ndjson in your request)
7. Create an new index which has 5 shard, 1 replica and add your own custom index mapping.
8. Check how the ES cluster configures your newly created index over the cluster.
9. Get the automatically created index mapping from step 1 and compare it with your custom index mapping from step 7 (use _mapping on both indices)

