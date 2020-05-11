const Koa = require("koa");
const router = require('./routes');
const bodyParser = require('koa-bodyparser');

const app = new Koa();

app.use(bodyParser());

app.use(router.routes())
  .use(router.allowedMethods())
  .listen(3100, err => {
    if (err) throw errc
    console.log("Server running on port 3100");
  });