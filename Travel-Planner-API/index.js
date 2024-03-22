const express = require("express");
const path = require("path");
const cors = require("cors");
const morgan = require('morgan')
const mongoose = require("mongoose");
const socketIO = require("socket.io");
const userRoutes = require("./routes/UserRoutes");
const getRoutes = require("./routes/AmadeusRoutes");
const tripRoutes = require("./routes/TripRoutes");
const suggRoutes = require("./routes/SuggestionRoutes");
const chatsRoute = require("./routes/ChatsRoute");
require("dotenv").config();
const app = express();
app.use(cors());
app.use(morgan('combined'))
app.set("view engine", "ejs");
app.set("views", path.join(__dirname, "views"));
app.use(express.json({ limit: "50mb" }));
app.get("/", (_, res) => res.json("WELCOME TO TRAVEL PLANNER APP"));
app.use("/api/auth", userRoutes);
app.use("/get/amadeus", getRoutes);
app.use("/trip", tripRoutes);
app.use("/sugg", suggRoutes);
app.use("/data/tickets", express.static(path.join("data", "tickets")));
app.use("/data/photos", express.static(path.join("data","photos")))
mongoose
  .connect(process.env.DB_URL)
  .then(() => {
    console.log("DB Connected");
  })
  .catch((err) => {
    console.log(err.message);
  });

const server = app.listen(process.env.PORT || 5000, () => {
  console.log(`Listening to 5000`);
});
const io = socketIO(server);
io.on("connection", (socket) => {
  console.log("A user connected");
  socket.on("joinRoom", (roomId) => {
    socket.join(roomId);
  });
  socket.on("disconnect", () => {
    console.log("User disconnected");
  });
  socket.on("joinnotif",(userId)=>{
    console.log(`User connected with userId: ${userId}`);
  })
  socket.on("error", (error) => {
    console.error("Socket connection error:", error);
  });
});
io.engine.on("connection_error", (err) => {
  console.log(err.code);  
  console.log(err.message);  
  console.log(err.context); 
});
app.use((req, res, next) => {
  req.io = io;
  next();
});
app.use("/chats", chatsRoute);