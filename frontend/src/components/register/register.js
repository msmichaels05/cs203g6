import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import './register.css';

const Register = () => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (event) => {
    event.preventDefault();

    // Client-side validation to ensure password confirmation
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
      // POST request to register a new user
      const response = await fetch('http://localhost:8080/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email,
          username,
          password,
        }),
      });

      if (response.status === 201) {  // Check for HTTP 201 Created
        const userData = await response.json(); // Assuming the server returns user data including the user ID
        const userId = userData.id;

        setSuccessMessage("Registration successful!");
        setError('');
        
        // Navigate to RegisterPlayers page with userId, username, and password as state parameters
        navigate('/register/players', { state: { userId, username, password } });
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
        <div className="tennis-ball"></div>
        <h2>Create an Account</h2>
        {successMessage && <p className="success-message">{successMessage}</p>}
        {error && <p className="error-message">{error}</p>}
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
            <label>Username:</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
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

          <button type="submit">Register</button>
        </form>
        <div className="additional-options">
          <span onClick={() => navigate("/login")} className="link-text">
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
