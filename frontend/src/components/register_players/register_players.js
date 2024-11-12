import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import './register_players.css';

const RegisterPlayers = ({ userId }) => {
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [age, setAge] = useState('');
  const [gender, setGender] = useState('');
  const [elo, setElo] = useState(1500); // Default starting ELO
  const [matchesPlayed, setMatchesPlayed] = useState(0); // Default to 0
  const [matchesWon, setMatchesWon] = useState(0); // Default to 0
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  const handleRegisterPlayer = async (event) => {
    event.preventDefault();
  
    try {
      const token = localStorage.getItem('authToken'); // Replace 'authToken' with the actual key if different
      const response = await fetch(`http://localhost:8080/users/${userId}/players`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`, // Include token in Authorization header
        },
        body: JSON.stringify({
          name,
          phoneNumber,
          age: parseInt(age, 10),
          gender,
          elo: parseInt(elo, 10),
          matchesPlayed: parseInt(matchesPlayed, 10),
          matchesWon: parseInt(matchesWon, 10),
        }),
      });
  
      if (response.ok) {
        setSuccessMessage("Player registration successful!");
        setError('');
        navigate('/');
      } else if (response.status === 409) {
        setError("A player with this phone number or name already exists.");
      } else {
        const errorData = await response.json();
        setError(`Player registration failed: ${errorData.message || 'Please try again.'}`);
      }
    } catch (error) {
      console.error('Error during registration:', error);
      setError('An error occurred. Please try again later.');
    }
  };
  

  return (
    <div className="register-page">
      <div className="register-container">
        <div className="tennis-ball"></div>
        <h2>Register Player</h2>
        {successMessage && <p className="success-message">{successMessage}</p>}
        {error && <p className="error-message">{error}</p>}
        <form onSubmit={handleRegisterPlayer}>
          <div className="form-group">
            <label>Name:</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
              maxLength="100"
            />
          </div>
          <div className="form-group">
            <label>Phone Number:</label>
            <input
              type="tel"
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
              required
              minLength="8"
              maxLength="16"
            />
          </div>
          <div className="form-group">
            <label>Age:</label>
            <input
              type="number"
              value={age}
              onChange={(e) => setAge(e.target.value)}
              required
              min="1"
            />
          </div>
          <div className="form-group">
            <label>Gender:</label>
            <select value={gender} onChange={(e) => setGender(e.target.value)} required>
              <option value="" disabled>Select Gender</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
              <option value="other">Other</option>
            </select>
          </div>
          <div className="form-group">
            <label>ELO (Starting ELO):</label>
            <input
              type="number"
              value={elo}
              onChange={(e) => setElo(e.target.value)}
              required
              min="0"
            />
          </div>
          <div className="form-group">
            <label>Matches Played:</label>
            <input
              type="number"
              value={matchesPlayed}
              onChange={(e) => setMatchesPlayed(e.target.value)}
              required
              min="0"
            />
          </div>
          <div className="form-group">
            <label>Matches Won:</label>
            <input
              type="number"
              value={matchesWon}
              onChange={(e) => setMatchesWon(e.target.value)}
              required
              min="0"
            />
          </div>

          <button type="submit">Register Player</button>
        </form>
      </div>
    </div>
  );
};

export default RegisterPlayers;
