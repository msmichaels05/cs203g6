import React, { useState } from 'react';
import { useNavigate } from "react-router-dom";
import './register.css';

const Register = () => {
  const [step, setStep] = useState(1); // Track registration steps
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [age, setAge] = useState('');
  const [gender, setGender] = useState('');
  const [elo, setElo] = useState(1500);
  const [matchesPlayed, setMatchesPlayed] = useState(0);
  const [matchesWon, setMatchesWon] = useState(0);
  const [userId, setUserId] = useState(null);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();

  // Handle initial user registration
  const handleRegisterUser = async (event) => {
    event.preventDefault();

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
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

      if (response.status === 201) {
        const data = await response.json(); // Assume userId is returned in the response
        setUserId(data.userId);
        setSuccessMessage("User registration successful!");
        setError('');
        setStep(2); // Move to player registration step
      } else {
        setError('User registration failed. Please try again.');
      }
    } catch (error) {
      setError('An error occurred. Please try again later.');
    }
  };

  // Handle player registration
  const handleRegisterPlayer = async (event) => {
    event.preventDefault();
    
    const credentials = btoa(`${username}:${password}`);

    try {
      const response = await fetch(`http://localhost:8080/users/${userId}/players`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Basic ${credentials}`,
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
        navigate('/'); // Redirect after successful player registration
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
        {step === 1 ? (
          <>
            <h2>Create an Account</h2>
            {successMessage && <p className="success-message">{successMessage}</p>}
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleRegisterUser}>
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
          </>
        ) : (
          <>
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
          </>
        )}
      </div>
    </div>
  );
};

export default Register;
