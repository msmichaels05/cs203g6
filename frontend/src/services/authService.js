import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth'; // Change this to match your backend login endpoint

export const login = async (email, password) => {
  try {
    const response = await axios.post(`${API_URL}/login`, { email, password });
    // Assuming the backend sends a token on successful login
    if (response.data.token) {
      localStorage.setItem('token', response.data.token); // Store the token in localStorage or cookies
      return { success: true };
    }
    return { success: false };
  } catch (error) {
    console.error('Login error:', error);
    return { success: false };
  }
};
