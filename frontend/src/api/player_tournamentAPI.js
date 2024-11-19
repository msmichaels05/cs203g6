import axios from 'axios';

// Define the API base URL
const API_BASE_URL = "http://18.141.57.161:8080";

// Create an axios instance without default credentials
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Function to get the authorization header from localStorage or other user context
const getAuthHeader = () => {
  const username = localStorage.getItem('username'); // or from context/store
  const password = localStorage.getItem('password'); // or from context/store

  // If username and password exist in localStorage, create the authorization header
  if (username && password) {
    return 'Basic ' + btoa(`${username}:${password}`);
  }

  // If no credentials found, throw an error indicating the issue
  throw new Error('Authorization required: Missing username or password.');
};

// Function to join a tournament by sending a POST request
export const joinTournament = async (id) => {
  try {
    const authHeader = getAuthHeader(); // Get the authorization header
    const response = await axiosInstance.post(`/tournaments/${id}/players`, {}, {
      headers: {
        'Authorization': authHeader,
      },
    });
    return response.data;
  } catch (error) {
    // Check for specific error types
    if (error.response) {
      // HTTP error response from the server
      switch (error.response.status) {
        case 400:
          console.error('Bad Request:', error.response.data);
          alert('Bad Request: Please check the data you provided.');
          break;
        case 401:
          console.error('Unauthorized:', error.response.data);
          alert('Unauthorized: You must log in first.');
          break;
        case 404:
          console.error('Tournament Not Found:', error.response.data);
          alert('Tournament not found. Please try again.');
          break;
        default:
          console.error('Error:', error.response.data);
          alert('An unexpected error occurred. Please try again later.');
      }
    } else if (error.request) {
      // Network error (e.g., no response from server)
      console.error('Network Error:', error.request);
      alert('Network error: Unable to reach the server. Please try again later.');
    } else {
      // Something went wrong while setting up the request
      console.error('Request Error:', error.message);
      alert('An error occurred. Please try again.');
    }
    throw error; // Re-throw the error to propagate it
  }
};
