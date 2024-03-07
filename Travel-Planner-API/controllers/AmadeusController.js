const axios = require("axios");
const fs = require("fs");
const path = require("path");
const { getToken } = require("../amadeus/amadeus");
async function getFromAxios(url, headers) {
  try {
    const response = await axios.get(url, { headers });
    return response.data;
  } catch (e) {
    console.log(e.message);
  }
}
module.exports.getHotels = async (req, res, next) => {
  try {
    const { code } = req.query;
    const headers = req.headers;
    if (code === "BOM") {
      const json = fs.readFileSync(
        path.join(__dirname, "hotel.json"),
        "utf-8"
      );
      const data = JSON.parse(json);
      return res.json(data);
    }
    const url = `https://test.api.amadeus.com/v1/reference-data/locations/hotels/by-city?cityCode=${code}`;
    const hotels = await getFromAxios(url, headers);
    return res.json(hotels);
  } catch (e) {
    next(e);
  }
};
module.exports.getHotelReview = async (req, res, next) => {
  try {
    const { hotelId } = req.query;
    const headers = req.headers;
    const url = `https://test.api.amadeus.com/v2/e-reputation/hotel-sentiments?hotelIds=${hotelId}`;

    const sentiment = await getFromAxios(url, headers);
    console.log(sentiment);
    const item = sentiment[0];
    const { overallRating, sentiments } = item;
    const {
      service,
      facilities,
      roomComforts,
      catering,
      staff
    } = sentiments
    const review = {
      overallRating,service,facilities,roomComforts,catering,staff
    }
    return res.json(review)
  } catch (e) {
    next(e);
  }
};
module.exports.
getFlights = async (req, res, next) => {
  try {
    const { src, des, date, range } = req.query;
    console.log(date,range);
    if(src === "DEL" && des=== "BOM"){
      const json = fs.readFileSync(path.join(__dirname,"flight.json"),'utf-8')
      const data = JSON.parse(json)
      return res.json(data)

    }
    const headers = req.headers;
    const url = `https://test.api.amadeus.com/v1/shopping/flight-dates?origin=${src}&destination=${des}&departureDate=${date},${range}&oneWay=true&nonStop=false&viewBy=DATE`;
    const flights = await getFromAxios(url, headers);
    const flightDates = flights.data;
    const promises = flightDates.map(async (item) => {
      const api = item.links.flightOffers;
      const date = item.departureDate;
      const flightOndate = await getFromAxios(api,headers);
      const arrFlight = flightOndate.data.slice(0, 15);
      const flightData = arrFlight.map((item) => {
        const { duration, segments } = item.itineraries[0];
        const { total } = item.price;
        const inr = parseFloat(total);
        const price = inr * 90.3;
        const { departure, arrival } = segments[0];
        return {
          date,
          duration,
          price,
          departure,
          arrival,
        };
      });
      return {
        date,
        flightData,
      };
    });
    const data = await Promise.all(promises);
    fs.writeFileSync(path.join(__dirname, "flight.json"), JSON.stringify(data));
    return res.json(data);
  } catch (e) {
    next(e);
  }
};
module.exports.getDestination = async (req, res, next) => {
  try {
    const { lat, lon } = req.query;
    const headers = req.headers;
    if (true){
      const json = fs.readFileSync(
        path.join(__dirname, "des.json"),
        "utf-8"
      );
      const data = JSON.parse(json);
      return res.json(data);
    }
    const url = `https://test.api.amadeus.com/v1/shopping/activities?latitude=41.397158&longitude=2.160873&radius=1`;
    const trips = await getFromAxios(url, headers);
    const trip = trips;
    const data = trip
      .map((item) => {
        const {
          name,
          description,
          price,
          pictures,
          bookingLink,
          minimumDuration,
        } = item;
        const { amount, currencyCode } = price;
        const hour = minimumDuration.replace("hours", "H");
        const duration = hour.replace("minutes", "M");
        return {
          name,
          description,
          amount,
          currencyCode,
          pictures,
          bookingLink,
          minimumDuration: duration,
        };
      })
      .filter((item) => item !== null);
      fs.writeFileSync(path.join(__dirname,"des.json"),JSON.stringify(data))
    return res.json(data);
  } catch (e) {
    next(e);
  }
};
module.exports.getActivities = async (req, res, next) => {
  try {
    const { lat, lon } = req.query;
    const json = fs.readFileSync(path.join(__dirname,"activity.json"),'utf-8')
    const data = JSON.parse(json)
    return res.json(data);
    const headers = req.headers;
    const url = `https://test.api.amadeus.com/v1/reference-data/locations/pois?latitude=41.397158&longitude=2.160873&radius=1&page%5Blimit%5D=10&page%5Boffset%5D=0`;
    const activities = await getFromAxios(url, headers);
    const activity = activities.map((item) => {
      const { name, category, rank, tags } = item;
      return { name, category, rank, tags };
    });
  } catch (e) {
    next(e);
  }
};
