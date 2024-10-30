import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import AdminNavbar from '../navbar/AdminNavbar';  // Import AdminNavbar
import PlayerNavbar from '../navbar/PlayerNavbar';  // Import PlayerNavbar
import './home.css';

const Authen_home = () => {
  // Mock user role (In the future, this should come from your backend or global state, like Redux or Context API)
  const user = {
    role: 'player',  // Change this to 'admin' for admin view
  };

  return (
    <>
      {/* Conditionally render the Navbar based on user role */}
      {user.role === 'admin' ? <AdminNavbar /> : <PlayerNavbar />}
      
      <Container className="mt-4">
        <Row>
          <Col md={3}>
            <Card className="mb-3">
              <Card.Body>
                <Card.Title>Best of AmateurAces</Card.Title>
                <ul>
                  <li>Upcoming Tournaments</li>
                  <li>Top Players</li>
                  <li>Previous Results</li>
                  <li>Top Coaches</li>
                </ul>
              </Card.Body>
            </Card>
          </Col>
          
          <Col md={6}>
            <Card>
              <Card.Img variant="top" src="path/to/tournament-image.jpg" />
              <Card.Body>
                <Card.Title>Headline Tournament Event</Card.Title>
                <Card.Text>
                  Check out the latest highlights from the AmateurAces tournaments. Stay tuned for more updates!
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>

          <Col md={3}>
            <Card className="mb-3">
              <Card.Body>
                <Card.Title>Top Headlines</Card.Title>
                <ul>
                  <li>Player X takes the lead</li>
                  <li>Surprising Upset in Finals</li>
                  <li>Top Players to Watch</li>
                </ul>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Authen_home;
