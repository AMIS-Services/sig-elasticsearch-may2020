# Elasticsearch in a Spring app

In this hands-on we are going to connect our Spring application with the Elastic Search cluster using Spring Data Elasticsearch and retrieve some information from the recipes index. In the Spring Data Elasticsearch you can use the ElasticsearchCrudRepository, which will work in the same way as a JPA repository.

All start code and code examples are located in the elasticsearch-nodejs folder. The code you should have after every step is located in the folder with the name of that step. To do each step separately (without having done the previous steps) you can therefore use the code from the previous step as the starting point. If you start with a project in one of the step folders, you have to run “npm install” first in order to install the node_modules used by that project. If you are doing all the steps consecutively you can start with the “basic-app” project and keep expanding from there.

For this part of the hands-on we are assuming that you are running an elasticsearch cluster with docker as described [elasticsearch_docker.md](elasticsearch_docker.md). We are using the same recipes index that is loaded in this manual.

## Step 1 – creating the connection with ES

1. Open the Springboot application with your favorite IDE.
2. Add the annotation @EnableElasticsearchRepositories to the application. This enables the elastic search repositories.
3. Open the Recipe class and add the annotation @Document(indexName = "recipes", type = "_doc") to this class.
3. The springboot application is configured to connect to a ES cluster on localhost on port 9200. Run the application to check if everything works.
4. Test the GET method of recipe controller. The url is http://localhost:8080/recipes/{id}.

## Step 2 - Using the ElasticsearchCrudRepository
1. Open the RecipesRepository. You will see an empty interface which extends ElasticsearchCrudRepository. If you are known to Spring JPA repository, then you will see similarities in how you work with the elastic search repository. The Elastic Search Repository also contains basic crud functions like delete, save, findbyid, findall, etc..
2. Open the RecipesController class.
3. Uncomment the insertRecipe method which could add a new document to recipes index. Make use of the already built in functions of the RecipesRepository. Try your newly created method using postman. A new recipe should look the same as when you retrieve a document from ES, except without the id.
4. Uncomment the deleteRecipe function and let this function delete a recipe from the index.

## Step 3 - Custom queries
1. Open the RecipesRepository
2. Uncomment the findByName query and add the annotation @Query("{\"match\": {\"name\": \"?0\"} }"). As you see, you only have to specify the part within the query JSON element. Also uncomment the findRecipeByName function in the RecipesController. Test this new method using a GET request on http://localhost:9200/recipes/findbyname?name=shrimps
3. Implement a second custom query and create a new method in the controller which calls your new function in the RecipesRepository.
4. Open the RecipesRepositoryCustom. Replace the findByName method with the following piece of code:
```
  public List<Recipe> findByName(String name) {
    SearchQuery searchQuery = new NativeSearchQueryBuilder()
      .withQuery(QueryBuilders.matchQuery("name", name))
      .build();

    return esTemplate.queryForList(searchQuery, Recipe.class);
  }
```
This will do the exact query as the findByName query in the RecipesRepository. However, in the custom implementation using the elasticsearchRestTemplate you can have more flexibility in creating the ES query. Uncomment the findRecipeByNameCustom method in the Controller and test this endpoint.
5. Try to implement one or more aggregations which you created in the handson [queries and aggregations part](queries_aggregations.md) in the RecipesRepository or in the RecipesRepositoryCustom.

