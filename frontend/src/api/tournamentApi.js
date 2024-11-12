// src/api/tournamentApi.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';
const authHeader = 'Basic ' + btoa('admin:goodpassword'); // Encode credentials

// Create an axios instance with default headers
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Authorization': authHeader,
  },
});

// Now, use axiosInstance for all API calls, so headers are included in every request.

// Fetch all tournaments (GET /tournament)
export const fetchTournaments = async () => {
  try {
    const response = await axiosInstance.get('/tournaments');
    return response.data;
  } catch (error) {
    console.error('Error fetching tournaments:', error);
    throw error;
  }
};

// Get a specific tournament by ID (GET /tournaments/{id})
export const getTournament = async (id) => {
  try {
    const response = await axiosInstance.get(`/tournaments/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching tournament with ID ${id}:`, error);
    throw error;
  }
};

// Add a new tournament (POST /tournaments)
export const addTournament = async (tournamentData) => {
  try {
    const response = await axiosInstance.post('/tournaments', tournamentData);
    return response.data;
  } catch (error) {
    console.error('Error adding tournament:', error);
    throw error;
  }
};

// Edit a tournament by ID (PUT /tournaments/{id})
export const editTournament = async (id, tournamentData) => {
  try {
    const response = await axiosInstance.put(`/tournaments/${id}`, tournamentData);
    return response.data;
  } catch (error) {
    console.error(`Error editing tournament with ID ${id}:`, error);
    throw error;
  }
};

// Delete a tournament by ID (DELETE /tournaments/{id})
export const deleteTournament = async (id) => {
  try {
    await axiosInstance.delete(`/tournaments/${id}`);
  } catch (error) {
    console.error(`Error deleting tournament with ID ${id}:`, error);
    throw error;
  }
};


// Get players in a tournament (GET /tournaments/{id}/players)
export const getPlayersInTournament = async (id) => {
  try {
    const response = await axiosInstance.get(`/tournaments/${id}/players`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching players in tournament with ID ${id}:`, error);
    throw error;
  }
};

// Remove a player from a tournament (DELETE /tournaments/{tournamentId}/players/{playerId})
export const removePlayerFromTournament = async (tournamentId, playerId) => {
  try {
    await axiosInstance.delete(`/tournaments/${tournamentId}/players/${playerId}`);
  } catch (error) {
    console.error(`Error removing player with ID ${playerId} from tournament ${tournamentId}:`, error);
    throw error;
  }
};
