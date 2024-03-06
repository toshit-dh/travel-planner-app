const { Trip } = require("../models/TripModel");
const User = require("../models/UserModel");
const Chat = require("../models/ChatModel")
const fs = require('fs')
const io = require('../index')
module.exports.getItems = async (req, res, next) => {
  try {
    const currentUserId = req.user.user;
    const currentUser = await User.findById(currentUserId);
    if (!currentUser) {
      return res.status(404).json({ message: "User not found", status: false });
    }
    const allUsers = await User.find({});
    const users = allUsers
      .filter((item) => {
        return (
          item._id.toString() !== currentUserId.toString() &&
          !currentUser.friends.includes(item._id) &&
          !currentUser.friendsRequest.includes(item._id)
        );
      })
      .map((item) => ({
        _id: item._id,
        name: item.name,
        username: item.username,
      }));
    const friendsIds = currentUser.friends;
    const tripsIds = currentUser.trips;
    const friendsPromises = friendsIds.map(async (item) => {
      const friend = await User.findById(item._id);
      return {
        _id: friend._id,
        name: friend.name,
        username: friend.username,
      };
    });
    const tripPromises = tripsIds.map(async (item) => {
      const trip = await Trip.findById(item);
      const {name} = await User.findById(trip.creator);
        return {
        _id: trip._id,
        city: trip.city,
        creator: name
      };
    });
    const friends = await Promise.all(friendsPromises);
    const trips = await Promise.all(tripPromises);
    return res.json({ users, friends, trips});
  } catch (e) {
    console.log(e);
    next(e);
  }
};

module.exports.addFriendRequest = async (req, res, next) => {
  try {
    const currentUserId = req.user.user;
    console.log(currentUserId);
    const targetUserId = req.query.add_who;
    console.log(targetUserId);
    const currentUser = await User.findById(currentUserId);
    const targetUser = await User.findById(targetUserId);
    if (!currentUser || !targetUser) {
      console.log("h");
      return res.status(404).json({ message: "User not found", status: false });
    }
    if (targetUser.friendsRequest.includes(currentUserId)) {
      return res
        .status(400)
        .json({ message: "Friend request already sent", status: false });
    }
    targetUser.friendsRequest.push(currentUserId);
    await targetUser.save();
    console.log("jb");
    return res
      .status(200)
      .json({ msg: "Friend request sent successfully", status: true });
  } catch (e) {
    console.log(e);
    next(e);
  }
};

module.exports.acceptFriendRequest = async (req, res, next) => {
  try {
  } catch (e) {
    console.log(e);
    next(e);
  }
};
module.exports.rejectFriendRequest = async (req, res, next) => {
  try {
  } catch (e) {
    console.log(e);
    next(e);
  }
};
module.exports.addTripRequest = async (req, res, next) => {
  try {
  } catch (e) {
    console.log(e);
    next(e);
  }
};
module.exports.acceptTripRequest = async (req, res, next) => {
  try {
  } catch (e) {
    console.log(e);
    next(e);
  }
};
module.exports.rejectTripRequest = async (req, res, next) => {
  try {
  } catch (e) {
    console.log(e);
    next(e);
  }
};
module.exports.addMessage = async (req,res,next)=>{
  try {
    const userId = req.user.user
    const {name} = await User.findById(userId)
    const {text} = req.query
    const {tripId} = req.query
    const chat = await Chat.findOne({trip: tripId})
    const {messages} = chat
    const message = {
      message: text,
      from: userId,
      fromWho: name,
      isType: "text"
    }
    messages.push(message)
    await chat.save()
    req.io.to(tripId).emit("newMessage", {...message,fromWhoId: userId,fromSelf: false});
    return res.json({msg: "Message Added"})
  } catch (e) {
    next(e)
  }
}
module.exports.addImageMessage = async (req,res,next)=>{
  try {
    const userId = req.user.user
    const {name} = await User.findById(userId)
    const {tripId} = req.query
    const fileNames = req.files.map((item)=>{
      return `/data/photos/${item.filename}`
    })
    const chat = await Chat.findOne({trip: tripId})
    const {messages} = chat
    const message = {
      message: `Images...+${fileNames.length}`,
      from: userId,
      isType: "images",
      images : fileNames,
      fromWho: name
    }
    messages.push(message)
    await chat.save()
    req.io.to(tripId).emit("newMessage", {...message,fromWhoId: userId,fromSelf: false});
    return res.json({msg: "Image Added"})
  } catch (e) {
    try {
      const deletionPromises = filePaths.map((filePath) => fs.unlink(filePath));
      await Promise.all(deletionPromises);
      console.log('Files deleted successfully');
    } catch (error) {
      console.error('Error deleting files:', error);
    }
    next(e)
  }
}
module.exports.getTripMessages = async (req,res,next)=>{
  try {
    const userId = req.user.user
    const {tripId} = req.query
    const {messages} = await Chat.findOne({trip: tripId})
    let imageUrl = [];
    const messagePromise = messages.map(async(item)=>{
      const fromSelf = item.from.toString() === userId.toString()
      if(item.isType === "images"){
        imageUrl.push(...item.images)
      }
      return {...item.toObject(),fromSelf}
    })
    const message = await Promise.all(messagePromise)
    console.log(imageUrl);
    return res.status(200).json({message,imageUrl})
  } catch (e) {
    next(e)
  }
}
