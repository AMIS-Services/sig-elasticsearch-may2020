const Router = require("@koa/router");
const { esClient } = require("../elasticsearch/connection");
const { v4: uuidv4 } = require("uuid");

const router = new Router();

router.post("/", async (ctx, next) => {
  ctx.body = await esClient.create({
    index: "recipes",
    id: uuidv4(),
    body: ctx.request.body,
  });
});

router.put("/:id", async (ctx, next) => {
  ctx.body = await esClient.update({
    index: "recipes",
    id: ctx.params.id,
    body: { doc: ctx.request.body },
  });
});

router.delete("/:id", async (ctx, next) => {
  ctx.body = await esClient.delete({ index: "recipes", id: ctx.params.id });
});

// "queries and aggregations hands-on" JSON queries #1
router.get("/newest", async (ctx, next) => {
  ctx.body = await esClient.search({
    index: "recipes",
    body: {
      size: 5,
      from: 20,
      sort: [{ datePublished: "desc" }, "name.keyword"],
      _source: ["name", "datePublished"],
    },
  });
});

// "queries and aggregations hands-on" JSON queries #4
router.post("/fuzzy-search-ingredients", async (ctx, next) => {
  ctx.body = await esClient.search({
    index: "recipes",
    body: {
      query: {
        fuzzy: {
          ingredients: {
            value: ctx.request.body.searchvalue,
            fuzziness: 2,
          },
        },
      },
    },
  });
});

module.exports = router;
