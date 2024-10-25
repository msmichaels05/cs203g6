import React, { useState } from 'react';
import './tournaments_create.css'; // Importing CSS for styling

const CreateTournament = () => {
  const [tournamentName, setTournamentName] = useState('');
  const [maxPlayers, setMaxPlayers] = useState('');
  const [location, setLocation] = useState('');
  const [startDate, setStartDate] = useState(new Date().toISOString().substring(0, 10));
  const [endDate, setEndDate] = useState('');
  const [registrationEndDate, setRegistrationEndDate] = useState('');

  const isPowerOfTwo = (num) => {
    return (Math.log2(num) % 1 === 0); // Checks if num is a power of 2
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!isPowerOfTwo(maxPlayers)) {
      alert("Max players must be a power of 2 (e.g., 2, 4, 8, 16, 32, etc.)");
      return;
    }
    console.log({
      tournamentName,
      maxPlayers,
      location,
      startDate,
      endDate,
      registrationEndDate,
    });
    // Here you can add logic to send data to the backend or any further logic
  };

  return (
    <div className="create-tournament-container">
      <h2>Create Tournament</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Tournament Name:</label>
          <input
            type="text"
            value={tournamentName}
            onChange={(e) => setTournamentName(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Max Players (must be a power of 2):</label>
          <input
            type="number"
            value={maxPlayers}
            onChange={(e) => setMaxPlayers(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Location (Google API):</label>
          <input
            type="text"
            value={location}
            onChange={(e) => setLocation(e.target.value)}
            placeholder="Enter location"
            required
          />
        </div>

        <div className="form-group">
          <label>Start Date:</label>
          <input
            type="date"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
            required
            min={new Date().toISOString().substring(0, 10)}
          />
        </div>

        <div className="form-group">
          <label>End Date:</label>
          <input
            type="date"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
            required
            min={startDate}
          />
        </div>

        <div className="form-group">
          <label>Registration End Date:</label>
          <input
            type="date"
            value={registrationEndDate}
            onChange={(e) => setRegistrationEndDate(e.target.value)}
            required
            min={new Date().toISOString().substring(0, 10)}
          />
        </div>

        <button type="submit" className="btn create-btn">
          Create Tournament
        </button>
      </form>
    </div>
  );
};

export default CreateTournament;
