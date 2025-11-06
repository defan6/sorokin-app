import {Alert, Button, Container, Form} from "react-bootstrap";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import EventService from "../services/EventService";
import VenueService, {Venue} from "../services/VenueService";


const CreateEventPage: React.FC = () => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
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

        if (!name && !description) {
            setError("Name and description are required");
            setLoading(false);
            return;
        }


        try {
            console.log('Creating event: ', {name, description});

            await EventService.createEvent({name, description, venueId: Number(venueId)});

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
            <h2>Create New Event</h2>
            {/*Name*/}
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formEventName">
                    <Form.Label>Event name:</Form.Label>
                    <
                        Form.Control
                        type="text"
                        placeholder="Enter event name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
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
                            <option key={venueId} value={venue.id}>
                                {venue.name} ({venue.address})
                            </option>
                        ))}
                    </Form.Select>
                </Form.Group>

                {/*Поле description*/}
                <Form.Group className="mb-3" controlId="formEventDescription">
                    <Form.Label>Description:</Form.Label>
                    <
                        Form.Control
                        as="textarea"
                        rows={3}
                        placeholder="Event description"
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        required
                    />
                </Form.Group>

                {error && <Alert variant="danger">{error}</Alert>}

                <Button variant="primary" type="submit" disabled={loading}>
                    {loading ? 'Creating...' : 'Create Event'}
                </Button>
            </Form>
        </Container>
    );
};

export default CreateEventPage