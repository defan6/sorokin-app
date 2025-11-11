import React, {useState, useEffect} from 'react';
import {BrowserRouter as Router, Routes, Route, Link, useNavigate} from 'react-router-dom';
import {Navbar, Container, Nav} from 'react-bootstrap';

import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import EventsPage from './pages/EventsPage';
import AuthService from './services/AuthService';
import EventDetailPage from "./pages/EventDetailPage";
import ProfilePage from "./pages/ProfilePage";
import HomePage from "./pages/HomePage";
import CreateEventPage from "./pages/CreateEventPage";
import CreateVenuePage from "./pages/CreateVenuePage";
import AdminRoute from "./components/AdminRoute";
import VenuesPage from "./pages/VenuesPage";
import EditVenuePage from "./pages/EditVenuePage";
import UserManagementPage from "./pages/UserManagementPage";
import MyBookingsPage from "./pages/MyBookingsPage";


interface User {
    accessToken: string;
    username: string
    fullName: string
    roles: string[]
    // Добавьте другие поля пользователя, если они есть в ответе
}

const MainApp: React.FC = () => {
    const [currentUser, setCurrentUser] = useState<User | null>(null);
    const navigate = useNavigate();

    useEffect(() => {
        const user = AuthService.getCurrentUser();
        if (user) {
            setCurrentUser(user);
        }
    }, []);

    const logOut = () => {
        AuthService.logout();
        setCurrentUser(null);
        navigate('/');
        // Можно добавить редирект на главную страницу
    };

    const isAdmin = currentUser?.roles?.includes("ROLE_ADMIN");

    return (
        <>
            <Navbar bg="dark" variant="dark" expand="lg">
                <Container>
                    <Navbar.Brand as={Link} to="/">Event Platform</Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav"/>
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="me-auto">
                            <Nav.Link as={Link} to="/">Home</Nav.Link>
                            <Nav.Link as={Link} to="/events">Events</Nav.Link>
                            {isAdmin && (
                                <>
                                    <Nav.Link as={Link} to="/events/create">Create Event</Nav.Link>
                                    <Nav.Link as={Link} to="/venues/create">Create Venue</Nav.Link>
                                    <Nav.Link as={Link} to="/venues">Venues</Nav.Link>
                                    <Nav.Link as={Link} to="/admin/users">User Management</Nav.Link>
                                </>
                            )}
                        </Nav>
                        <Nav>
                            {currentUser ? (
                                <>
                                    <Nav.Link as={Link} to="/profile">{currentUser.username}</Nav.Link>
                                    <Nav.Link as={Link} to="/my-bookings">My Bookings</Nav.Link>
                                    <Nav.Link onClick={logOut}>Logout</Nav.Link>
                                </>
                            ) : (
                                <>
                                    <Nav.Link as={Link} to="/login">Login</Nav.Link>
                                    <Nav.Link as={Link} to="/register">Register</Nav.Link>
                                </>
                            )}
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            <Container className="mt-4">
                <Routes>
                    <Route path="/" element={<HomePage />}/>
                    <Route path="/events" element={<EventsPage/>}/>
                    <Route path="/login" element={<LoginPage/>}/>
                    <Route path="/register" element={<RegisterPage/>}/>
                    <Route path="/events/:id" element={<EventDetailPage/>}/>
                    <Route path="/profile" element={<ProfilePage />} />
                    <Route path="/admin/users" element={<UserManagementPage />} />
                    <Route path="/my-bookings" element={<MyBookingsPage />} />
                    <Route element={<AdminRoute />}>
                        <Route path="/venues" element={<VenuesPage/>}></Route>
                        <Route path="/events/create" element={<CreateEventPage/>} />
                        <Route path="/venues/create" element={<CreateVenuePage/>} />
                        <Route path="/venues/edit/:id" element={<EditVenuePage />} />
                    </Route>
                </Routes>
            </Container>
        </>
    );
}


const App: React.FC = () => {
    return (
        <Router>
            <MainApp/>
        </Router>
    )
}

export default App;