// src/api/playerApi.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';
const authHeader = 'Basic ' + btoa('admin:goodpassword'); // Update with actual credentials if needed

// Create an axios instance with default headers
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Authorization': authHeader,
  },
  
});

// Fetch all players (GET /players)
export const fetchPlayers = async () => {
  try {
    const response = await axiosInstance.get('/players');
    return response.data; // Returns all players
  } catch (error) {
    console.error('Error fetching players:', error);
    if (error.response) {
      console.error('Response data:', error.response.data);
    }
    throw error;
  }
};

// Fetch player details by userId (GET /users/{userId}/players)
export const fetchPlayerDetails = async (userId) => {
  try {
    const response = await axiosInstance.get(`/users/${userId}/players`);
    console.log(response);
    return response.data; // Returns player details for the given userId
  } catch (error) {
    console.error('Error fetching player details:', error);
    if (error.response) {
      console.error('Response data:', error.response.data);
    }
    throw error;
  }
};


// Edit a player by playerId (PUT /users/{playerId}/players/{playerId})
export const editPlayer = async (playerId, updatedPlayer) => {
  try {
    const response = await axiosInstance.put(
      `/users/${playerId}/players/${playerId}`,
      updatedPlayer,
      {
        headers: {
          'Content-Type': 'application/json', // Ensure JSON content type
        },
      }
    );
    return response.data; // Returns the updated player data
  } catch (error) {
    console.error('Error editing player:', error);
    if (error.response) {
      console.error('Response data:', error.response.data);
    }
    throw error;
  }
};

// Delete a player by playerId (DELETE /users/{playerId}/players/{playerId})
export const deletePlayer = async (playerId) => {
  try {
    const response = await axiosInstance.delete(`/users/${playerId}/players/${playerId}`);
    return response.data; // Returns a confirmation message or status
  } catch (error) {
    console.error('Error deleting player:', error);
    if (error.response) {
      console.error('Response data:', error.response.data);
    }
    throw error;
  }
};
