import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Spinner } from 'react-bootstrap';
import AdminNavbar from '../navbar/AdminNavbar';  // Import AdminNavbar
import PlayerNavbar from '../navbar/PlayerNavbar';  // Import PlayerNavbar
import './home.css';

const Authen_home = () => {
  const [role, setRole] = useState(null);  // Store the user's role
  const [loading, setLoading] = useState(true);  // Loading state

  useEffect(() => {
    // Simulating loading state for user role
    const storedRole = localStorage.getItem('role');  // Retrieve role from localStorage
    if (storedRole) {
      setRole(storedRole);  // Set the role if it exists
    }
    setLoading(false);  // After fetching role, stop loading
  }, []);  // Empty dependency array means this runs only once, when the component mounts

  if (loading) {
    return (
      <div className="loading-container">
        <Spinner animation="border" variant="primary" />
        <span> Loading...</span>
      </div>
    );  // Show a loading spinner until the role is fetched
  }

  return (
    <>
      {/* Conditionally render the Navbar based on user role */}
      {role === 'ROLE_ADMIN' ? <AdminNavbar /> : <PlayerNavbar />}
      
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
