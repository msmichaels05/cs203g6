import React from 'react';
import { Container, Nav, Navbar, Button } from 'react-bootstrap';
import { FaUserCircle } from 'react-icons/fa';  // Import icon for profile

const PlayerNavbar = () => {
  return (
    <Navbar expand="lg" style={{ backgroundColor: '#004080' }}>
      <Container>
        <Navbar.Brand href="/" style={{ color: 'white' }}>AmateurAces - Player</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/authen_home" style={navLinkStyle}>Home</Nav.Link>
            <Nav.Link href="/schedule" style={navLinkStyle}>Schedule</Nav.Link>
            <Nav.Link href="/draws" style={navLinkStyle}>Draws</Nav.Link>
            <Nav.Link href="/players" style={navLinkStyle}>Players</Nav.Link>
          </Nav>

          {/* Profile and Logout Links on the right side */}
          <Nav className="ml-auto">
            <Nav.Link href="/profile" style={profileLinkStyle}>
              <FaUserCircle style={profileIconStyle} /> My Profile
            </Nav.Link>
            <Button variant="outline-light" href="/home" style={{ marginLeft: '10px' }}>Logout</Button>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

const navLinkStyle = {
  color: 'white',
  marginRight: '15px',
};

const profileLinkStyle = {
  color: 'white',  // Set profile link font color to white
  marginRight: '15px',
  fontWeight: 'bold',  // Make the profile link bold
  backgroundColor: '#003366',  // Optional: background color for the profile link
  padding: '5px 10px',
  borderRadius: '5px',
  display: 'flex',
  alignItems: 'center',  // Align text and icon vertically
};

const profileIconStyle = {
  marginRight: '8px',  // Space between the icon and the text
  fontSize: '20px',  // Adjust icon size
  verticalAlign: 'middle',
};

export default PlayerNavbar;
