import React, { useState } from 'react';
import { Container, Row, Col, Card, Table, Button, Form, Modal } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import AdminNavbar from '../navbar/AdminNavbar';

const Admins = () => {
  const [admins, setAdmins] = useState([
    { id: 1, name: 'Jamie', email: 'jamiewet3030@gmail.com' },
    { id: 2, name: 'Johnny', email: 'jeffrey@gmail.com' },
    { id: 3, name: 'Michael', email: 'chaselim96@gmail.com' },
  ]);

  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(''); // 'add' or 'edit'
  const [currentAdmin, setCurrentAdmin] = useState({});

  const handleSearch = (event) => setSearchTerm(event.target.value);

  const filteredAdmins = admins.filter(admin =>
    admin.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Add Admin Modal
  const handleAddAdmin = () => {
    setModalType('add');
    setCurrentAdmin({ name: '', email: '' });
    setShowModal(true);
  };

  // Edit Admin Modal
  const handleEditAdmin = (admin) => {
    setModalType('edit');
    setCurrentAdmin(admin);
    setShowModal(true);
  };

  // Delete Admin
  const handleDeleteAdmin = (id) => {
    setAdmins(admins.filter(admin => admin.id !== id));
  };

  // Save Admin (for both adding and editing)
  const handleSaveAdmin = () => {
    if (modalType === 'add') {
      setAdmins([...admins, { ...currentAdmin, id: Date.now() }]);
    } else {
      setAdmins(admins.map(a => (a.id === currentAdmin.id ? currentAdmin : a)));
    }
    setShowModal(false);
  };

  return (
    <>
      <AdminNavbar />

      <Container className="mt-4">
        <Row>
          <Col className="d-flex justify-content-between align-items-center mb-3">
            <h4>All Admins - AmateurAces</h4>
            <Button variant="success" onClick={handleAddAdmin}>Add New Admin</Button>
          </Col>
        </Row>
        <Row>
          <Col>
            <Form>
              <Form.Group controlId="search">
                <Form.Control
                  type="text"
                  placeholder="Search by admin name"
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
                    <th>Admin Name</th>
                    <th>Email</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredAdmins.length > 0 ? (
                    filteredAdmins.map((admin) => (
                      <tr key={admin.id}>
                        <td>{admin.name}</td>
                        <td>{admin.email}</td>
                        <td>
                          <Button variant="warning" size="sm" className="me-2" onClick={() => handleEditAdmin(admin)}>
                            <FaEdit />
                          </Button>
                          <Button variant="danger" size="sm" onClick={() => handleDeleteAdmin(admin.id)}>
                            <FaTrash />
                          </Button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="3" className="text-center">
                        No admins found
                      </td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Card>
          </Col>
        </Row>

        {/* Modal for Add/Edit Admin */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>{modalType === 'add' ? 'Add New Admin' : 'Edit Admin'}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form>
              <Form.Group controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  type="text"
                  value={currentAdmin.name}
                  onChange={(e) => setCurrentAdmin({ ...currentAdmin, name: e.target.value })}
                />
              </Form.Group>
              <Form.Group controlId="email">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  value={currentAdmin.email}
                  onChange={(e) => setCurrentAdmin({ ...currentAdmin, email: e.target.value })}
                />
              </Form.Group>
            </Form>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Cancel
            </Button>
            <Button variant="primary" onClick={handleSaveAdmin}>
              Save
            </Button>
          </Modal.Footer>
        </Modal>
      </Container>
    </>
  );
};

export default Admins;
