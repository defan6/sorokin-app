import { Container, Form, Button, Alert } from 'react-bootstrap';
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import VenueService from "../services/VenueService";

const CreateVenuePage: React.FC = () => {
    const [name, setName] = useState('');
    const [location, setLocation] = useState('');
    const [capacity, setCapacity] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        if (!name || !location) {
            setError('Name and location are required');
            setLoading(false);
            return;
        }

        try {
            await VenueService.createVenue({name, address: location, capacity: Number(capacity)});
            navigate('/');
        } catch (err: any) {
            const errorMessage = err.response?.data?.message || "Failed to create venue. Please try again";
            setError(errorMessage);
            console.error(err);
        } finally {
            setLoading(false);
        }
    };
    return (
        <Container>
            <h2>Create New Venue</h2>
            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formVenueName">
                    <Form.Label>Venue Name</Form.Label>
                    <Form.Control type="text" placeholder="Enter venue name" value={name}
                                  onChange={(e) => setName(e.target.value)} required
                    />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formVenueLocation">
                    <Form.Label>Location</Form.Label>
                    <Form.Control type="text"
                                  placeholder="Enter address"
                                  value={location}
                                  onChange={(e) => setLocation(e.target.value)}
                                  required
                    />
                </Form.Group>
                <Form.Group className="mb-3" controlId="formVenueCapacity">
                    <Form.Label>Capacity</Form.Label>
                    <Form.Control type="number" placeholder="Enter capacity" value={capacity}
                                onChange={(e) => setCapacity(e.target.value)} required
                    />

                </Form.Group>

                {error && <Alert variant="danger">{error}</Alert>}

                <Button variant="primary" type="submit" disabled={loading}>
                    {loading ? 'Creating...' : 'Create Venue'}
                </Button>
            </Form>
        </Container>
    );
};

export default CreateVenuePage;