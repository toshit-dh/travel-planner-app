const User = require("../models/UserModel");
const jwt = require("jsonwebtoken");
const jwtkey = "travelapi";
const bcrypt = require("bcrypt");
const crypto = require('crypto')
const ejs = require('ejs')
const path = require('path')
module.exports.register = async (req, res, next) => {
  try {
    const { name,username, email, password } = req.body;
    const isUsername = await User.findOne({ username });
    if (isUsername)
      return res.status(401).json({ msg: "Username already exists", status: false });
    const isEmail = await User.findOne({ email });
    if (isEmail) return res.status(401).json({ msg: "Email already used", status: false });
    const hashPass = await bcrypt.hash(password, 10);
    const verify = crypto.randomBytes(32).toString('hex');
    const user = await User.create({ name, username, email, password: hashPass,token: verify });
    delete user.password;
    const token = jwt.sign({ user: user._id }, jwtkey)
    req.token = verify
    req.email = email
    next()
  } catch (e) {
    res.status(401).json({msg: e.message, status: false})
  }
}
module.exports.verifyEmail = async (req, res, next) => {
  try {
    const { token } = req.params;
    console.log(token);
    const views = path.join(path.dirname(__dirname),'views')
    const user = await User.findOne({ token });
    console.log(user);
    if (!user) {
      return res.status(401).json({ msg: "User doesn't exist", status: false });
    }
    if (token === user.token) {
      user.verified = true;
      delete user.token
      await user.save();
      const htmlContent = await ejs.renderFile(path.join(views,'verify.ejs'), { success: true });
      return res.status(200).send(htmlContent);
    } else {
      const htmlContent = await ejs.renderFile(path.join(views,'verify.ejs'), { success: false, errorMsg: "Invalid token" });
      return res.status(401).send(htmlContent);
    }
  } catch (e) {
    next(e);
  }
}
module.exports.login = async (req, res, next) => {
  try {
    const { username, password } = req.body;
    const user = await User.findOne({ username });
    if (!user) return res.status(404).json({ msg: "No user found", status: false });
    if(!user.verified) return res.status(403).json({ msg: "Verify Email First", status: false })
    if (!(await bcrypt.compare(password, user.password)))
      return res.status(401).json({ msg: "Wrong Password", status: false });
    else {
      const token = jwt.sign({ user: user._id }, jwtkey);
      return res.json({ status: true, token });
    }
  } catch (e) {
    next(e);
  }
}
module.exports.getData = async (req,res,next) =>{
  try {
    const {user} = req.user
    const {_id,username,name,email} = await User.findOne({_id: user})
    return res.json({_id,username,name,email})
  } catch (e) {
    next(e)
  }
}
