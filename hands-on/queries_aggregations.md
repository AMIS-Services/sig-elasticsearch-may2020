# Queries and aggregations hands-on

In this part of the hands-on we are going to write some queries aggregations and run them on an elasticsearch cluster using postman. Each part consists of a few questions or assignments for which you can find the answer by writing a query or aggregation. The answers can be found in [queries_aggregations_answers.md](queries_aggregations_answers.md).

For this part of the hands-on we are assuming that you are running an elasticsearch cluster with docker as described [elasticsearch_docker.md](elasticsearch_docker.md). We are using the same recipes index that is loaded in this manual.

To see the mapping of the recipes index perform the following GET request: http://localhost:9200/recipes/_mapping.

## Queries

### URL-based queries

Try to find the answers or complete the assignment by writing an url-based query. All questions and assignments are about the recipes index. Don’t forget to use url encoding (+ is %2B, space is %20, “ is %22).

1. Get the 15 newest recipes.
2. Get recipes with “mint” OR “syrup” in any of the fields and return results 100 to 105.
3. Get recipes with words starting with “choco” in the name AND “light” in the description.
4. Compare the total amount of hits when searching recipes for “jalepeno” in the ingredients normally, while including a fuzziness of 1, and while including a fuzziness of 2.
5. Get recipes that have “2 eggs” (so not “2 eggs, beaten” etc) listed as an ingredient and return only the name and the ingredients fields.

### JSON queries

Try to find the answers or complete the assignment by writing a JSON-based query. All questions and assignments are about the recipes index.

1. Get recipes 20 to 25 of the newest recipes and return only the name and datePublished fields. If recipes are published on the same date they must be further sorted by name.
2. Get recipes with “mint” OR “syrup” in any of the fields.
3. Get all recipes published between 1 January and 10 January 2015.
4. Search for “jalepeno” in the ingredients and include results with 2 character changes for jalepeno (so it will include “jalepeño”, “jalapeno” etc).
5. Find all recipes that have “apple pie” in the name, but also include names such as “apple mini pie”, “apple crumble pie”, “apple and cinnamon pie”, where there are at most 2 words between “apple” and “pie”.
6. Find all recipes that have “pizza” and a word starting with “f” in the name.
7. Return only recipes that are published in December 2013 that have “ground beef” as one of the ingredients. Make results that contain “easy” or “classic” in the description or serve 6 people more relevant (higher in the results list).
8. Return only recipes that have “taco” in the name and make recipes that have “beef” as an ingredient less relevant than other recipes.

## Aggregations

Try to find the answers or complete the assignment by writing an aggregation. All questions and assignments are about the recipes index.

1. How many recipes are not from one of the top 10 sources (hint: look at the “sum_other_doc_count” in the results of the aggregation)?
2. Which 10 ingredients are used more often in combination with “sugar” than in other recipes?
3. How many different values are present for the field preptime?
4. How many percent of the recipes have 6 or less ingredients (hint: use a script to get the length of the ingredients array for each document)?
5. Calculate the average number of ingredients for recipes with a short, medium and long prepTime. For short prepTime include values: PT5M PT10M PT15M, for medium prepTime include values: PT20M PT25M PT30M PT35M PT40M, and for long prepTime include values: PT45M PT50M PT55M PT1H.
6. In which year where most cocktail (“cocktail” in name field) recipes published?
