const Router = require('@koa/router');
const infoRouter = require('./info');
const recipeRouter = require('./recipes');

const router = new Router();

router.use('/info', infoRouter.routes(), infoRouter.allowedMethods());
router.use('/recipes', recipeRouter.routes(), recipeRouter.allowedMethods());

module.exports = router;