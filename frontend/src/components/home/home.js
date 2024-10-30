import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import CustomNavbar from '../navbar/CustomNavbar';  
import './home.css';

const Home = () => {
  return (
    <>
      <CustomNavbar />
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
}

export default Home;
