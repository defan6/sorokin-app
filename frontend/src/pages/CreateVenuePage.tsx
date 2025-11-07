import { Container, Form, Button, Alert, Row, Col, Card } from 'react-bootstrap';
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import VenueService from "../services/VenueService";

const CreateVenuePage: React.FC = () => {
    const [name, setName] = useState('');
    const [address, setAddress] = useState('');
    const [capacity, setCapacity] = useState('');
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate()

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        if (!name || !address) {
            setError('Name and address are required');
            setLoading(false);
            return;
        }

        try {
            await VenueService.createVenue({name, address, capacity: Number(capacity)});
            navigate('/venues');
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
            <Row className="justify-content-md-center">
                <Col md={8} lg={6}>
                    <Card className="mt-4">
                        <Card.Body>
                            <h2 className="text-center mb-4">Create New Venue</h2>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3" controlId="formVenueName">
                                    <Form.Label>Venue Name</Form.Label>
                                    <Form.Control type="text" placeholder="Enter venue name" value={name}
                                                  onChange={(e) => setName(e.target.value)} required
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3" controlId="formVenueAddress">
                                    <Form.Label>Address</Form.Label>
                                    <Form.Control type="text"
                                                  placeholder="Enter address"
                                                  value={address}
                                                  onChange={(e) => setAddress(e.target.value)}
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

                                <Button variant="primary" type="submit" disabled={loading} className="w-100">
                                    {loading ? 'Creating...' : 'Create Venue'}
                                </Button>
                            </Form>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default CreateVenuePage;