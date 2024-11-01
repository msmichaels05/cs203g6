// src/api/tournamentApi.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

// Fetch all tournaments (GET /tournament)
export const fetchTournaments = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/tournament`);
    return response.data;
  } catch (error) {
    console.error('Error fetching tournaments:', error);
    throw error;
  }
};

// Get a specific tournament by ID (GET /tournaments/{id})
export const getTournament = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/tournaments/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching tournament with ID ${id}:`, error);
    throw error;
  }
};

// Add a new tournament (POST /tournaments)
export const addTournament = async (tournamentData) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/tournaments`, tournamentData);
    return response.data;
  } catch (error) {
    console.error('Error adding tournament:', error);
    throw error;
  }
};

// Edit a tournament by ID (PUT /tournaments/{id})
export const editTournament = async (id, tournamentData) => {
  try {
    const response = await axios.put(`${API_BASE_URL}/tournaments/${id}`, tournamentData);
    return response.data;
  } catch (error) {
    console.error(`Error editing tournament with ID ${id}:`, error);
    throw error;
  }
};

// Delete a tournament by ID (DELETE /tournaments/{id})
export const deleteTournament = async (id) => {
  try {
    await axios.delete(`${API_BASE_URL}/tournaments/${id}`);
  } catch (error) {
    console.error(`Error deleting tournament with ID ${id}:`, error);
    throw error;
  }
};

// Add a player to a tournament (POST /tournaments/{id}/players)
export const joinTournament = async (id) => {
  try {
    await axios.post(`${API_BASE_URL}/tournaments/${id}/players`);
  } catch (error) {
    console.error(`Error joining tournament with ID ${id}:`, error);
    throw error;
  }
};

// Get players in a tournament (GET /tournaments/{id}/players)
export const getPlayersInTournament = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/tournaments/${id}/players`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching players in tournament with ID ${id}:`, error);
    throw error;
  }
};

// Remove a player from a tournament (DELETE /tournaments/{tournamentId}/players/{playerId})
export const removePlayerFromTournament = async (tournamentId, playerId) => {
  try {
    await axios.delete(`${API_BASE_URL}/tournaments/${tournamentId}/players/${playerId}`);
  } catch (error) {
    console.error(`Error removing player with ID ${playerId} from tournament ${tournamentId}:`, error);
    throw error;
  }
};
