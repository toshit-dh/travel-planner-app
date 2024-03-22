const User = require("../models/UserModel");
const jwt = require("jsonwebtoken");
const nodemailer = require("nodemailer");
module.exports.verifyToken = (req, res, next) => {
  const token = req.headers.authorization;
  if (!token) {
    return res.status(401).json({ msg: "No token provided", status: false });
  }
  jwt.verify(token, "travelapi", (err, decoded) => {
    if (err) {
      return res.status(401).json({ msg: "Invalid token", status: false });
    }
    req.user = decoded;
    next();
  });
};
module.exports.generateEmail = async (req, res, next) => {
  const userEmail = req.email;
  const token = req.token;
  const transporter = nodemailer.createTransport({
    service: "gmail",
    host: "smtp.gmail.com",
    port: 587,
    secure: false,
    auth: {
      user: process.env.TRAVEL_EMAIL,
      pass: process.env.TRAVEL_APP_PASSWORD,
    },
  });
  const mailOptions = {
    from: process.env.TRAVEL_EMAIL,
    to: userEmail,
    subject: "Email Verification",
    text: `Click the following link to verify your email: http://192.168.127.70:5000/api/auth/verifyEmail/${token}`,
  };
  try {
    await transporter.sendMail(mailOptions);
    return res
      .status(200)
      .json(
        { message: "SignUp Successfull. Email sent for verification",token}
      );
  } catch (error) {
    console.error("Error sending email:", error);
    res.status(500).json({ error: "Internal Server Error" });
  }
};
