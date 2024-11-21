// src/api/playerApi.js
import axios from 'axios';

const API_BASE_URL = 'http://54.152.65.230:8080';
const authHeader = 'Basic ' + btoa('admin:goodpassword'); // Update with actual credentials if needed

// Create an axios instance with default headers
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Authorization': authHeader,
  },
});

// Fetch all players (GET /players)
export const fetchPlayers = async () => {
  try {
    const response = await axiosInstance.get('/players'); // Ensure endpoint is /players
    return response.data; // Ensure the backend returns relevant player fields
  } catch (error) {
    console.error('Error fetching players:', error);
    throw error;
  }
};

// Add a new player (POST /players)
export const addPlayer = async (playerData) => {
  try {
    const response = await axiosInstance.post('/players', playerData); // Ensure endpoint is /players
    return response.data;
  } catch (error) {
    console.error('Error adding player:', error);
    throw error;
  }
};

// Edit a player by ID (PUT /players/{id})
export const editPlayer = async (id, playerData) => {
  try {
    const response = await axiosInstance.put(`/players/${id}`, playerData); // Ensure endpoint is /players/{id}
    return response.data;
  } catch (error) {
    console.error(`Error editing player with ID ${id}:`, error);
    throw error;
  }
};

// Delete a player by ID (DELETE /players/{id})
export const deletePlayer = async (id) => {
  try {
    await axiosInstance.delete(`/players/${id}`); // Ensure endpoint is /players/{id}
  } catch (error) {
    console.error(`Error deleting player with ID ${id}:`, error);
    throw error;
  }
};
