import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import EventService, {Event} from "../services/EventService";
import {Alert, Button, Card, Container, Spinner} from "react-bootstrap";


const EventDetailPage: React.FC = () => {
    const {id} = useParams<{ id: string }>();
    const [event, setEvent] = useState<Event | null>(null)
    const [loading, setLoading] = useState<boolean>(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        const fetchEvent = async () => {
            if (!id) {
                setError('Event id is missing.');
                setLoading(false);
                return;
            }
            try {
                const response = await EventService.getEventById(Number(id));
                setEvent(response.data);
            } catch (err: any) {
                console.error('Failed to fetch event details:', err);
                setError(err.response?.data?.message || "Failed to load event details");
            } finally {
                setLoading(false)
            }
        };
        fetchEvent();
    }, [id])

    if (loading) {
        return (
            <Container className="mt-4 text-center">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading event details...</span>
                </Spinner>
            </Container>
        );
    }


    if (error) {
        return (
            <Container className="mt-4">
                <Alert variant="danger">{error}</Alert>
                <Link to="/events">
                    <Button variant="secondary" className="mt-3">
                        Back to Events
                    </Button>
                </Link>
            </Container>
        );
    }


    if (!event) {
        return (
            <Container className="mt-4">
                <Alert variant="warning">Event not found.</Alert>
                <Link to="/events">
                    <Button variant="secondary" className="mt-3">
                        Back to Events
                    </Button>
                </Link>
            </Container>
        )
    }

    return (
        <Container className="mt-4">
            <Card>
                <Card.Body>
                    <Card.Title as="h2">{event.title}</Card.Title>
                    <Card.Subtitle className="mb-2 text-muted">Event ID: {event.id}</Card.Subtitle>
                    <Card.Text>
                        <strong>Description:</strong> {event.description}
                    </Card.Text>
                    <Link to="/events">
                        <Button variant="primary" className="mt-3">
                            Back to Events
                        </Button>
                    </Link>
                </Card.Body>
            </Card>
        </Container>
    );
};


export default EventDetailPage;