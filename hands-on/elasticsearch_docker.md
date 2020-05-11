# Running elasticsearch from docker

## Needed installations on computer:

- Docker/ docker for windows
- Npm
- Node.js

## First Time setup:

- Start docker/docker for windows

- For windows: Make sure docker engine has at least 4GB of memory, otherwise you cannot create a cluster with 3 nodes (and you get a weird error, see: https://github.com/elastic/elasticsearch/issues/51196). Click to docker icon in the toolbar > go to Settings > Resources > Advanced > set memory to 4.00GB > Apply & Restart.

![screenshot of docker for windows memory adjustment](images/adjust-memory-docker.png)

- Run `docker-compose up` from a command line tool in the folder that contains the docker-compose.yml file. This creates an elasticsearch cluster with 3 nodes. The volumes are bound so data will be preserved. This may take a few minutes and you will so a lot of logging in the console.

> “Compose preserves all volumes used by your services. When docker-compose up runs, if it finds any containers from previous runs, it copies the volumes from the old container to the new container. This process ensures that any data you’ve created in volumes isn’t lost.” - https://docs.docker.com/compose/

This does a whole bunch of logging in the console, when finished should look something like this:

![screenshot of the output when running docker-compose up](images/docker-compose-up-output.png)

Leave this window open and open a new window for next steps.

- Check if elasticsearch is running correctly in postman by doing a GET request on http://localhost:9200/_nodes. This will return a JSON result that should include the following:

```json
"_nodes": {
        "total": 3,
        "successful": 3,
        "failed": 0
    },
```

### load recipes data

- Run `npm install elasticsearch` in the folder where the load_recipes.js script is located (elasticsearch-docker folder). (Script adapted from: https://www.sitepoint.com/building-recipe-search-site-angular-elasticsearch/).

- Unzip the recipeitems.zip and place the unzipped recipeitems.json in the same folder as load-recipes.js.

- Run the script to bulk load recipes by running `node load_recipes.js` in the command line. This script counts down the records until all are inserted and should show “Inserted all records!” when finished. The command line window where the elasticsearch docker is running should show something similar to this:

![screenshot of elasticsearch output after inserting recipes](images/elasticsearch-output-after-insert.png)

- To check if data is loaded correctly, do a GET request on: http://localhost:9200/_stats. The result should show that there are 2 shards. Under “indices” it should show only the "resipes" index:

```json
"indices": {
        "recipes": {
            "uuid": "vwUxcJusRKiIRX5C7I2szg",
            "primaries": {
                "docs": {
                    "count": 172699,
                    "deleted": 0
                },
(...)
```

The elasticsearch cluster is now up and the recipes data is loaded!

## Ways of stopping the elasticsearch cluster

- Run `docker-compose stop` in the folder containing the docker-compose.yml file.
- Press ctrl+c in the window that is still running the docker container.
- Click on the stop icon in docker dashboard (if using docker for windows).

## Removing the cluster

Run `docker-compose down`. This does not remove the data. If you also want to remove all the data that was saved in elasticsearch, run: `docker-compose down -v`. This removes all the volumes specified in the yml file. You can check which docker containers (running and stopped) are on your computer by running `docker ps` (`docker container ls` to list only the ones that are running). To check which volumes are still on your computer run `docker volume ls`.

## Starting elasticsearch cluster after first setup:

Run `docker-compose up`. If you didn’t remove the volumes the data should still be there. If you did remove the volumes the data needs to be loaded again using the node load_recipes.js script. Any changes to the data you made yourself will be gone.
