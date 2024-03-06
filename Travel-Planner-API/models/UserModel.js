const mongoose = require('mongoose')
const userSchema = mongoose.Schema({
    name: {
        type: String,
        required: true,
        min: 3,
        max: 15,
    },
    username : {
        type: String,
        required: true,
        min: 3,
        max: 15,
        unique: true
    },
    email : {
        type: String,
        required: true,
        min: 3,
        max: 45,
        unique: true,
        validate: {
            validator: function (value) {
                return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
            },
            msg: 'Invalid email format'
        }
    },
    password : {
        type: String,
        required: true,
        min: 8,
    },
    verified : {
        type : Boolean,
        default : false
    },
    token  : {
        type : String
    },
    trips : {
        type: [mongoose.Types.ObjectId],
        default: []
    },
    tripsRequests : {
        type: [mongoose.Types.ObjectId]
    },
    friends : {
        type: [mongoose.Types.ObjectId],
        default: []
    },
    friendsRequest : {
        type: [mongoose.Types.ObjectId],
        default: []
    }
})

module.exports = mongoose.model("Users",userSchema)