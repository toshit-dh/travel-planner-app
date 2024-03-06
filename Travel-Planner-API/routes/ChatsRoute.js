const {
  addFriendRequest,
  acceptFriendRequest,
  rejectFriendRequest,
  addTripRequest,
  acceptTripRequest,
  rejectTripRequest,
  getItems,
  getTripMessages,
  addMessage,
  addImageMessage
} = require("../controllers/ChatController")
const {verifyToken} = require('../middlewares/UserMiddleware')
const file = require('../middlewares/ChatImagesUploadMiddleware')
const router = require("express").Router();
router.get("/addFriend",verifyToken,addFriendRequest)
router.get("/acceptFriend",verifyToken,acceptFriendRequest)
router.get("/rejectFriend",verifyToken,rejectFriendRequest)
router.get("/addTripRequest",verifyToken,addTripRequest)
router.get("/acceptTrip",verifyToken,acceptTripRequest)
router.get("/rejectTrip",verifyToken,rejectTripRequest)
router.get("/getChatItems",verifyToken,getItems)
router.get("/getTripMessages",verifyToken,getTripMessages)
router.get("/addMessage",verifyToken,addMessage)
router.post("/addImagesMessage",verifyToken,file.any(),addImageMessage)
module.exports = router;
