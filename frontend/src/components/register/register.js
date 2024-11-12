import React, { useState } from 'react';
import { login } from '../../services/authService';
import { useNavigate } from "react-router-dom"; // Ensure this import is correct
import './register.css';

const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate(); // Correctly defining the navigate function

  const handleRegister = async (event) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
      const response = await login(email, password);
      if (response.success) {
        console.log('Registration successful');
        window.location.href = '/home';  // Or use navigate('/home') for programmatic navigation
      } else {
        setError('Registration failed. Please try again.');
      }
    } catch (error) {
      setError('An error occurred. Please try again later.');
    }
  };

  return (
    <div className="register-page">
      <div className="register-container">
        <div className="tennis-ball"></div> {/* Tennis ball graphic */}
        <h2>Create an Account</h2>
        <form onSubmit={handleRegister}>
          <div className="form-group">
            <label>Email:</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Confirm Password:</label>
            <input
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
          </div>

          {error && <div className="error">{error}</div>}
          <button type="submit">Register</button>
        </form>
        <div className="additional-options">
          <span onClick={() => navigate("/")} className="link-text">
            Login here
          </span>
          <span onClick={() => navigate("/")} className="link-text">
            Continue as Guest
          </span>
        </div>
      </div>
    </div>
  );
};

export default Register;
