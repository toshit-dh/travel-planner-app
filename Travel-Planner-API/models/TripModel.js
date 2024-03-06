const mongoose = require('mongoose')
const tripSchema = mongoose.Schema({
    creator : {
        type : mongoose.Types.ObjectId,
        required : true
    },
    date : {
        type: Date,
        required: true,
    },
    returndate : {
        type: Date,
        required: true,
    },
    city : {
        type: String,
        required: true
    },
    ticket : {
        type: String,
        required: true
    },
    tripmates : {
        type : [mongoose.Types.ObjectId],
        default: []
    },
})
const Trip = mongoose.model("Trips",tripSchema)
module.exports = {Trip,tripSchema}