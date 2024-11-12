// src/api/bracketAPI.js
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

// Generate all matches for a tournament (POST /tournaments/{tournamentId}/generate)
export const generateBracketMatches = async (tournamentId) => {
  try {
    const response = await axiosInstance.post(`/tournaments/${tournamentId}/generate`);
    return response.data;
  } catch (error) {
    console.error(`Error generating bracket matches for tournament with ID ${tournamentId}:`, error);
    throw error;
  }
};

// Fetch all matches in a tournament bracket (GET /tournaments/{tournamentId}/matches)
export const fetchBracketMatches = async (tournamentId) => {
  try {
    const response = await axiosInstance.get(`/tournaments/${tournamentId}/matches`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching bracket matches for tournament with ID ${tournamentId}:`, error);
    throw error;
  }
};

// Update the winner of a specific match in the bracket (PUT /tournaments/{tournamentId}/matches/{matchId})
export const updateBracketMatchWinner = async (tournamentId, matchId, winnerId) => {
  try {
    const response = await axiosInstance.put(`/tournaments/${tournamentId}/matches/${matchId}`, {
      winnerId,
    });
    return response.data;
  } catch (error) {
    console.error(`Error updating bracket match winner in tournament ${tournamentId} with match ID ${matchId}:`, error);
    throw error;
  }
};
