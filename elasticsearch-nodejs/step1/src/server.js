const Koa = require("koa");
const Router = require("@koa/router");
const elasticsearch = require("elasticsearch");

const app = new Koa();
const router = new Router();

const esClient = new elasticsearch.Client({
  host: "http://localhost:9200",
  log: "info",
});

router.get("/health", async (ctx, next) => {
  ctx.body = await esClient.cluster.health();
});

app
  .use(router.routes())
  .use(router.allowedMethods())
  .listen(3101, (err) => {
    if (err) throw errc;
    console.log("Server running on port 3100");
  });
