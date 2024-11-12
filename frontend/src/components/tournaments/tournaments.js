import React, { useState, useEffect } from 'react';
import { Modal, Button, Form, Card } from 'react-bootstrap';
import './tournaments.css';
import AdminNavbar from '../navbar/AdminNavbar';
import PlayerNavbar from '../navbar/PlayerNavbar';

import { Link } from "react-router-dom";
import { fetchTournaments, addTournament, editTournament, deleteTournament } from '../../api/tournamentApi';

const Tournament = () => {
  const [tournaments, setTournaments] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState('');
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [coverImage, setCoverImage] = useState(null);
  const currentUser = { role: "admin" };
  const isAdmin = currentUser.role === "admin";
  
  const isPowerOfTwo = (num) => Math.log2(num) % 1 === 0;

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await fetchTournaments();
        setTournaments(data);
      } catch (error) {
        console.error("Error fetching tournaments:", error);
      }
    };
    fetchData();
  }, []);

  const handleAddTournament = async () => {
    const formData = new FormData();
    formData.append('name', selectedTournament.name);
    formData.append('elorequirement', selectedTournament.elorequirement);
    formData.append('maxPlayers', selectedTournament.maxPlayers);
    formData.append('startDate', selectedTournament.startDate);
    formData.append('endDate', selectedTournament.endDate);
    formData.append('gender', selectedTournament.gender);
    formData.append('registrationEndDate', selectedTournament.registrationEndDate);
    formData.append('location', selectedTournament.location);
    formData.append('description', selectedTournament.description);
    
    if (coverImage) {
      formData.append('coverImage', coverImage); // Image upload
    }
    
    try {
      const newTournament = await addTournament(formData);
      setTournaments([...tournaments, newTournament]);
      handleCloseModal();
    } catch (error) {
      console.error("Error adding tournament:", error);
    }
  };

  const handleOpenModal = (type, tournament = null) => {
    setModalType(type);
    setSelectedTournament(
      tournament || {
        name: '',
        elorequirement: '',
        maxPlayers: '',
        playerCount: 0,
        startDate: '',
        endDate: '',
        gender: '',
        registrationEndDate: '',
        location: '',
        description: '',
      }
    );
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedTournament(null);
    setCoverImage(null);
  };

  const handleSaveChanges = async () => {
    if (modalType === 'edit') {
      if (!isPowerOfTwo(selectedTournament.maxPlayers)) {
        alert('Max players must be a power of 2 (e.g., 2, 4, 8, 16, etc.)');
        return;
      }
      try {
        await editTournament(selectedTournament.id, selectedTournament);
        setTournaments(
          tournaments.map((t) => (t.id === selectedTournament.id ? selectedTournament : t))
        );
        handleCloseModal();
      } catch (error) {
        console.error("Error editing tournament:", error);
      }
    } else if (modalType === 'add') {
      handleAddTournament();
    }
  };

  const handleDeleteTournament = async () => {
    try {
      await deleteTournament(selectedTournament.id);
      setTournaments(tournaments.filter((t) => t.id !== selectedTournament.id));
      handleCloseModal();
    } catch (error) {
      console.error("Error deleting tournament:", error);
    }
  };

  const handleCoverImageChange = (e) => {
    setCoverImage(e.target.files[0]);
  };

  return (
    <div>
      <CustomNavbar />
      {isAdmin ? <AdminNavbar /> : <PlayerNavbar />}
      <div className="container mt-4">
        {isAdmin && (
          <Button variant="success" onClick={() => handleOpenModal('add')} className="add-tournament-btn mb-4">
            Add New Tournament
          </Button>
        )}

        <div className="tournament-grid">
          {tournaments.map((tournament) => (
            <Card key={tournament.id} className="tournament-card">
              <Card.Img variant="top" src={tournament.coverImage || "https://via.placeholder.com/300x200"} />
              <Card.Body>
                <Card.Title>{tournament.name}</Card.Title>
                <Card.Text><strong>ELO Requirement:</strong> {tournament.elorequirement}</Card.Text>
                <Card.Text><strong>Location:</strong> {tournament.location}</Card.Text>
                <Card.Text><strong>Max Players:</strong> {tournament.maxPlayers}</Card.Text>

                <div className="btn-container d-flex justify-content-between">
                  <Button variant="info">
                    <Link to={`/tournament/${tournament.id}`} className="text-white text-decoration-none">
                      View
                    </Link>
                  </Button>
                  <Button variant="warning" onClick={() => handleOpenModal('edit', tournament)}>Edit</Button>
                  <Button variant="danger" onClick={() => handleOpenModal('delete', tournament)}>Delete</Button>
                </div>
              </Card.Body>
            </Card>
          ))}
        </div>

        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>{modalType === 'add' ? 'Add Tournament' : modalType === 'edit' ? 'Edit Tournament' : 'Confirm Delete'}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {(modalType === 'edit' || modalType === 'add') && (
              <Form>
                <Form.Group controlId="formName" className="mb-3">
                  <Form.Label>Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={selectedTournament?.name || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, name: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formELO" className="mb-3">
                  <Form.Label>ELO Requirement</Form.Label>
                  <Form.Control
                    type="number"
                    value={selectedTournament?.elorequirement || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, elorequirement: parseInt(e.target.value) })}
                  />
                </Form.Group>
                <Form.Group controlId="formMaxPlayers" className="mb-3">
                  <Form.Label>Max Players</Form.Label>
                  <Form.Control
                    type="number"
                    value={selectedTournament?.maxPlayers || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, maxPlayers: parseInt(e.target.value) })}
                  />
                </Form.Group>
                <Form.Group controlId="formLocation" className="mb-3">
                  <Form.Label>Location</Form.Label>
                  <Form.Control
                    type="text"
                    value={selectedTournament?.location || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, location: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formDescription" className="mb-3">
                  <Form.Label>Description</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={selectedTournament?.description || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, description: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formCoverImage" className="mb-3">
                  <Form.Label>Cover Image</Form.Label>
                  <Form.Control
                    type="file"
                    onChange={handleCoverImageChange}
                  />
                </Form.Group>
                <Form.Group controlId="formStartDate" className="mb-3">
                  <Form.Label>Start Date</Form.Label>
                  <Form.Control
                    type="date"
                    value={selectedTournament?.startDate || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, startDate: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formEndDate" className="mb-3">
                  <Form.Label>End Date</Form.Label>
                  <Form.Control
                    type="date"
                    value={selectedTournament?.endDate || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, endDate: e.target.value })}
                  />
                </Form.Group>
              </Form>
            )}
            {modalType === 'delete' && (
              <p>Are you sure you want to delete the tournament: {selectedTournament?.name}?</p>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            {modalType === 'delete' ? (
              <Button variant="danger" onClick={handleDeleteTournament}>Delete</Button>
            ) : (
              <Button variant="primary" onClick={handleSaveChanges}>
                Save Changes
              </Button>
            )}
          </Modal.Footer>
        </Modal>
      </div>
    </div>
  );
};

export default Tournament;
