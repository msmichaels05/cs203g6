import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Table, Form } from 'react-bootstrap';
import CustomNavbar from '../navbar/CustomNavbar';
import { fetchPlayers } from '../../api/playersAPI';

const GuestPlayers = () => {
  const [players, setPlayers] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [loading, setLoading] = useState(true);

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

  if (loading) {
    return <div>Loading...</div>; // Loading indicator until players are loaded
  }

  return (
    <>
      <CustomNavbar /> {/* Use the custom GuestNavbar for guests */}
      <Container className="mt-4">
        <Row>
          <Col className="d-flex justify-content-between align-items-center mb-3">
            <h4>All Players - AmateurAces (Guest View)</h4>
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
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="6" className="text-center">
                        No players found
                      </td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default GuestPlayers;
