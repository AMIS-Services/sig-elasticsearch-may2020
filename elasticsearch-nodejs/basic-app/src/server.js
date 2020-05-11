const Koa = require("koa");
const Router = require('@koa/router');

const app = new Koa();
const router = new Router();

router.get('/', (ctx, next) => {
  ctx.body = 'Hello world!'
});

app.use(router.routes())
  .use(router.allowedMethods())
  .listen(3100, err => {
    if (err) throw errc
    console.log("Server running on port 3100");
  });
