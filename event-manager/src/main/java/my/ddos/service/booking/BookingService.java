package my.ddos.service.booking;

import my.ddos.model.dto.booking.BookingRequest;
import my.ddos.model.dto.booking.BookingResponse;
import my.ddos.model.dto.booking.RegisterBookingResponse;
import my.ddos.model.dto.booking.UserBookingResponse;

import java.util.List;

public interface BookingService {
    UserBookingResponse getMyBookings();

    List<UserBookingResponse> getAllBookings();

    RegisterBookingResponse createBooking(BookingRequest registerBookingRequest);
}
