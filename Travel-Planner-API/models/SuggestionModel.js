const mongoose = require("mongoose");
const suggestSchema = mongoose.Schema(
  {
    by: {
      type: String,
      required: true,
    },
    tag : {
        type: String,
        required: true,
    },
    msg : {
        type: String,
        required: true
    },
    loc : {
        type: Object
    },
    sentiment : {
      type : String,
      required : true
    },
    votes : {
      type : Number,
      default : 0
    },
    votedBy: {
      type : [mongoose.Types.ObjectId],
      default: []
    }
  },
  {
    timestamps: true,
  }
);
module.exports = mongoose.model("Suggest", suggestSchema);

