import axios from 'axios';

const API_BASE_URL = 'http://54.152.65.230:8080'; // Your backend URL

export const loginAPI = async (username, password) => {
  // Encode the credentials for Basic Auth
  const authHeader = 'Basic ' + btoa(`${username}:${password}`);  // Using username instead of email
  
  try {
    // Make the POST request to login
    const response = await axios.post(
      `${API_BASE_URL}/login`, // Ensure the endpoint is correct on your backend
      {}, // Pass an empty object as the body if no data is needed in the request body
      {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': authHeader, // Basic Auth header with username and password
        },
      }
    );

    // Handle response data (assuming the backend sends user data or a token)
    return response.data;
  } catch (error) {
    console.error('Error logging in:', error);
    throw error;
  }
};
