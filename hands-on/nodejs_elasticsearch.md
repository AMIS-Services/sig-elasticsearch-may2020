# Elasticsearch in a Node.js app

In this part of the hands-on we are going to establish a connection to elasticsearch from a Node.js application. In the Node.js application the Koa framework (https://koajs.com/) is used for easily making an API.

All start code and code examples are located in the elasticsearch-nodejs folder. The code you should have after every step is located in the folder with the name of that step. To do each step separately (without having done the previous steps) you can therefore use the code from the previous step as the starting point. If you start with a project in one of the step folders, you have to run “npm install” first in order to install the node_modules used by that project. If you are doing all the steps consecutively you can start with the “basic-app” project and keep expanding from there.

For this part of the hands-on we are assuming that you are running an elasticsearch cluster with docker as described [elasticsearch_docker.md](elasticsearch_docker.md). We are using the same recipes index that is loaded in this manual.

## Step 1 – first connection to elasticsearch

For this step, start with the “basic-app”.

1. Open a terminal, for example powershell or command-line, in the basic-app folder and run `npm install`.
2. Run `npm start` to start the app and listen for code changes. You should see the following text in the terminal: “Server running on port 3100”. If port 3100 is already in use (error in terminal: “Error: listen EADDRINUSE: address already in use :::3100”), you can change the port in src/server.js on line 13: “app.listen(3100);”.

To test if the app is running correctly you can do a GET request (with postman) on http://localhost:3100/.

3. Now that we made sure the app is running, we need to stop it in order to install the official elasticsearch module for Node.js. (You can stop the app by doing ctrl + c in the terminal where it’s running). Run `npm install elasticsearch` and start the app again.
4. To use the elasticsearch module in a file we need to add the following code, add this to server.js:

```javascript
const elasticsearch = require("elasticsearch");
```

5. Add the following code in order to instantiate an elasticsearch client that communicates with the elasticsearch cluster:

```javascript
const esClient = new elasticsearch.Client({
  host: "http://localhost:9200",
  log: "info",
});
```

The port number should be the one where elasticsearch is available. If you followed the explanation in [elasticsearch_docker.md](elasticsearch_docker.md) this should be 9200.

6. Replace the router.get method by the following:

```javascript
router.get("/health", async (ctx, next) => {
  ctx.body = await esClient.cluster.health();
});
```

Now the “health” endpoint returns information about the health of the cluster.

7. In postman, execute a GET request on http://localhost:3100/health in order to check if this endpoint works and the connection with elasticsearch has succeeded.

## Step 2 – adjust app structure

If we put all API endpoints in the server.js file this file can become very cluttered. This is why we are going to put the endpoints in designated files. If you are not interested in this, you can skip this step and proceed with step 3 (using the project in the step2 folder).

1. Create a new folder called “elasticsearch” in the src folder, with in it a file called “connection.js” and place the code for creating an elasticsearch client in this file (and remove it from server.js). Export the client from this file by adding the following code at the end of the file:

```javascript
module.exports = { esClient };
```

2. Create a new folder in the src map called “routes”. Create 3 new files: “index.js”, “info.js”, and “recipes.js”. In info.js all endpoints concerning information about the elasticsearch cluster are placed, like the previously created “/health” endpoint. In recipes.js all endpoints with an action on the recipes index, like search queries, adding recipes, deleting recipes etc will be placed. In index.js all routes will be added together.

3. Put the following code in both info.js and recipes.js:

```javascript
const Router = require("@koa/router");

const router = new Router();

module.exports = router;
```

This creates new instances of the router in order to create the respective endpoints in these files.

4. Now we will bring these two route files together in index.js, both with their own url:

```javascript
const Router = require("@koa/router");
const infoRouter = require("./info");
const recipeRouter = require("./recipes");

const router = new Router();

router.use("/info", infoRouter.routes(), infoRouter.allowedMethods());
router.use("/recipes", recipeRouter.routes(), recipeRouter.allowedMethods());

module.exports = router;
```

Now all endpoints in info.js can be reached on http://localhost:3100/info/(..) and all endpoints in recipes.js file on http://localhost:3100/recipes/(..).

5. Move the router.get function for the “health” endpoint from server.js to info.js and use the esClient that was exported from connection.js:

```javascript
const { esClient } = require("../elasticsearch/connection");
```

6. In server.js,remove the code that creates the router, we will import this from routes/index.js by adding the following code on the top of the server.js file:

```javascript
const router = require("./routes");
```

The check if the restructuring was succesfull you can execute a GET request on http://localhost:3100/info/health.

## Step 3 – add, update, and delete documents

1. Stop the application, run `npm install uuid koa-bodyparser` and restart the application. We will us uuid the generate a random id for new recipes and koa-bodyparser is needed to be able to use the request body in our POST and PUT requests.

2. In server.js add the koa-bodyparser by placing the following code on the top of the file:

```javascript
const bodyParser = require("koa-bodyparser");
```

And above the statement that contains app.listen, add the following code:

```javascript
app.use(bodyParser());
```

3. In order to use uuid, add the following to the top of the recipes.js file:

```javascript
const { v4: uuidv4 } = require("uuid");
```

We can now generate a new uuid by calling `uuidv4()`.

4. Now we are going to write a POST function on the “/” endpoint in recipes.js. This will be used to add a new recipe to the recipes index. The endpoint for this request will be http://localhost:3100/recipes because of the subdivision of routes in index.js. We can use the create function of the elasticsearch client for this (https://www.elastic.co/guide/en/elasticsearch/client/javascript-api/current/api-reference.html), to which we will give the index (recipes), the new id and the body that will be send in the post request (ctx.request.body) as parameters. The body in the post request will contains the new recipe as JSON. You can try this yourself or check the code on the next point to get started.

5.

```javascript
router.post("/", async (ctx, next) => {
  ctx.body = await esClient.create({
    index: "recipes",
    id: uuidv4(),
    body: ctx.request.body,
  });
});
```

Check if this is working as you expect by performing a POST request in postman, with as a body your favourite recipe as JSON with some properties like name, description, ingredients etc.

6. Also create a PUT and DELETE function for updating and deleting recipes. You can use the update and delete method of the elasticsearch client (as described here: https://www.elastic.co/guide/en/elasticsearch/client/javascript-api/current/api-reference.html). The endpoint for both request should be ‘/:id’. This id can be extracted from the request with “ctx.params.id”.

## Stap 4 – queries en aggregaties

Add a couple of the queries and aggregations we performed in the [queries and aggregations part](queries_aggregations.md) of the hands-on to the code.

## Bonus

Try out a few of the other functions of the elasticsearch client (see: https://www.elastic.co/guide/en/elasticsearch/client/javascript-api/current/api-reference.html). For example, you can try to create a new index, add a mapping, remove an index, or try some functions that give information on the cluster and the nodes.
