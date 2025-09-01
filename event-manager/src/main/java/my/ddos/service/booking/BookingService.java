package my.ddos.service.booking;

import my.ddos.model.dto.booking.*;

import java.util.List;

public interface BookingService {
    UserBookingResponse getMyBookings(String username);

    List<UserBookingResponse> getAllBookings();

    RegisterBookingResponse createBooking(BookingRequest registerBookingRequest, String username);

    void cancelBooking(String username, CancelBookingRequest cancelBookingRequest);
}
