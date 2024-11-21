// src/api/adminApi.js
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

// Fetch all admins (GET /admin or /admins)
export const fetchAdmins = async () => {
  try {
    const response = await axiosInstance.get('/admin'); // Change to `/admins` if that’s your endpoint
    return response.data; // Ensure the backend returns the phone field as well
  } catch (error) {
    console.error('Error fetching admins:', error);
    throw error;
  }
};

// Add a new admin (POST /admin or /admins)
export const addAdmin = async (adminData) => {
  try {
    const response = await axiosInstance.post('/admin', adminData); // Adjust to `/admins` if needed
    return response.data;
  } catch (error) {
    console.error('Error adding admin:', error);
    throw error;
  }
};

// Edit an admin by ID (PUT /admin/{id} or /admins/{id})
export const editAdmin = async (id, adminData) => {
  try {
    const response = await axiosInstance.put(`/admin/${id}`, adminData); // Adjust to `/admins/${id}` if needed
    return response.data;
  } catch (error) {
    console.error(`Error editing admin with ID ${id}:`, error);
    throw error;
  }
};

// Delete an admin by ID (DELETE /admin/{id} or /admins/{id})
export const deleteAdmin = async (id) => {
  try {
    await axiosInstance.delete(`/admin/${id}`); // Adjust to `/admins/${id}` if needed
  } catch (error) {
    console.error(`Error deleting admin with ID ${id}:`, error);
    throw error;
  }
};
