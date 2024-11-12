// src/api/player_tournament.js
import axios from 'axios';

// Define the API base URL
const API_BASE_URL = 'http://localhost:8080';

// Set the Authorization header with 'betatester:betatester'
const authHeader = 'Basic ' + btoa('betatester:betatester'); // Correct credentials

// Create an axios instance with the necessary headers
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Authorization': authHeader,
  },
});

// Function to join a tournament by sending a POST request
export const joinTournament = async (id) => {
  try {
    // Send a POST request to the endpoint that adds the player to the tournament
    const response = await axiosInstance.post(`/tournaments/${id}/players`);
    return response.data; // Optionally return response data, if needed
  } catch (error) {
    console.error(`Error joining tournament with ID ${id}:`, error);
    throw error; // Re-throw the error so it can be handled in the component
  }
};
