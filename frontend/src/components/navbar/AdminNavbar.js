import React from 'react';
import { Container, Nav, Navbar, Button } from 'react-bootstrap';

const AdminNavbar = () => {
  return (
    <Navbar expand="lg" style={{ backgroundColor: '#004080' }}>
      <Container>
        <Navbar.Brand href="/" style={{ color: 'white' }}>AmateurAces - Admin</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link href="/authen_home" style={navLinkStyle}>Home</Nav.Link>
            <Nav.Link href="/players" style={navLinkStyle}>Players</Nav.Link>
            <Nav.Link href="/tournaments" style={navLinkStyle}>Tournaments</Nav.Link>
            <Nav.Link href="/admins" style={navLinkStyle}>Admins</Nav.Link>
            

          </Nav>
          <Nav className="ml-auto">
            <Button variant="outline-light" href="/home" style={{ marginRight: '10px' }}>Logout</Button>
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

export default AdminNavbar;
