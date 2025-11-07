import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import VenueService from "../services/VenueService";
import {Alert, Button, Container, Form, Spinner, Row, Col, Card} from "react-bootstrap";

const EditVenuePage: React.FC = () => {
    const {id} = useParams<{id: string}>();
    const navigate = useNavigate();

    const [name, setName] = useState('');
    const [address, setAddress] = useState('');
    const [capacity, setCapacity] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);


    useEffect(() => {
        if(id){
            const venueId = Number(id);
            setLoading(true);
            VenueService.getVenueById(venueId)
                .then(response => {
                  const venue = response.data;
                  setName(venue.name);
                  setAddress(venue.address);
                  setCapacity(String(venue.capacity));
                  setLoading(false);
                })
                .catch(err => {
                    console.error('Failed to fetch venue:', err);
                    setError('Failed to load venue data');
                    setLoading(false);
                });
        }
    }, [id]);


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if(!id) return;

        setLoading(true);

        setError(null);


        try {
            await VenueService.updateVenue(Number(id), {
                name,
                address,
                capacity: Number(capacity),
            });
            navigate('/venues');
        } catch (err) {
            console.error('Failed to update venue:', err);
            setError('Failed to update venue. Please try again');
        } finally {
            setLoading(false);
        }
    };


    if(loading){
        return (
            <Container className="mt-4 text-center">
                <Spinner animation="border"/>
            </Container>
        )
    }


    if(error) {
        return (
            <Container className="mt-4">
                <Alert variant="danger">{error}</Alert>
            </Container>
        )
    }


    return (
        <Container>
            <Row className="justify-content-md-center">
                <Col md={8} lg={6}>
                    <Card className="mt-4">
                        <Card.Body>
                            <h2 className="text-center mb-4">Edit Venue</h2>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3" controlId="formVenueName">
                                    <Form.Label>Venue Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={name}
                                        onChange={(e) => setName(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formVenueAddress">
                                    <Form.Label>Address</Form.Label>
                                    <Form.Control
                                        type="text"
                                        value={address}
                                        onChange={(e) => setAddress(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formVenueCapacity">
                                    <Form.Label>Capacity</Form.Label>
                                    <Form.Control
                                        type="number"
                                        value={capacity}
                                        onChange={(e) => setCapacity(e.target.value)}
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


export default EditVenuePage;