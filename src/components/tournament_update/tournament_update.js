import React, { useState, useEffect } from 'react';
import './tournament_update.css'; // Importing CSS for styling

const TournamentUpdate = ({ tournament }) => {
  // State variables to store form values
  const [tournamentName, setTournamentName] = useState('');
  const [maxPlayers, setMaxPlayers] = useState('');
  const [location, setLocation] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [registrationEndDate, setRegistrationEndDate] = useState('');

  useEffect(() => {
    // Pre-fill the form with the existing tournament data (assuming tournament prop is passed)
    if (tournament) {
      setTournamentName(tournament.name);
      setMaxPlayers(tournament.maxPlayers);
      setLocation(tournament.location);
      setStartDate(tournament.startDate);
      setEndDate(tournament.endDate);
      setRegistrationEndDate(tournament.registrationEndDate);
    }
  }, [tournament]);

  const isPowerOfTwo = (num) => {
    return Math.log2(num) % 1 === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!isPowerOfTwo(maxPlayers)) {
      alert('Max players must be a power of 2 (e.g., 2, 4, 8, 16, 32, etc.)');
      return;
    }

    // Prepare the updated tournament data
    const updatedTournament = {
      name: tournamentName,
      maxPlayers,
      location,
      startDate,
      endDate,
      registrationEndDate,
    };

    console.log('Updated Tournament:', updatedTournament);
    // Send the updated tournament data to the backend (to be implemented)
  };

  return (
    <div className="update-tournament-container">
      <h2>Update Tournament</h2>
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

        <button type="submit" className="btn update-btn">
          Update Tournament
        </button>
      </form>
    </div>
  );
};

export default TournamentUpdate;
