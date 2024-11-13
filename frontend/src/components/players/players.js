import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Button, Form, Modal } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import AdminNavbar from '../navbar/AdminNavbar';
import PlayerNavbar from '../navbar/PlayerNavbar';
import { fetchPlayers, editPlayer, deletePlayer } from '../../api/playersAPI';

const Players = () => {
  const [players, setPlayers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(''); // 'edit'
  const [currentPlayer, setCurrentPlayer] = useState({});
  const [role, setRole] = useState(null);  // Store the user's role
  const [loading, setLoading] = useState(true);  // Loading state
  const [showDeleteModal, setShowDeleteModal] = useState(false);  // Delete confirmation modal state
  const [playerToDelete, setPlayerToDelete] = useState(null);  // Store the player to delete

  // Fetch user role from localStorage on component mount
  useEffect(() => {
    const storedRole = localStorage.getItem('role');  // Retrieve role from localStorage
    if (storedRole) {
      setRole(storedRole);  // Set the role if it exists
    }
  }, []);

  // Fetch players when the component mounts
  useEffect(() => {
    const loadPlayers = async () => {
      setLoading(true); // Set loading to true before API call
      try {
        const playersData = await fetchPlayers();
        console.log('Fetched players:', playersData); // Log fetched players
        setPlayers(playersData); // Set players state from backend data
      } catch (error) {
        console.error('Error fetching players:', error);
      } finally {
        setLoading(false); // Set loading to false after API call is complete
      }
    };

    loadPlayers();
  }, []);

  const handleSearch = (event) => setSearchTerm(event.target.value);

  const filteredPlayers = players.filter(player =>
    player.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Edit Player Modal
  const handleEditPlayer = (player) => {
    console.log('Editing player:', player); // Log the player to be edited
    setModalType('edit');
    setCurrentPlayer(player);
    setShowModal(true);
  };

  // Save Player (for editing)
  const handleSavePlayer = async () => {
    console.log('Saving player:', currentPlayer);
  
    if (!currentPlayer.id) {
      console.error("Error: playerId is undefined.");
      return;
    }
  
    setLoading(true);
    try {
      // Prepare updated player data, excluding `authorities` if present
      const updatedPlayer = {
        ...currentPlayer,
        user: { ...currentPlayer.user },
      };
      delete updatedPlayer.user.authorities; // Remove authorities field
  
      const savedPlayer = await editPlayer(currentPlayer.id, updatedPlayer);
      console.log('Updated player:', savedPlayer);
      setPlayers(players.map(p => (p.id === savedPlayer.id ? savedPlayer : p)));
      setShowModal(false);
    } catch (error) {
      console.error('Error saving player:', error);
      if (error.response) {
        console.error('Response data:', error.response.data);
      }
    } finally {
      setLoading(false);
    }
  };
  

  // Delete Player
  const handleDeletePlayer = (player) => {
    console.log('Player to delete:', player); // Log the player ID to delete
    setPlayerToDelete(player); // Store the player to be deleted
    setShowDeleteModal(true); // Show the delete confirmation modal
  };

  const confirmDelete = async () => {
    console.log('Confirming delete for player ID:', playerToDelete.id); // Log the ID of the player to be deleted
    setLoading(true); // Set loading true during API call
    try {
      await deletePlayer(playerToDelete.id); // Pass playerId to deletePlayer
      console.log('Player deleted:', playerToDelete.id); // Log the successful deletion
      setPlayers(players.filter(player => player.id !== playerToDelete.id)); // Update local state
      setShowDeleteModal(false); // Close the modal after deletion
    } catch (error) {
      console.error('Error deleting player:', error);
    } finally {
      setLoading(false);  // Set loading false after the API call completes
    }
  };

  if (loading) {
    return <div>Loading...</div>;  // Loading indicator until role is fetched and players are loaded
  }

  return (
    <>
      {role === "ROLE_ADMIN" ? <AdminNavbar /> : <PlayerNavbar />}

      <Container className="mt-4">
        <Row>
          <Col className="d-flex justify-content-between align-items-center mb-3">
            <h4>All Players - AmateurAces</h4>
          </Col>
        </Row>
        <Row>
          <Col>
            <Form>
              <Form.Group controlId="search">
                <Form.Control
                  type="text"
                  placeholder="Search by player name"
                  value={searchTerm}
                  onChange={handleSearch}
                />
              </Form.Group>
            </Form>
          </Col>
        </Row>
        <Row>
          <Col>
            <Card className="mt-3">
              <Table striped bordered hover responsive>
                <thead>
                  <tr>
                    <th>Player Name</th>
                    <th>Phone Number</th>
                    <th>Age</th>
                    <th>Gender</th>
                    <th>Matches Played</th>
                    <th>Matches Won</th>
                    {role === "ROLE_ADMIN" && <th>Actions</th>}
                  </tr>
                </thead>
                <tbody>
                  {filteredPlayers.length > 0 ? (
                    filteredPlayers.map((player) => (
                      <tr key={player.id}>
                        <td>{player.name}</td>
                        <td>{player.phoneNumber}</td>
                        <td>{player.age}</td>
                        <td>{player.gender}</td>
                        <td>{player.matchesPlayed}</td>
                        <td>{player.matchesWon}</td>
                        {role === "ROLE_ADMIN" && (
                          <td>
                            <Button variant="warning" size="sm" className="me-2" onClick={() => handleEditPlayer(player)}>
                              <FaEdit />
                            </Button>
                            <Button variant="danger" size="sm" onClick={() => handleDeletePlayer(player)}>
                              <FaTrash />
                            </Button>
                          </td>
                        )}
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={role === "ROLE_ADMIN" ? "7" : "6"} className="text-center">
                        No players found
                      </td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card>
          </Col>
        </Row>

        {/* Modal for Edit Player */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Edit Player</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  value={currentPlayer.name || ''}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, name: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="phoneNumber">
                <Form.Label>Phone Number</Form.Label>
                <Form.Control
                  type="text"
                  value={currentPlayer.phoneNumber || ''}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, phoneNumber: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="age">
                <Form.Label>Age</Form.Label>
                <Form.Control
                  type="number"
                  value={currentPlayer.age || ''}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, age: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="gender">
                <Form.Label>Gender</Form.Label>
                <Form.Control
                  as="select"
                  value={currentPlayer.gender || ''}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, gender: e.target.value })}
                >
                  <option value="">Select...</option>
                  <option value="male">Male</option>
                  <option value="female">Female</option>
                  <option value="other">Other</option>
                </Form.Control>
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Close
            </Button>
            <Button variant="primary" onClick={handleSavePlayer}>
              Save Changes
            </Button>
          </Modal.Footer>
        </Modal>

        {/* Delete Confirmation Modal */}
        <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Confirm Deletion</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>Are you sure you want to delete the player?</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
              Cancel
            </Button>
            <Button variant="danger" onClick={confirmDelete}>
              Confirm
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </>
  );
};

export default Players;
