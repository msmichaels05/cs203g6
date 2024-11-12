import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Table } from 'react-bootstrap';
import PlayerNavbar from '../navbar/PlayerNavbar';
import './profile.css';
import { fetchPlayerDetails } from '../../api/playersAPI'; // Import the API function

const Profile = () => {
  const [playerDetails, setPlayerDetails] = useState({
    name: "Player Name",
    username: "username",
    email: "email@example.com",
    joined: "",
    gamesPlayed: 0,
    wins: 0,
    losses: 0,
    elo: 0,
    gender: "N/A",
    age: 0,
    profilePicture: "https://via.placeholder.com/150",
  });

  const [matchHistory] = useState([
    { date: "28-11-2023", tournament: "JJk1", result: "W", opponent: "Geto", score: "6-3" },
    { date: "28-11-2023", tournament: "JJK2", result: "W", opponent: "Toji", score: "4-2" },
  ]);

  useEffect(() => {
    const userData = JSON.parse(localStorage.getItem('user'));
  
    // Fetch player details if user data exists
    if (userData && userData.userId) {
      fetchPlayerDetails(userData.userId)
        .then((data) => {
          setPlayerDetails((prevDetails) => ({
            ...prevDetails,
            name: data.name || "Player Name",
            username: data.user.username || "username",
            email: data.user.email || "email@example.com",
            age: data.age || 0,
            gender: data.gender || "N/A",
            elo: data.elo || 0,
            gamesPlayed: data.matchesPlayed || 0,
            wins: data.matchesWon || 0,
            losses: data.matchesPlayed - data.matchesWon || 0,
            profilePicture: data.profilePicture || prevDetails.profilePicture,
          }));
        })
        .catch((error) => {
          console.error('Error fetching player details:', error);
        });
    }
  }, []);

  const handlePhotoUpload = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPlayerDetails((prevDetails) => ({
          ...prevDetails,
          profilePicture: reader.result,
        }));
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <>
      <PlayerNavbar />
      <Container className="mt-4">
        <Row className="justify-content-center">
          <Col md={8}>
            <Card className="profile-card mb-4">
              <Card.Body className="text-center">
                <Card.Img
                  variant="top"
                  src={playerDetails.profilePicture}
                  className="rounded-circle mb-3"
                  style={{ width: '150px', height: '150px' }}
                />
                <Card.Title className="mb-2">{playerDetails.name}</Card.Title>
                <Card.Text className="text-muted">@{playerDetails.username}</Card.Text>
                <Card.Text>Email: {playerDetails.email}</Card.Text>
                <Card.Text>Age: {playerDetails.age} years</Card.Text>
                <Card.Text>Gender: {playerDetails.gender}</Card.Text>
                <Card.Text>Elo: {playerDetails.elo}</Card.Text>
                <Button variant="primary" className="mt-3" onClick={() => document.getElementById('photoUpload').click()}>
                  Edit Profile Photo
                </Button>
                <input
                  type="file"
                  id="photoUpload"
                  accept="image/*"
                  style={{ display: 'none' }}
                  onChange={handlePhotoUpload}
                />
              </Card.Body>
            </Card>
            <Card className="profile-card mb-4">
              <Card.Body>
                <Card.Title className="text-center">Game Statistics</Card.Title>
                <ul className="stat-list">
                  <li><strong>Games Played:</strong> {playerDetails.gamesPlayed}</li>
                  <li><strong>Wins:</strong> {playerDetails.wins}</li>
                  <li><strong>Losses:</strong> {playerDetails.losses}</li>
                </ul>
              </Card.Body>
            </Card>
            <Card className="profile-card">
              <Card.Body>
                <Card.Title className="text-center">Match History</Card.Title>
                <Table striped bordered hover>
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Tournament</th>
                      <th>W/L</th>
                      <th>Opponent</th>
                      <th>Score</th>
                    </tr>
                  </thead>
                  <tbody>
                    {matchHistory.length === 0 ? (
                      <tr>
                        <td colSpan="5" className="text-center">No match history available.</td>
                      </tr>
                    ) : (
                      matchHistory.map((match, index) => (
                        <tr key={index}>
                          <td>{match.date}</td>
                          <td>{match.tournament}</td>
                          <td>{match.result}</td>
                          <td>{match.opponent}</td>
                          <td>{match.score}</td>
                        </tr>
                      ))
                    )}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Profile;
