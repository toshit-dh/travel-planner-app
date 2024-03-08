const { default: axios } = require("axios");
const Chat = require("../models/ChatModel");
const fs = require("fs");
const path = require('path')
const User = require("../models/UserModel");
const { Trip } = require("../models/TripModel");
const { default: mongoose } = require("mongoose");
module.exports.addTrip = async (req, res, next) => {
  try {
    const id = req.user.user;
    const { departureDate, departureTime, returndate, city, tripmates } =
      req.body;
    const departureDateTime = `${departureDate}T${departureTime}`;
    const filename = req.file.filename;
    const imageUrl = `/data/tickets/${filename}`;
    const user = await User.findOne({ _id: id });
    const date = new Date(departureDateTime);
    const retdate = new Date(returndate);
    let tripMatesId;
    if (Array.isArray(tripmates)) {
      tripMatesId = tripmates.map((item) =>
        mongoose.Types.ObjectId.createFromHexString(item.replace(/"/g, ""))
      );
    } else if (typeof tripmates === "string") {
      tripMatesId = mongoose.Types.ObjectId.createFromHexString(
        tripmates.replace(/"/g, "")
      );
    }
    const trip = await Trip.create({
      date,
      returndate: retdate,
      city,
      ticket: imageUrl,
      tripmates: tripmates
        ? Array.isArray(tripmates)
          ? [...tripMatesId, req.user.user]
          : [tripMatesId,req.user.user]
        : [req.user.user],
      creator: req.user.user,
    });
    if (Array.isArray(tripmates)) {
      const tripMatesPromises = tripMatesId.map(async (item) => {
        const id = item;
        const user = await User.findById(id);
        console.log(user);
        user.tripsRequests.unshift(trip);
        await user.save();
      });
      await Promise.all(tripMatesPromises);
    }
    else{
      const user = await User.findById(tripMatesId);
      user.tripsRequests.unshift(trip);
      await user.save()
    }
    user.trips.unshift(trip._id);
    await user.save();
    await Chat.create({ trip: trip._id });
    // if(Array.isArray(tripmates)){
    //   tripmates.map((item)=>{
    //     req.io.to(item).emit("tripzrequests",{_id: trip._id,city,date,creator: id})
    //   })
    // }
    // else{
    //   req.io.to(tripMatesId).emit("tripzrequests",{_id: trip._id,city,date,creator: id})
    // }
    return res.status(200).json({ msg: "Trip Added" })
  } catch (e) {
    console.log(e.message);
    if (req.file && req.file.filename) {
      try {
        fs.unlink(`data/tickets/${req.file.filename}`, () =>
          console.log("File Deleted")
        );
      } catch (err) {
        console.error("Error deleting file:", err);
      }
    }
    next(e);
  }
};
module.exports.getTrip = async (req, res, next) => {
  try {
    const id = req.user.user;
    const user = await User.findOne({ _id: id });
    const tripsPromises = user.trips.map(async (item) => {
      const { _id, creator, date, returndate, city, ticket, tripmates } =
        await Trip.findById(item);
      const { name } = await User.findById(creator);
      const tripMatesPromises = tripmates.map(async (item) => {
        const { name } = await User.findById(item);
        return name;
      });
      const tripMates = await Promise.all(tripMatesPromises);
      return { _id, creator: name, date, returndate, city, ticket, tripMates };
    });
    const trips = await Promise.all(tripsPromises);
    console.log(trips);
    return res.json(trips);
  } catch (e) {
    next(e);
  }
};
module.exports.cancelTrip = async (req, res, next) => {
  try {
    const { id } = req.query;
    await Trip.findOneAndDelete({ _id: id });
    const iD = req.user.user;
    const user = await User.findOne({ _id: iD });
    let userTrips = user.trips;
    user.trips = userTrips.filter((item) => {
      if (item._id.toString() === id) return false;
      else return true;
    });
    console.log(userTrips);
    await user.save();
    return res.json({ msg: "Trip Cancelled" });
  } catch (e) {
    next(e);
  }
};
module.exports.getWeather = async (req, res, next) => {
  try {
    const { cityName } = req.query;
    console.log(cityName);
    const openWeatherAPIKey = "52363e6c0c51824d1e3dd833dc3c0918";
    const url = `https://api.openweathermap.org/data/2.5/forecast?q=${cityName.trim()},&APPID=${openWeatherAPIKey}`;
    try {
      console.log(url);
      const weather = await axios.get(url);
      const hour = weather.data.list;
      const data = hour.map((item) => {
        const { main, weather, clouds, wind, dt_txt } = item;
        const { temp, pressure, humidity, sea_level, grnd_level } = main;
        const tempinC = temp - 273.15;
        const { description, icon } = weather[0];
        const iconurl = `https://openweathermap.org/img/wn/${icon}@2x.png`;
        const { all } = clouds;
        const { speed, deg, gust } = wind;
        return {
          tempinC,
          pressure,
          humidity,
          sea_level,
          grnd_level,
          description,
          iconurl,
          cloud: all,
          windSpeed: speed,
          windDeg: deg,
          windGust: gust,
          time: dt_txt,
        };
      });
      return res.json(data);
    } catch (e) {
      console.log(e.message);
      throw e;
    }
  } catch (e) {
    console.log(e.message);
    next(e);
  }
};
module.exports.planItenary = async (req, res, next) => {
  try {
    const userId = req.user.user
    const {email} = await User.findById(userId)
    const { location, stay_days, budget } = req.query;
    console.log(location);
    if(location.toLowerCase() === "pune"){
      const data = fs.readFileSync(path.join(__dirname,"itenary.json"),'utf-8')
      req.itenary = data
      req.email = email
      next()
    }else{
    try {
      const response = await axios.post(
        "http://127.0.0.1:5000/make-trip-plan",
        {
          location: location.trim(),
          stay_days: stay_days.trim(),
          budget: budget.trim(),
        }
      );
      const data = response.data.slice(7,-3)
      req.itenary = data
      req.email = email
      next()
    } catch (e) {
      console.log(e.message);
    }}
  } catch (e) {
    console.log(e.message);
  }
};
