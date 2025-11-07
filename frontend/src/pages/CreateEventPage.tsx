import {Alert, Button, Container, Form, Row, Col, Card} from "react-bootstrap";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import EventService from "../services/EventService";
import VenueService, {Venue} from "../services/VenueService";


const CreateEventPage: React.FC = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [eventDate, setEventDate] = useState('');
    const [venues, setVenues] = useState<Venue[]>([]);
    const [venueId, setVenueId] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();


    useEffect(() => {
        const fetchVenues = async () => {
            try {
                const response = await VenueService.getVenues();
                setVenues(response.data);
            } catch (err) {
                console.error("Failed to fetch venues", err);
                setError("Could not load venues for selection");
            }
        };
        fetchVenues();
    }, []);


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        if (!title || !description || !eventDate || !venueId) {
            setError("All fields are required");
            setLoading(false);
            return;
        }


        try {
            console.log('Creating event: ', {title, description, eventDate, venueId});

            await EventService.createEvent({title, description, eventDate, venueId: Number(venueId)});

            navigate('/events');
        } catch (err) {
            setError('Failed to create event. Please try again');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };


    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col md={8} lg={6}>
                    <Card className="mt-4">
                        <Card.Body>
                            <h2 className="text-center mb-4">Create New Event</h2>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3" controlId="formEventName">
                                    <Form.Label>Event name:</Form.Label>
                                    <Form.Control
                                        type="text"
                                        placeholder="Enter event name"
                                        value={title}
                                        onChange={(e) => setTitle(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formEventDate">
                                    <Form.Label>Event Date:</Form.Label>
                                    <Form.Control
                                        type="datetime-local"
                                        value={eventDate}
                                        onChange={(e) => setEventDate(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formEventVenue">
                                    <Form.Label> Venue </Form.Label>
                                    <Form.Select
                                        value={venueId}
                                        onChange={(e) => setVenueId(e.target.value)}
                                        required>
                                        <option value="" disabled>Select a venue</option>
                                        {venues.map((venue) => (
                                            <option key={venue.id} value={venue.id}>
                                                {venue.name} ({venue.address})
                                            </option>
                                        ))}
                                    </Form.Select>
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formEventDescription">
                                    <Form.Label>Description:</Form.Label>
                                    <Form.Control
                                        as="textarea"
                                        rows={3}
                                        placeholder="Event description"
                                        value={description}
                                        onChange={(e) => setDescription(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                {error && <Alert variant="danger">{error}</Alert>}

                                <Button variant="primary" type="submit" disabled={loading} className="w-100">
                                    {loading ? 'Creating...' : 'Create Event'}
                                </Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default CreateEventPage