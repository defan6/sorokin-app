import React, { useState, useEffect } from 'react';
import { Container, ListGroup, Alert, Spinner } from 'react-bootstrap';
import VenueService, { Venue } from '../services/VenueService';
import AuthService from '../services/AuthService';

const VenuesPage: React.FC = () => {
  const [venues, setVenues] = useState<Venue[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const currentUser = AuthService.getCurrentUser();

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
      <h2>Venues</h2>
      {venues.length === 0 ? (
        <Alert variant="info">No venues available.</Alert>
      ) : (
        <ListGroup>
          {venues.map((venue) => (
            <ListGroup.Item key={venue.id}>
              <h5>{venue.name}</h5>
              <p className="mb-1 text-muted">{venue.address}</p>
              <small>Capacity: {venue.capacity}</small>
            </ListGroup.Item>
          ))}
        </ListGroup>
      )}
    </Container>
  );
};

export default VenuesPage;