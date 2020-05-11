// source : https://www.sitepoint.com/building-recipe-search-site-angular-elasticsearch/

const fs = require("fs");
const es = require("elasticsearch");
const client = new es.Client({
  host: "localhost:9200",
});

fs.readFile("recipeitems.json", { encoding: "utf-8" }, (err, data) => {
  if (err) {
    throw err;
  }

  // Build up a giant bulk request for elasticsearch.
  bulk_request = data.split("\n").reduce((bulk_request, line) => {
    let obj, recipe;

    try {
      obj = JSON.parse(line);
    } catch (e) {
      console.log("Done reading");
      return bulk_request;
    }

    // Rework the data slightly
    recipe = {
      id: obj._id.$oid, // Was originally a mongodb entry
      name: obj.name,
      source: obj.source,
      url: obj.url,
      recipeYield: obj.recipeYield,
      ingredients: obj.ingredients.split("\n"),
      prepTime: obj.prepTime,
      cookTime: obj.cookTime,
      datePublished: obj.datePublished,
      description: obj.description,
    };

    bulk_request.push({ index: { _index: "recipes", _id: recipe.id } });
    bulk_request.push(recipe);
    return bulk_request;
  }, []);

  let busy = false;
  // Recursively whittle away at bulk_request, 1000 at a time.
  const perhaps_insert = () => {
    if (!busy) {
      busy = true;
      client.bulk(
        {
          body: bulk_request.slice(0, 1000),
        },
        (err, resp) => {
          if (err) {
            console.log(err);
          }
          busy = false;
        }
      );
      bulk_request = bulk_request.slice(1000);
      console.log(bulk_request.length);
    }

    if (bulk_request.length > 0) {
      setTimeout(perhaps_insert, 10);
    } else {
      console.log("Inserted all records! :)");
    }
  };

  perhaps_insert();
});
