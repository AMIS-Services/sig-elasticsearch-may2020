# Queries and aggregations hands-on answers

For this part of the hands-on we are assuming that you are running an elasticsearch cluster with docker as described [elasticsearch_docker.md](elasticsearch_docker.md). We are using the same recipes index that is loaded in this manual.

## Queries

### URL-based queries

The answers are all GET requests, so this will not be specified with each answer.

1. Get the 15 newest recipes.

http://localhost:9200/recipes/_search?size=15&sort=datePublished:desc

2. Get recipes with “mint” OR “syrup” in any of the fields and return results 100 to 105.

http://localhost:9200/recipes/_search?q=mint%20syrup&from=100&size=5

3. Get recipes with words starting with “choco” in the name AND “light” in the description.

http://localhost:9200/recipes/_search?q=%2Bname:choco*%20%2Bdescription:light or http://localhost:9200/recipes/_search?q=name:choco*%20AND%20description:light

4. Compare the total amount of hits when searching recipes for “jalepeno” in the ingredients normally, while including a fuzziness of 1, and while including a fuzziness of 2.

Query normal: http://localhost:9200/recipes/_search?q=ingredients:jalepeno.
Query with fuzziness: http://localhost:9200/recipes/_search?q=ingredients:jalepeno~3 (last number represents the fuzziness).
You will find that in 14 recipes jalapeño is spelled as “jalepeno”, when you add a fuzziness of 1 tot this query the results will include recipes with the spelling “jalapeno” or “jalepeño” and this will result in 1709 recipes. With a fuzziness of 2 you will also include the recipes with the spelling “jalepeño” (and other spellings that differ two alterations from “jalepeno”) and you will get 2451 results.

5. Get recipes that have “2 eggs” (so not “2 eggs, beaten” etc) listed as an ingredient and return only the name and the ingredients fields.

