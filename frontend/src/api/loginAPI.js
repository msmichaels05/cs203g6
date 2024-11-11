import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080'; // Your backend URL
const authHeader = 'Basic ' + btoa('admin:goodpassword'); // Basic Authentication credentials

// Create an axios instance with default headers
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Authorization': authHeader, // Use this for basic authentication
  },
});

// Login API: Authenticates user and returns user information
export const loginAPI = async (username, password) => {
  try {
    const response = await axiosInstance.post('/', {
      username,
      password,
    });

    // Assuming the response includes user info like a token or user details
    return response.data;  // Return the data received from the backend
  } catch (error) {
    console.error('Error logging in:', error);
    throw error; // Propagate error for frontend to handle
  }
};
