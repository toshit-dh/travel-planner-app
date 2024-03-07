const multer = require("multer");
const PDFDocument = require("pdfkit");
const fs = require("fs");
const path = require("path");
const { v4 } = require("uuid");
const nodemailer = require("nodemailer");
const MIME_TYPE_MAP = {
  "image/png": "png",
  "image/jpeg": "jpeg",
  "image/jpg": "jpg",
};
const file = multer({
  limits: 500000000,
  storage: multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, "data/tickets");
    },
    filename: (req, file, cb) => {
      const { user } = req.user;
      const ext = MIME_TYPE_MAP[file.mimetype];
      cb(null, user + v4() + "." + ext);
    },
  }),
  fileFilter: (req, file, cb) => {
    const isValid = !!MIME_TYPE_MAP[file.mimetype];
    let error = isValid ? null : new Error("Invalid File Type");
    cb(error, isValid);
  },
});
const generatePdf = (req, res,next) => {
  try {
    const jsonData = JSON.parse(req.itenary);
    const pdfPath = path.join(path.dirname(__dirname),"data","itenaries",`${v4()}.pdf`)
    const doc = new PDFDocument();
    const stream = fs.createWriteStream(pdfPath);
    doc.pipe(stream);
    doc.fontSize(20).text("Travel Itinerary", { align: "center" });
    jsonData.forEach((day, index) => {
      if (index !== jsonData.length - 1) {
        const dayHeading = `Day ${index + 1}`;
        doc.fontSize(18).text(dayHeading, { align: "center" });
        ["morning", "afternoon", "evening"].forEach((time) => {
          const activity = day[time].activity;
          const cost = day[time].cost;
          doc
            .fontSize(14)
            .text(
              `${time.charAt(0).toUpperCase() + time.slice(1)}: ${activity}`,
              { indent: 20 }
            );
          doc.fontSize(12).text(`Cost: ${cost}`, { indent: 40 });
          doc.moveDown();
        });
      }
      if (
        index === jsonData.length - 1 &&
        jsonData[index].interestingActivities
      ) {
        doc.fontSize(16).text("Interesting Activities", { align: "center" });
        jsonData[index].interestingActivities.forEach((interest, i) => {
          const activity = interest.activity;
          const cost = interest.cost;
          doc
            .fontSize(14)
            .text(`Activity ${i + 1}: ${activity}`, { indent: 20 });
          doc.fontSize(12).text(`Cost: ${cost}`, { indent: 40 });
          doc.moveDown();
        });
      }
    });
    doc.end();
    req.pdfPath = pdfPath
    req.itenaryData = jsonData
    next()
  } catch (e) {
    console.log(e.message);
  }
};
const generateEmail = async (req, res, next) => {
    const userEmail = req.email;
    console.log(userEmail);
    const pdfPath = req.pdfPath
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
      subject: "Travel Itenary",
      text: `Click the following pdf of 10 days travel itenary`,
      attachments: [
        {
          filename: "TravelItinerary.pdf",
          path: pdfPath,
          encoding: "base64",
        },
      ],
    };
    try {
      await transporter.sendMail(mailOptions);
      fs.unlink(pdfPath, (err) => {
        if (err) {
          console.error("Error deleting PDF file:", err);
        } else {
          console.log("PDF file deleted successfully.");
        }
      });
      let intrestingActivitiesObj;
      const dayPlan = req.itenaryData.map((item,index)=>{
        if(index!=10) return item
        if(index==10) intrestingActivitiesObj = item
      }).filter((item)=>item)
      const {interestingActivities} = intrestingActivitiesObj
      return res
        .status(200)
        .json(
          { message: "Email sent to your registered email",dayPlan,intrestingActivities: interestingActivities}
        );
    } catch (error) {
      console.error("Error sending email:", error);
      res.status(500).json({ error: "Internal Server Error" });
    }
  };
  
module.exports = { file, generatePdf,generateEmail};
