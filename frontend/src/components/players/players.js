import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Button, Form, Modal } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import AdminNavbar from '../navbar/AdminNavbar';
import PlayerNavbar from '../navbar/PlayerNavbar';
import { fetchPlayers, addPlayer, editPlayer, deletePlayer } from '../../api/playersAPI'; // Import API functions

const Players = () => {
  const [players, setPlayers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(''); // 'add' or 'edit'
  const [currentPlayer, setCurrentPlayer] = useState({});
  const [role, setRole] = useState(null);  // Store the user's role
  const [loading, setLoading] = useState(true);  // Loading state

  // Fetch user role from localStorage on component mount
  useEffect(() => {
    const storedRole = localStorage.getItem('role');  // Retrieve role from localStorage
    if (storedRole) {
      setRole(storedRole);  // Set the role if it exists
    }
    setLoading(false);  // Stop loading after fetching role
  }, []);

  // Fetch players when the component mounts
  useEffect(() => {
    const loadPlayers = async () => {
      try {
        const playersData = await fetchPlayers();
        setPlayers(playersData); // Set players state from backend data
      } catch (error) {
        console.error('Error fetching players:', error);
      }
    };

    loadPlayers();
  }, []);

  const handleSearch = (event) => setSearchTerm(event.target.value);

  const filteredPlayers = players.filter(player =>
    player.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Add Player Modal
  const handleAddPlayer = () => {
    setModalType('add');
    setCurrentPlayer({ name: '', email: '', phoneNumber: '', age: '', gender: '', matchesPlayed: 0, matchesWon: 0 });
    setShowModal(true);
  };

  // Edit Player Modal
  const handleEditPlayer = (player) => {
    setModalType('edit');
    setCurrentPlayer(player);
    setShowModal(true);
  };

  // Delete Player
  const handleDeletePlayer = async (id) => {
    try {
      await deletePlayer(id); // Call the API to delete the player
      setPlayers(players.filter(player => player.id !== id)); // Update local state
    } catch (error) {
      console.error('Error deleting player:', error);
    }
  };

  // Save Player (for both adding and editing)
  const handleSavePlayer = async () => {
    try {
      if (modalType === 'add') {
        const newPlayer = await addPlayer(currentPlayer); // Add player via API
        setPlayers([...players, newPlayer]); // Add new player to local state
      } else {
        const updatedPlayer = await editPlayer(currentPlayer.id, currentPlayer); // Update player via API
        setPlayers(players.map(p => (p.id === updatedPlayer.id ? updatedPlayer : p))); // Update player in local state
      }
      setShowModal(false);
    } catch (error) {
      console.error('Error saving player:', error);
    }
  };

  if (loading) {
    return <div>Loading...</div>;  // Loading indicator until role is fetched
  }

  return (
    <>
      {role === "ROLE_ADMIN" ? <AdminNavbar /> : <PlayerNavbar />}

      <Container className="mt-4">
        <Row>
          <Col className="d-flex justify-content-between align-items-center mb-3">
            <h4>All Players - AmateurAces</h4>
            {role === "ROLE_ADMIN" && (
              <Button variant="success" onClick={handleAddPlayer}>Add New Player</Button>
            )}
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
                    <th>Email</th>
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
                        <td>{player.email}</td>
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
                            <Button variant="danger" size="sm" onClick={() => handleDeletePlayer(player.id)}>
                              <FaTrash />
                            </Button>
                          </td>
                        )}
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={role === "ROLE_ADMIN" ? "8" : "7"} className="text-center">
                        No players found
                      </td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card>
          </Col>
        </Row>

        {/* Modal for Add/Edit Player */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>{modalType === 'add' ? 'Add New Player' : 'Edit Player'}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  value={currentPlayer.name}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, name: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="email">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  value={currentPlayer.email}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, email: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="phoneNumber">
                <Form.Label>Phone Number</Form.Label>
                <Form.Control
                  type="text"
                  value={currentPlayer.phoneNumber}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, phoneNumber: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="age">
                <Form.Label>Age</Form.Label>
                <Form.Control
                  type="number"
                  value={currentPlayer.age}
                  onChange={(e) => setCurrentPlayer({ ...currentPlayer, age: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="gender">
                <Form.Label>Gender</Form.Label>
                <Form.Control
                  as="select"
                  value={currentPlayer.gender}
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
              Cancel
            </Button>
            <Button variant="primary" onClick={handleSavePlayer}>
              Save
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </>
  );
};

export default Players;
