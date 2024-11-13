// src/components/guest_tournament/guest_tournament.js
import React, { useEffect, useState } from 'react';
import { fetchTournaments } from '../../api/tournamentApi'; // Ensure the path to tournamentApi is correct
import CustomNavbar from '../navbar/CustomNavbar';  // Import CustomNavbar
import '../tournaments/tournaments.css'; // Make sure this path is correct


const GuestTournament = () => {
  const [tournaments, setTournaments] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadTournaments = async () => {
      try {
        const tournamentsData = await fetchTournaments();
        setTournaments(tournamentsData);
      } catch (err) {
        console.error('Failed to fetch tournaments:', err);
        setError('Could not load tournaments.');
      }
    };
    loadTournaments();
  }, []);

  return (
    <div>
      <CustomNavbar />

     
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <div className="tournament-list">
        {tournaments.map(tournament => (
          <div className="tournament-card" key={tournament.id}>
            <h3>{tournament.name}</h3>
            <p>Location: {tournament.location}</p>
            <p>Date: {new Date(tournament.date).toLocaleDateString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default GuestTournament;