http://localhost:9200/recipes/_search?_source=name,ingredients&q=ingredients.keyword:%222%20eggs%22
(Without url encoding: ?q=ingredients.keyword:”2 eggs”.
Use the keyword field and quotation marks to ensure the whole ingredient needs to match.

### JSON queries

All queries are POST request on http://localhost:9200/recipes/_search so in the answers only the POST body will be shown.

1. Get recipes 20 to 25 of the newest recipes and return only the name and datePublished fields. If recipes are published on the same date they must be further sorted by name.

```json
{
  "size": 5,
  "from": 20,
  "sort": [{ "datePublished": "desc" }, "name.keyword"],
  "_source": ["name", "datePublished"]
}
```

The keyword field has to be used for name, since this is a text field. In the sort you can also include “asc” for name, but since this is the default it is not needed.

2. Get recipes with “mint” OR “syrup” in any of the fields.

```json
{
  "query": {
    "multi_match": {
      "query": "mint syrup"
    }
  }
}
```

or

```json
{
  "query": {
    "query_string": {
      "query": "mint syrup"
    }
  }
}
```

3. Get all recipes published between 1 January and 10 January 2015.

```json
{
  "query": {
    "range": {
      "datePublished": { "gt": "2015-01-01", "lt": "2015-01-10" }
    }
  }
}
```

4. Search for “jalepeno” in the ingredients and include results with 2 character changes for jalepeno (so it will include “jalepeño”, “jalapeno” etc).

```json
{
  "query": {
    "fuzzy": {
      "ingredients": {
        "value": "jalepeno",
        "fuzziness": 2
      }
    }
  }
}
```

The fuzziness parameter can also be left out of this query, since 2 is the default.

or

```json
{
  "query": {
    "query_string": {
      "query": "ingredients:jalepeno~2"
    }
  }
}
```

5. Find all recipes that have “apple pie” in the name, but also include names such as “apple mini pie”, “apple crumble pie”, “apple and cinnamon pie”, where there are at most 2 words between “apple” and “pie”.

```json
{
  "query": {
    "match_phrase": {
      "name": {
        "query": "apple pie",
        "slop": 2
      }
    }
  }
}
```

6. Find all recipes that have “pizza” and a word starting with “f” in the name.

```json
{
  "query": {
    "match_bool_prefix": {
      "name": "pizza f"
    }
  }
}
```

7. Return only recipes that are published in December 2013 that have “ground beef” as one of the ingredients. Make results that contain “easy” or “classic” in the description or serve 6 people more relevant (higher in the results list).

```json
{
  "query": {
    "bool": {
      "must": {
        "match_phrase": {
          "ingredients": "ground beef"
        }
      },
      "should": [
        { "match": { "description": "easy classic" } },
        { "match": { "recipeYield": "6" } }
      ],
      "filter": {
        "range": {
          "datePublished": { "gte": "2013-12-01", "lte": "2013-12-31" }
        }
      }
    }
  }
}
```

8. Return only recipes that have “taco” in the name and make recipes that have “beef” as an ingredient less relevant than other recipes.

```json
{
  "query": {
    "boosting": {
      "positive": {
        "match": {
          "name": "taco"
        }
      },
      "negative": {
        "match": {
          "ingredients": "beef"
        }
      },
      "negative_boost": 0.5
    }
  }
}
```

## Aggregations

All queries are POST request on http://localhost:9200/recipes/_search so in the answers only the POST body will be shown.

1. How many recipes are not from one of the top 10 sources (hint: look at the “sum_other_doc_count” in the results of the aggregation)?

Answer: 9145, aggregation:

```json
{
  "aggregations": {
    "top_source": {
      "terms": {
        "field": "source.keyword"
      }
    }
  },
  "size": 0
}
```

2. Which 10 ingredients are used more often in combination with “sugar” than in other recipes?

Answer: "1 teaspoon vanilla extract", "1 cup white sugar", "1/2 cup white sugar", "2 eggs", "1 teaspoon baking soda", "1/2 teaspoon salt", "2 cups all-purpose flour", "1 egg", "1/4 teaspoon salt", "1 teaspoon ground cinnamon", query:

```json
{
  "query": {
    "match": {
      "ingredients": "sugar"
    }
  },
  "aggregations": {
    "top_ingredients": {
      "significant_terms": {
        "field": "ingredients.keyword"
      }
    }
  },
  "size": 0
}
```

3. How many different values are present for the field preptime?

Answer: 181, query:

```json
{
  "aggregations": {
    "unique_prepTime": {
      "cardinality": {
        "field": "prepTime.keyword"
      }
    }
  },
  "size": 0
}
```

4. How many percent of the recipes have 6 or less ingredients (hint: use a script to get the length of the ingredients array for each document)?

Answer: 31.54912020498733, query:

```json
{
  "aggregations": {
    "number_of_ingredients_percentiles": {
      "percentile_ranks": {
        "script": "doc['ingredients.keyword'].length",
        "values": [6]
      }
    }
  },
  "size": 0
}
```

5. Calculate the average number of ingredients for recipes with a short, medium and long prepTime. For short prepTime include values: PT5M PT10M PT15M, for medium prepTime include values: PT20M PT25M PT30M PT35M PT40M, and for long prepTime include values: PT45M PT50M PT55M PT1H.

Answer: short = 7.644589513119085, medium = 10.307565077991807, long = 11.67237687366167, query:

```json
{
  "aggregations": {
    "prepTime": {
      "filters": {
        "filters": {
          "short": { "match": { "prepTime": "PT5M PT10M PT15M" } },
          "medium": {
            "match": { "prepTime": "PT20M PT25M PT30M PT35M PT40M" }
          },
          "long": { "match": { "prepTime": "PT45M PT50M PT55M PT1H" } }
        }
      },
      "aggregations": {
        "average_ingredients_number": {
          "avg": {
            "script": { "source": "doc['ingredients.keyword'].length" }
          }
        }
      }
    }
  },
  "size": 0
}
```

6. In which year where most cocktail (“cocktail” in name field) recipes published?

Answer: 2014, query:

```json
{
  "query": {
    "match": {
      "name": "cocktail"
    }
  },
  "aggregations": {
    "cocktail_recipes_per_year": {
      "date_histogram": {
        "field": "datePublished",
        "format": "YYYY-MM-DD",
        "calendar_interval": "1y"
      }
    },
    "max_cocktail_recipes": {
      "max_bucket": {
        "buckets_path": "cocktail_recipes_per_year._count"
      }
    }
  },
  "size": 0
}
```
