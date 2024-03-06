const {getToken} = require('../amadeus/amadeus')
let token = null
let tokenExpiredTime = null
const giveToken = async (req,res,next)=>{
    const currentTime = Date.now()
    if(!token && currentTime >= tokenExpiredTime){
        token = await getToken()
        tokenExpiredTime = currentTime + 2900000
    }
    const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
    req.headers = headers
    next()
}
module.exports.giveToken = giveToken