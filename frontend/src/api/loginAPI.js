import axios from 'axios';

const API_BASE_URL = "http://18.141.57.161:8080"; // Your backend URL


export const loginAPI = async (username, password) => {
  const authHeader = 'Basic ' + btoa(`${username}:${password}`);  // Using Basic Auth
  
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

    // Handle response data
    const data = response.data;

    // Extract the role from authorities array (assuming there is only one role)
    const role = data.authorities && data.authorities.length > 0 
      ? data.authorities[0].authority // Extract the first role (ROLE_USER, ROLE_ADMIN, etc.)
      : null;

    // Store role, username, password, and user info in localStorage
    if (role) {
      localStorage.setItem('role', role);  // Store role
      localStorage.setItem('user', JSON.stringify(data));  // Store the full user data (user details)
      localStorage.setItem('username', username);  // Store username
      localStorage.setItem('password', password);  // Store password (be cautious with this)
    }

    return data;  // Return the user data (including role and details)
  } catch (error) {
    console.error('Error logging in:', error);
    console.log(API_BASE_URL)
    throw error;
  }
};