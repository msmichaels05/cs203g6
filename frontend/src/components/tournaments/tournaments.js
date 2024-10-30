import React, { useState } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import './tournaments.css'; // Importing CSS for styling
import CustomNavbar from '../navbar/CustomNavbar';
import { Link } from "react-router-dom";

let initialTournaments = [
  { id: 1, name: "Champions Cup", ELOrequirement: 1500, maxPlayers: 16, location: "New York", startDate: "2024-06-01", endDate: "2024-06-07", registrationEndDate: "2024-05-30" },
  { id: 2, name: "Elite Tournament", ELOrequirement: 1600, maxPlayers: 32, location: "Los Angeles", startDate: "2024-07-10", endDate: "2024-07-17", registrationEndDate: "2024-07-05" },
  { id: 3, name: "Amateur Showdown", ELOrequirement: 1400, maxPlayers: 8, location: "Chicago", startDate: "2024-08-01", endDate: "2024-08-07", registrationEndDate: "2024-07-28" },
];

const isAdmin = true; // Change this flag to false for player view

const Tournament = () => {
  const [tournaments, setTournaments] = useState(initialTournaments);
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(''); // 'add', 'view', 'edit', or 'delete'
  const [selectedTournament, setSelectedTournament] = useState(null);
  const [coverImage, setCoverImage] = useState(null); // State for the cover image

  const isPowerOfTwo = (num) => {
    return Math.log2(num) % 1 === 0;
  };

  const handleAddTournament = (newTournament) => {
    // Assign a unique ID for the new tournament and add the cover image
    const tournamentWithId = {
      ...newTournament,
      id: Date.now(),
      coverImage: coverImage ? URL.createObjectURL(coverImage) : null, // Store cover image URL
    };
    setTournaments([...tournaments, tournamentWithId]);
    setCoverImage(null); // Clear the cover image after saving
    setShowModal(false); // Close the modal after adding
  };


  // Open modal with correct type and tournament data
  const handleOpenModal = (type, tournament = null) => {
    setModalType(type);
    setSelectedTournament(tournament || { name: '', ELOrequirement: '', maxPlayers: '', location: '', startDate: '', endDate: '', registrationEndDate: '' });
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedTournament(null);
  };

  // Save changes for editing or adding
  const handleSaveChanges = () => {
    if (modalType === 'edit') {
      if (!isPowerOfTwo(selectedTournament.maxPlayers)) {
        alert('Max players must be a power of 2 (e.g., 2, 4, 8, 16, etc.)');
        return;
      }
      setTournaments(tournaments.map(t => t.id === selectedTournament.id ? selectedTournament : t));
      console.log("Tournament updated:", selectedTournament);
    } else if (modalType === 'add') {
      const newTournament = { ...selectedTournament, id: Date.now() };
      setTournaments([...tournaments, newTournament]);
      console.log("Tournament added:", newTournament);
    }
    handleCloseModal();
  };

  // Delete selected tournament
  const handleDeleteTournament = () => {
    setTournaments(tournaments.filter(t => t.id !== selectedTournament.id));
    console.log("Tournament deleted:", selectedTournament);
    handleCloseModal();
  };

  return (
    <div>
      <CustomNavbar />
      <div className="container">
        {isAdmin && (
          <Button variant="success" onClick={() => handleOpenModal('add')} className="add-tournament-btn">
            Add New Tournament
          </Button>
        )}

        <div className="tournament-grid">
          {tournaments.map((tournament) => (
            <div key={tournament.id} className="tournament-card">
              <img src={tournament.coverImage || "https://via.placeholder.com/300x200"} alt="Tournament Cover" />
              <h3>{tournament.name}</h3>
              <p><strong>ELO Requirement:</strong> {tournament.ELOrequirement}</p>
              <p><strong>Location:</strong> {tournament.location}</p>
              <p><strong>Max Players:</strong> {tournament.maxPlayers}</p>

              <div className="btn-container">
                {isAdmin ? (
                  <>
                    <Button variant="info">
                      <Link
                        to="/tournament/view"
                        state={{ tournamentId: tournament.id }}
                        style={{ color: 'white', textDecoration: 'none' }}
                      >
                        View
                      </Link>
                    </Button>
                    <Button variant="warning" onClick={() => handleOpenModal('edit', tournament)}>Edit</Button>
                    <Button variant="danger" onClick={() => handleOpenModal('delete', tournament)}>Delete</Button>
                  </>
                ) : (
                  <form action={`/tournament/register/${tournament.id}`} method="GET">
                    <button type="submit" className="btn register-btn">
                      Register
                    </button>
                  </form>
                )}
              </div>
            </div>
          ))}
        </div>

        {/* Modal for Add, View, Edit, and Delete */}
        <Modal show={showModal} onHide={handleCloseModal}>
          <Modal.Header closeButton>
            <Modal.Title>
              {modalType === 'view' ? 'View Tournament' :
                modalType === 'edit' ? 'Edit Tournament' :
                  modalType === 'add' ? 'Add Tournament' :
                    'Confirm Delete'}
            </Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {modalType === 'view' && selectedTournament && (
              <div>
                <p><strong>Name:</strong> {selectedTournament?.name}</p>
                <p><strong>ELO Requirement:</strong> {selectedTournament?.ELOrequirement}</p>
                <p><strong>Location:</strong> {selectedTournament?.location}</p>
                <p><strong>Max Players:</strong> {selectedTournament?.maxPlayers}</p>
                <p><strong>Start Date:</strong> {selectedTournament?.startDate}</p>
                <p><strong>End Date:</strong> {selectedTournament?.endDate}</p>
                <p><strong>Registration End Date:</strong> {selectedTournament?.registrationEndDate}</p>
              </div>
            )}
            {(modalType === 'edit' || modalType === 'add') && selectedTournament && (
              <Form>
                {/* Other fields for tournament data */}
                <Form.Group controlId="formCoverImage">
                  <Form.Label>Cover Image</Form.Label>
                  <Form.Control
                    type="file"
                    accept="image/*"
                    onChange={(e) => setCoverImage(e.target.files[0])} // Store the selected file
                  />
                </Form.Group>

                {/* Preview the selected image */}
                {coverImage && (
                  <div className="cover-image-preview">
                    <img
                      src={URL.createObjectURL(coverImage)}
                      alt="Cover Preview"
                      style={{ width: "100%", marginTop: "10px" }}
                    />
                  </div>
                )}
                <Form.Group controlId="formName">
                  <Form.Label>Name</Form.Label>
                  <Form.Control
                    type="text"
                    value={selectedTournament?.name || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, name: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formELO">
                  <Form.Label>ELO Requirement</Form.Label>
                  <Form.Control
                    type="number"
                    value={selectedTournament?.ELOrequirement || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, ELOrequirement: parseInt(e.target.value) })}
                  />
                </Form.Group>
                <Form.Group controlId="formMaxPlayers">
                  <Form.Label>Max Players (must be a power of 2)</Form.Label>
                  <Form.Control
                    type="number"
                    value={selectedTournament?.maxPlayers || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, maxPlayers: parseInt(e.target.value) })}
                  />
                </Form.Group>
                <Form.Group controlId="formLocation">
                  <Form.Label>Location</Form.Label>
                  <Form.Control
                    type="text"
                    value={selectedTournament?.location || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, location: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formStartDate">
                  <Form.Label>Start Date</Form.Label>
                  <Form.Control
                    type="date"
                    value={selectedTournament?.startDate || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, startDate: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formEndDate">
                  <Form.Label>End Date</Form.Label>
                  <Form.Control
                    type="date"
                    value={selectedTournament?.endDate || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, endDate: e.target.value })}
                  />
                </Form.Group>
                <Form.Group controlId="formRegistrationEndDate">
                  <Form.Label>Registration End Date</Form.Label>
                  <Form.Control
                    type="date"
                    value={selectedTournament?.registrationEndDate || ''}
                    onChange={(e) => setSelectedTournament({ ...selectedTournament, registrationEndDate: e.target.value })}
                  />
                </Form.Group>
              </Form>
            )}
            {modalType === 'delete' && selectedTournament && (
              <p>Are you sure you want to delete the tournament "{selectedTournament?.name}"?</p>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Close
            </Button>
            {(modalType === 'edit' || modalType === 'add') && (
              <Button variant="primary" onClick={handleSaveChanges}>
                Save Changes
              </Button>
            )}
            {modalType === 'delete' && (
              <Button variant="danger" onClick={handleDeleteTournament}>
                Confirm Delete
              </Button>
            )}
          </Modal.Footer>
        </Modal>

      </div>
    </div>
  );
};

export default Tournament;
