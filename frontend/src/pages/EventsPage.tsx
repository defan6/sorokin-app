import AuthService from "../services/AuthService";
import React, {useState, useEffect, useMemo} from 'react';
import {Container, Card, Button, Alert, Spinner, Row, Col} from 'react-bootstrap';
import EventService, {Event} from '../services/EventService';
import {Link} from "react-router-dom";

const EventsPage: React.FC = () => {
    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const currentUser = useMemo(() => AuthService.getCurrentUser(), []);

    useEffect(() => {
        if (currentUser) {
            const fetchEvents = async () => {
                try {
                    const response = await EventService.getEvents();
                    setEvents(response.data);
                } catch (err: any) {
                    console.error('Failed to fetch events:', err);
                    setError(err.response?.data?.message || 'Failed to load events.');
                } finally {
                    setLoading(false);
                }
            };

            fetchEvents();
        } else {
            setLoading(false);
        }
    }, [currentUser]);
    const isAdmin = useMemo(() => currentUser?.roles?.includes('ROLE_ADMIN'), [currentUser]);

    const handleDelete = async (id: number) => {
        if (window.confirm('Are you sure you want to delete this event?')) {
            try {
                await EventService.deleteEvent(id);
                setEvents(events.filter(event => event.id !== id));
            } catch (err) {
                console.error('Failed to delete event:', err);
                setError('Failed to delete event. Please try again.');
            }
        }
    };

    if (!currentUser) {
        return (
            <Container className="mt-4">
                <Alert variant="warning">
                    <h4>Please log in</h4>
                    <p>
                        To view the events, you need to be logged
                    </p>
                    <hr/>
                    <div className="d-flex justify-content-end">
                        <Link to="/login">
                            <Button variant="primary" className="me-2">
                                Login
                            </Button>
                        </Link>
                        <Link to="/register">
                            <Button variant="secondary">
                                Register
                            </Button>
                        </Link>
                    </div>
                </Alert>
            </Container>
        );
    }

    if (loading) {
        return (
            <Container className="mt-4 text-center">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading events...</span>
                </Spinner>
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
                <h2>Available Events</h2>
                {isAdmin && (
                    <Link to="/events/create" className="btn btn-primary">Create Event</Link>
                )}
            </div>
            {events.length === 0 ? (
                <Alert variant="info">No events available at the moment.</Alert>
            ) : (
                <Row xs={1} md={2} lg={3} className="g-4">
                    {events.map((event) => (
                        <Col key={event.id}>
                            <Card className="event-card h-100">
                                <Card.Body className="d-flex flex-column">
                                    <Card.Title>{event.title}</Card.Title>
                                    <Card.Text className="text-muted">
                                        {new Date(event.eventDate).toLocaleDateString()}
                                    </Card.Text>
                                    <Card.Text>{event.description}</Card.Text>
                                    <div className="mt-auto">
                                        <Link to={`/events/${event.id}`} className="btn btn-primary btn-sm me-2">View Details</Link>
                                        {isAdmin && (
                                            <>
                                                <Link to={`/events/edit/${event.id}`} className="btn btn-outline-secondary btn-sm me-2">Edit</Link>
                                                <Button variant="outline-danger" size="sm" onClick={() => handleDelete(event.id)}>Delete</Button>
                                            </>
                                        )}
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

export default EventsPage;
