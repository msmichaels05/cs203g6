import React, { useState, useEffect } from 'react';
import { Modal, Button, Card } from 'react-bootstrap';
import { Link } from "react-router-dom";
import '../tournaments/tournaments.css';
import PlayerNavbar from '../navbar/PlayerNavbar';

const PlayerTournament = () => {
  const [tournaments, setTournaments] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [selectedTournament, setSelectedTournament] = useState(null);
  const currentUser = { role: "player" };
  const currentDate = new Date();

  // Hardcoded tournament data
  const hardcodedTournaments = [
    {
      id: 1,
      name: "Tournament A",
      coverImage: "https://via.placeholder.com/300x200",
      elorequirement: 1200,
      location: "Location A",
      maxPlayers: 16,
      registrationEndDate: "2024-11-01T23:59:59", // Registration closed
      startDate: "2024-11-05T10:00:00",
    },
    {
      id: 2,
      name: "Tournament B",
      coverImage: "https://via.placeholder.com/300x200",
      elorequirement: 1500,
      location: "Location B",
      maxPlayers: 16,
      registrationEndDate: "2024-11-15T23:59:59", // Registration still open
      startDate: "2024-11-20T10:00:00",
    }
  ];

  const isRegistered = (startDate, registrationEndDate) => {
    // Check if the current date is within the registration period
    const registrationStart = new Date(registrationEndDate);
    return currentDate >= registrationStart && currentDate <= new Date(startDate);
  };

  const canViewMatch = (startDate) => {
    // Can only view the match if the registration date has passed
    return currentDate >= new Date(startDate);
  };

  useEffect(() => {
    // Use hardcoded tournament data
    setTournaments(hardcodedTournaments);
  }, []);

  const handleOpenModal = (tournament) => {
    setSelectedTournament(tournament);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedTournament(null);
  };

  return (
    <div>
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
                  {isRegistered(tournament.startDate, tournament.registrationEndDate) ? (
                    <Button variant="primary" className="register-btn" disabled>
                      Registration Closed
                    </Button>
                  ) : (
                    <Button variant="primary" className="register-btn">
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
            <Button variant="primary" onClick={() => alert('Registration Successful')}>
              Confirm Registration
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default PlayerTournament;
