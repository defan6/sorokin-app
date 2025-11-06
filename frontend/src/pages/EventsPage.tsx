import AuthService from "../services/AuthService";
import React, {useState, useEffect} from 'react';
import {Container, Card, Button, Alert, Spinner} from 'react-bootstrap';
import EventService, {Event} from '../services/EventService';
import {Link} from "react-router-dom";

const EventsPage: React.FC = () => {
    const [events, setEvents] = useState<Event[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const currentUser = AuthService.getCurrentUser();

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
            <h2>Available Events</h2>
            {events.length === 0 ? (
                <Alert variant="info">No events available at the moment.</Alert>
            ) : (
                <div className="d-flex flex-wrap justify-content-start">
                    {events.map((event) => (
                        <Card key={event.id} style={{width: '18rem', margin: '10px'}}>
                            <Card.Body>
                                <Card.Title>{event.name}</Card.Title>
                                <Card.Text>{event.description}</Card.Text>
                                {/* Добавьте другие детали события здесь */}
                                <Link to={`/events/${event.id}`}>
                                    <Button variant="primary">View Details</Button>
                                </Link>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            )}
        </Container>
    );
};

export default EventsPage;
