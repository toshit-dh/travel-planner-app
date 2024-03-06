const { register, login, getData,verifyEmail} = require('../controllers/UserController')
const {verifyToken,generateEmail} = require("../middlewares/UserMiddleware")
const router = require('express').Router()
router.post("/register",register,generateEmail)
router.get("/verifyEmail/:token",verifyEmail)
router.post("/login",login)
router.get("/getData",verifyToken,getData)
module.exports = router
