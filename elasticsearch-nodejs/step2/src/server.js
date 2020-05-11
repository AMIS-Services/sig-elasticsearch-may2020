const Koa = require("koa");
const router = require("./routes");

const app = new Koa();

app
  .use(router.routes())
  .use(router.allowedMethods())
  .listen(3100, (err) => {
    if (err) throw errc;
    console.log("Server running on port 3100");
  });
