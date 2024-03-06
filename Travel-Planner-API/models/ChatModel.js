const mongoose = require("mongoose");
const messageSchema = new mongoose.Schema(
  {
    message: {
      type: String,
      required: true,
    },
    images : {
      type : [String]
    },
    from: {
      type: mongoose.Types.ObjectId,
      required: true,
    },
    fromWho : {
      type: String,
      required: true
    },
    isType: {
      type: String,
      required: true
    }
  },
  {
    timestamps: true,
  }
);

const chatSchema = mongoose.Schema({
  trip: {
    type: mongoose.Types.ObjectId,
    required: true,
  },
  messages: { type: [messageSchema], default: [] },
});
module.exports = mongoose.model("Chats", chatSchema);
