import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Alert, Spinner, Row, Col, Card } from 'react-bootstrap';
import { useNavigate, useParams } from 'react-router-dom';
import EventService from '../services/EventService';
import VenueService, { Venue } from '../services/VenueService';

const EditEventPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [eventDate, setEventDate] = useState('');
  const [venueId, setVenueId] = useState('');
  const [venues, setVenues] = useState<Venue[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (id) {
      const eventId = Number(id);
      setLoading(true);

      // Загружаем данные события
      EventService.getEventById(eventId)
        .then(response => {
          const event = response.data;
          setTitle(event.title);
          setDescription(event.description);
          // Форматируем дату для input[type=datetime-local]
          const formattedDate = new Date(event.eventDate).toISOString().slice(0, 16);
          setEventDate(formattedDate);
          setVenueId(String(event.venueId));
        })
        .catch(err => {
          console.error('Failed to fetch event:', err);
          setError('Failed to load event data.');
        });

      // Загружаем список площадок для выпадающего списка
      VenueService.getVenues()
        .then(response => {
          setVenues(response.data);
        })
        .catch(err => {
          console.error('Failed to fetch venues:', err);
          // Ошибка загрузки площадок не является критичной для формы редактирования
        });

      setLoading(false);
    }
  }, [id]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!id) return;

    setLoading(true);
    setError(null);

    try {
      await EventService.updateEvent(Number(id), {
        title,
        description,
        eventDate,
        venueId: Number(venueId),
      });
      navigate('/events');
    } catch (err) {
      console.error('Failed to update event:', err);
      setError('Failed to update event. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (loading && !title) { // Показываем спиннер только при первоначальной загрузке
    return (
      <Container className="mt-4 text-center">
        <Spinner animation="border" />
      </Container>
    );
  }

  return (
    <Container>
      <Row className="justify-content-md-center">
        <Col md={8} lg={6}>
          <Card className="mt-4">
            <Card.Body>
              <h2 className="text-center mb-4">Edit Event</h2>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formEventTitle">
                  <Form.Label>Title</Form.Label>
                  <Form.Control
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formEventDate">
                  <Form.Label>Event Date</Form.Label>
                  <Form.Control
                    type="datetime-local"
                    value={eventDate}
                    onChange={(e) => setEventDate(e.target.value)}
                    required
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formEventVenue">
                  <Form.Label>Venue</Form.Label>
                  <Form.Select
                    value={venueId}
                    onChange={(e) => setVenueId(e.target.value)}
                    required
                  >
                    <option value="" disabled>Select a venue</option>
                    {venues.map(venue => (
                      <option key={venue.id} value={venue.id}>
                        {venue.name}
                      </option>
                    ))}
                  </Form.Select>
                </Form.Group>

                <Form.Group className="mb-3" controlId="formEventDescription">
                  <Form.Label>Description</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={3}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                  />
                </Form.Group>

                {error && <Alert variant="danger">{error}</Alert>}

                <Button variant="primary" type="submit" disabled={loading} className="w-100">
                  {loading ? 'Saving...' : 'Save Changes'}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default EditEventPage;
