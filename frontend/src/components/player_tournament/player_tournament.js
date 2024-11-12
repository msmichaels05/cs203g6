// PlayerTournament.js
import React, { useState, useEffect } from 'react';
import { Modal, Button, Card } from 'react-bootstrap';
import { Link } from "react-router-dom";
import '../tournaments/tournaments.css'; // Make sure this path is correct
import PlayerNavbar from '../navbar/PlayerNavbar';
import { fetchTournaments } from '../../api/tournamentApi';
import { joinTournament } from '../../api/player_tournamentAPI';

const PlayerTournament = () => {
  const [tournaments, setTournaments] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedTournament, setSelectedTournament] = useState(null);
  const currentDate = new Date();

  // Fetch tournament data from the backend
  useEffect(() => {
    const getTournaments = async () => {
      try {
        const data = await fetchTournaments();
        setTournaments(data);
      } catch (error) {
        console.error("Failed to fetch tournaments:", error);
      }
    };

    getTournaments();
  }, []);

  const isRegistrationClosed = (registrationEndDate) => {
    return currentDate > new Date(registrationEndDate);
  };

  const canViewMatch = (startDate) => {
    return currentDate >= new Date(startDate);
  };

  const handleRegister = (tournament) => {
    setSelectedTournament(tournament);
    setShowModal(true);
  };

  const handleConfirmRegistration = async () => {
    try {
      await joinTournament(selectedTournament.id);
      alert(`Successfully registered for ${selectedTournament.name}`);
      setShowModal(false);
      setSelectedTournament(null);
    } catch (error) {
      console.error("Error registering for tournament:", error);
      alert("Registration unsuccessful. Please try again.");
    }
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedTournament(null);
  };

  return (
    <div className="player-tournament">
      <PlayerNavbar />
      <div className="container mt-4">
        <div className="tournament-grid">
          {tournaments.map((tournament) => (
            <Card key={tournament.id} className="tournament-card">
              <Card.Img variant="top" src={tournament.coverImage || "https://via.placeholder.com/300x200"} />
              <Card.Body>
                <Card.Title>{tournament.name}</Card.Title>
                <Card.Text><strong>ELO Requirement:</strong> {tournament.elorequirement}</Card.Text>
                <Card.Text><strong>Location:</strong> {tournament.location}</Card.Text>
                <Card.Text><strong>Max Players:</strong> {tournament.maxPlayers}</Card.Text>
                <Card.Text><strong>Registration End Date:</strong> {tournament.registrationEndDate}</Card.Text>

                <div className="btn-container d-flex justify-content-between">
                  {!isRegistrationClosed(tournament.registrationEndDate) && (
                    <Button variant="primary" className="register-btn" onClick={() => handleRegister(tournament)}>
                      Register
                    </Button>
                  )}

                  {canViewMatch(tournament.startDate) && (
                    <Button variant="info">
                      <Link to={`/tournament/view/${tournament.id}`} className="text-white text-decoration-none">
                        View Match
                      </Link>
                    </Button>
                  )}
                </div>
              </Card.Body>
            </Card>
          ))}
        </div>

        {/* Modal to confirm registration */}
        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Registration</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            Are you sure you want to register for the tournament: {selectedTournament?.name}?
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            <Button variant="primary" onClick={handleConfirmRegistration}>
              Confirm Registration
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default PlayerTournament;
