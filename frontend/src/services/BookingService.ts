import api from './api';

const API_URL = '/api/manager/bookings';

// Интерфейс для одного бронирования
export interface Booking {
    id: number;
    eventName: string;
    eventDate: string;
    venueName: string;
    status: string;
}

// Интерфейс для ответа с сервера со списком бронирований
export interface UserBookingResponse {
    username: string;
    myBookingResponses: Booking[];
}

// Интерфейс для запроса на отмену бронирования
export interface CancelBookingRequest {
    bookingId: number;
}

class BookingService {

    getMyBookings() {
        return api.get<UserBookingResponse>(`${API_URL}/my`);
    }

    cancelBooking(data: CancelBookingRequest) {
        return api.post<string>(`${API_URL}/cancel`, data);
    }
}

export default new BookingService();
