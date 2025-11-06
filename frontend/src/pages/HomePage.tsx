import AuthService from "../services/AuthService";
import {Button, Col, Container, Row} from "react-bootstrap";
import {Link} from "react-router-dom";

const HomePage: React.FC = () => {
    const currentUser = AuthService.getCurrentUser()


    return (
        <Container className="mt-5">
            <Row className="justify-content-md-center text-center">
                <Col lg={8}>
                    <h1 className="display-4">Welcome to Event Platform</h1>
                    <p className="lead">
                        Your one-stop solution for discovering and managing events.
                        Browse through a wide variety of upcoming events, or log in to manage your own.
                    </p>
                    <hr className="my-4"/>
                    <p>
                        Ready to get started?
                    </p>
                    <p>
                        <Link to="/events">
                            <Button variant="primary" size="lg" className="m-2">
                                Browse Events
                            </Button>
                            {!currentUser && (
                                <Link to="/login">
                                    <Button variant="secondary" size="lg" className="m-2">
                                        Login
                                    </Button>
                                </Link>
                            )}
                        </Link>
                    </p>
                </Col>
            </Row>
        </Container>
    );
};

export default HomePage