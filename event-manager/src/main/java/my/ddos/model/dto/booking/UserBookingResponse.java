package my.ddos.model.dto.booking;

import java.util.List;

public record UserBookingResponse(String username, List<BookingResponse> bookingResponses) {
}
