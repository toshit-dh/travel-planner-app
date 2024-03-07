const axios = require('axios');
const accessTokenUrl = 'https://test.api.amadeus.com/v1/security/oauth2/token';
const headers = {
  'Content-Type': 'application/x-www-form-urlencoded'
};
async function getToken() {
  try {
    const requestBody = {
      grant_type: 'client_credentials',
      client_id: process.env.AMADEUS_API_KEY,
      client_secret: process.env.AMADEUS_SECRET_KEY
    };
    const response = await axios.post(accessTokenUrl, new URLSearchParams(requestBody), { headers });
    return response.data.access_token;
  } catch (error) {
    console.error('Errors:', error.response.data);
  }
}
module.exports = { getToken}
