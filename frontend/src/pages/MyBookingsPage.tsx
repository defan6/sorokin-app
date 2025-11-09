import React, { useState, useEffect, useMemo } from 'react';
import { Container, Button, Alert, Spinner, Badge } from 'react-bootstrap';
import BookingService, { Booking } from '../services/BookingService';
import AuthService from '../services/AuthService';

const MyBookingsPage: React.FC = () => {
    const [bookings, setBookings] = useState<Booking[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const currentUser = useMemo(() => AuthService.getCurrentUser(), []);

    const fetchBookings = () => {
        BookingService.getMyBookings()
            .then(response => {
                setBookings(response.data.myBookingResponses);
            })
            .catch(err => {
                console.error("Failed to fetch bookings", err);
                setError(err.response?.data?.message || 'Failed to load bookings.');
            })
            .finally(() => {
                setLoading(false);
            });
    };

    useEffect(() => {
        if (currentUser) {
            fetchBookings();
        }
    }, [currentUser]);

    const handleCancelBooking = async (bookingId: number) => {
        if (window.confirm('Are you sure you want to cancel this booking?')) {
            try {
                await BookingService.cancelBooking({ bookingId });
                // Перезагружаем список бронирований после отмены
                fetchBookings(); 
            } catch (err) {
                console.error("Failed to cancel booking", err);
                setError('Failed to cancel booking. Please try again.');
            }
        }
    };

    const getStatusBadge = (status: string) => {
        switch (status.toUpperCase()) {
            case 'CONFIRMED':
                return <Badge bg="success">Confirmed</Badge>;
            case 'CANCELLED':
                return <Badge bg="danger">Cancelled</Badge>;
            default:
                return <Badge bg="secondary">{status}</Badge>;
        }
    };

    if (loading) {
        return (
            <Container className="mt-4 text-center">
                <Spinner animation="border" />
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
            <h2>My Bookings</h2>
            {bookings.length === 0 ? (
                <Alert variant="info">You have no bookings.</Alert>
            ) : (
                <div>
                    {bookings.map(booking => (
                        <div key={booking.id} className="booking-item d-flex justify-content-between align-items-center mb-3">
                            <div>
                                <h5>{booking.eventName}</h5>
                                <p className="mb-1">
                                    <span className="text-muted">{booking.venueName}</span>
                                    <br />
                                    <small>{new Date(booking.eventDate).toLocaleString()}</small>
                                </p>
                                {getStatusBadge(booking.status)}
                            </div>
                            {booking.status.toUpperCase() !== 'CANCELLED' && (
                                <Button variant="outline-danger" size="sm" onClick={() => handleCancelBooking(booking.id)}>
                                    Cancel Booking
                                </Button>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </Container>
    );
};

export default MyBookingsPage;
