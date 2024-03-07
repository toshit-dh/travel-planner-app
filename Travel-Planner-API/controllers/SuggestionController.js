const Suggestion = require("../models/SuggestionModel");
const User = require("../models/UserModel");
const Sentiment = require("sentiment");
const sentimentAnalyse = new Sentiment();
module.exports.addSugg = async (req, res, next) => {
  try {
    const { user } = req.user;
    console.log(user);
    const { name } = await User.findOne({ _id: user });
    console.log(name);
    const { tag, city, country, msg } = req.query;
    const cityL = city.toLowerCase();
    console.log(tag, city, country, msg);
    const { score } = sentimentAnalyse.analyze(msg);
    const body = {
      by: name,
      tag,
      loc: { city: cityL, country },
      msg,
      sentiment: score,
    };
    console.log(body);
    await Suggestion.create(body);
    console.log("done");
    return res.json({ status: true, msg: "Suggestion Added" });
  } catch (e) {
    console.log(e.message);
    res
      .status(500)
      .json({ status: false, msg: `Suggestion Not Added ${e.message}` });
    next(e);
  }
};
module.exports.removeSugg = async (req, res, next) => {
  try {
    const { suggId } = req.query;
    const sugg = await Suggestion.findByIdAndDelete({ _id: suggId });
    if (!sugg) return res.json({ status: false, msg: "No Suggestion Found" });
    return res.json({ status: true, msg: "Suggestion Removed" });
  } catch (e) {
    next(e);
  }
};
module.exports.getSugg = async (req, res, next) => {
  try {
    const { tag, city } = req.query;
    const cityL = city.toLowerCase();
    const suggestions = await Suggestion.find({ tag, "loc.city": cityL });
    const suggestion = suggestions.map((item) => {
      const { createdAt } = item;
      const date = createdAt.toISOString().split("T")[0];
      const fname = item.by.split(" ")[0];
      return {
        id: item._id,
        by: fname,
        tag: item.tag,
        loc: {
          city: item.loc.city,
          country: item.loc.country,
        },
        msg: item.msg,
        votes: item.votes,
        date,
        sentiment: item.sentiment,
        isVoted: item.votedBy.reduce(
          (isVoted, item) => isVoted || item == req.user.user,
          false
        ),
      };
    });
    console.log(suggestion);
    return res.json(suggestion);
  } catch (error) {
    next(e);
  }
};
module.exports.getUserSugg = async (req, res, next) => {
  try {
    const { user } = req.user;
    const { name } = await User.findOne({ _id: user });
    const suggestions = await Suggestion.find({ by: name });
    const mySuggestion = suggestions.map((item) => {
      const { createdAt } = item;
      const date = createdAt.toISOString().split("T")[0];
      const fname = item.by.split(" ")[0];
      return {
        id: item._id,
        by: fname,
        tag: item.tag,
        loc: {
          city: item.loc.city,
          country: item.loc.country,
        },
        msg: item.msg,
        votes: item.votes,
        date,
        sentiment: item.sentiment,
        isVoted: false
      };
    });
    return res.json(mySuggestion);
  } catch (e) {
    console.log(e.message);
    next(e);
  }
};
module.exports.voteSugg = async (req, res, next) => {
  try {
    const { suggId } = req.query;
    console.log(suggId);
    const suggestion = await Suggestion.findOne({ _id: suggId });
      suggestion.votes += 1;
      suggestion.votedBy.push(req.user.user);
      await suggestion.save();
      return res.json({ status: true, msg: "Vote Added" });
  } catch (e) {
    next(e);
  }
};
module.exports.removeVote = async (req, res, next) => {
  try {
    const { suggId } = req.query;
    const suggestion = await Suggestion.findOne({ _id: suggId });
    suggestion.votes -= 1;
    const removedUser = suggestion.votedBy.filter((item)=>item!=req.user.user)
    suggestion.votedBy = removedUser
    await suggestion.save();
    return res.json({ status: true, msg: "Vote Removed" });
  } catch (e) {
    next(e);
  }
};
