import React, { useState, useEffect, useMemo } from 'react';
import {Container, Alert, Spinner, Button, Row, Col, Card} from 'react-bootstrap';
import VenueService, { Venue } from '../services/VenueService';
import AuthService from '../services/AuthService';
import {Link} from "react-router-dom";

const VenuesPage: React.FC = () => {
  const [venues, setVenues] = useState<Venue[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const currentUser = useMemo(() => AuthService.getCurrentUser(), []);

  useEffect(() => {
    if (currentUser) {
      const fetchVenues = async () => {
        try {
          const response = await VenueService.getVenues();
          setVenues(response.data);
        } catch (err: any) {
          console.error('Failed to fetch venues:', err);
          setError(err.response?.data?.message || 'Failed to load venues.');
        } finally {
          setLoading(false);
        }
      };
      fetchVenues();
    } else {
      setLoading(false);
      setError('You must be logged in to view venues.');
    }
  }, [currentUser]);


  const handleDelete = async (id:number) => {
      if (window.confirm('Are you sure you want to delete this venue?')) {
          try {
              await VenueService.deleteVenue(id);
              setVenues(venues.filter(venue => venue.id !== id));
          } catch (err){
              console.error('Failed to delete venue: ', err);
              setError('Failed to delete venue. Please try again');
          }
      }
  };

  if (loading) {
    return (
      <Container className="mt-4 text-center">
        <Spinner animation="border" />
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Venues</h2>
        <Link to="/venues/create" className="btn btn-primary">Create Venue</Link>
      </div>
      {venues.length === 0 ? (
        <Alert variant="info">No venues available.</Alert>
      ) : (
        <Row xs={1} md={2} lg={3} className="g-4">
          {venues.map((venue) => (
            <Col key={venue.id}>
              <Card className="event-card h-100">
                <Card.Body className="d-flex flex-column">
                  <Card.Title>{venue.name}</Card.Title>
                  <Card.Text className="text-muted">{venue.address}</Card.Text>
                  <Card.Text>Capacity: {venue.capacity}</Card.Text>
                  <div className="mt-auto">
                    <Link to={`/venues/edit/${venue.id}`} className="btn btn-outline-secondary btn-sm me-2">
                      Edit
                    </Link>
                    <Button variant="outline-danger" size="sm" onClick={() => handleDelete(venue.id)}>
                      Delete
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}
    </Container>
  );
};

export default VenuesPage;