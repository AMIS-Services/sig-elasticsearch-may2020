const Router = require('@koa/router');
const { esClient } = require('../elasticsearch/connection');

const router = new Router();

router.get('/health', async (ctx, next) => {
  ctx.body = await esClient.cluster.health();
});

module.exports = router;