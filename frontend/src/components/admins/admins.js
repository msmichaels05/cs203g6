import React, { useState, useEffect } from 'react';
import { fetchAdmins, addAdmin, editAdmin, deleteAdmin } from '../../api/adminsAPI';
import { Container, Row, Col, Card, Table, Button, Form, Modal } from 'react-bootstrap';
import { FaEdit, FaTrash } from 'react-icons/fa';
import AdminNavbar from '../navbar/AdminNavbar';

const Admins = () => {
  const [admins, setAdmins] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [modalType, setModalType] = useState(''); // 'add' or 'edit'
  const [currentAdmin, setCurrentAdmin] = useState({ name: '', phone: '' });

  useEffect(() => {
    const getAdmins = async () => {
      try {
        const data = await fetchAdmins();
        setAdmins(data);
      } catch (error) {
        console.error('Failed to fetch admins:', error);
      }
    };
    getAdmins();
  }, []);

  const handleSearch = (event) => setSearchTerm(event.target.value);

  const filteredAdmins = admins.filter(admin =>
    admin.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleAddAdmin = () => {
    setModalType('add');
    setCurrentAdmin({ name: '', phone: '' });
    setShowModal(true);
  };

  const handleEditAdmin = (admin) => {
    setModalType('edit');
    setCurrentAdmin(admin);
    setShowModal(true);
  };

  const handleDeleteAdmin = async (id) => {
    try {
      await deleteAdmin(id);
      setAdmins(admins.filter(admin => admin.id !== id));
    } catch (error) {
      console.error('Failed to delete admin:', error);
    }
  };

  const handleSaveAdmin = async () => {
    try {
      if (modalType === 'add') {
        const newAdmin = await addAdmin(currentAdmin);
        setAdmins([...admins, newAdmin]);
      } else {
        const updatedAdmin = await editAdmin(currentAdmin.id, currentAdmin);
        setAdmins(admins.map(a => (a.id === currentAdmin.id ? updatedAdmin : a)));
      }
      setShowModal(false);
    } catch (error) {
      console.error('Failed to save admin:', error);
    }
  };

  return (
    <>
      <AdminNavbar />

      <Container className="mt-4">
        <Row className="align-items-center mb-3">
          <Col>
            <h4>All Admins - AmateurAces</h4>
          </Col>
          <Col xs="auto">
            <Button
              variant="success"
              onClick={handleAddAdmin}
              style={{
                padding: '6px 12px',
                fontSize: '14px',
                fontWeight: 'bold',
                borderRadius: '5px'
              }}
            >
              Add New Admin
            </Button>
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
                    <th className="text-center">Admin Name</th>
                    <th className="text-center">Phone Number</th>
                    <th className="text-center">Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredAdmins.length > 0 ? (
                    filteredAdmins.map((admin) => (
                      <tr key={admin.id}>
                        <td className="text-center">{admin.name}</td>
                        <td className="text-center">{admin.phone}</td>
                        <td className="text-center">
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
              <Form.Group controlId="phone">
                <Form.Label>Phone Number</Form.Label>
                <Form.Control
                  type="text"
                  value={currentAdmin.phone}
                  onChange={(e) => setCurrentAdmin({ ...currentAdmin, phone: e.target.value })}
                  placeholder="Enter phone number"
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
