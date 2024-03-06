const axios = require('axios');
const apiKey = process.env.AMADEUS_API_KEY
const apiSecret = process.env.AMADEUS_SECRET_KEY
const accessTokenUrl = 'https://test.api.amadeus.com/v1/security/oauth2/token';
const requestBody = {
  grant_type: 'client_credentials',
  client_id: apiKey,
  client_secret: apiSecret
};
const headers = {
  'Content-Type': 'application/x-www-form-urlencoded'
};
async function getToken() {
  try {
    const response = await axios.post(accessTokenUrl, new URLSearchParams(requestBody), { headers });
    return response.data.access_token;
  } catch (error) {
    console.error('Error:', error.response.data);
  }
}
module.exports = { getToken}
