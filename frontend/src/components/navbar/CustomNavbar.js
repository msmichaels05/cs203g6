import React from 'react';
import { Container, Nav, Navbar, Button } from 'react-bootstrap';

function CustomNavbar() {
  return (
    <Navbar expand="lg" style={{ backgroundColor: '#004080' }}>
      <Container>
        <Navbar.Brand href="#home" style={{ color: 'white' }}>AmateurAces</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/" style={navLinkStyle}>Home</Nav.Link>
            <Nav.Link href="/players" style={navLinkStyle}>Players</Nav.Link>
            <Nav.Link href="/tournaments" style={navLinkStyle}>Tournaments</Nav.Link>
          </Nav>
          <Nav className="ml-auto">
            <Button variant="outline-light" href="/login" style={{ marginRight: '10px' }}>Login</Button>
            <Button variant="light" href="/register">Register</Button>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

const navLinkStyle = {
  color: 'white',
  marginRight: '15px',
};

export default CustomNavbar;
