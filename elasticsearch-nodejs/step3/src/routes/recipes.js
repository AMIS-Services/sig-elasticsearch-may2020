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

module.exports = router;
