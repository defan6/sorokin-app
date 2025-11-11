package my.ddos.model.dto.booking;

import my.ddos.enums.BookingStatus;

import java.time.LocalDateTime;

public record MyBookingResponse(
        Long id,
        String eventName,
        LocalDateTime eventDate,
        String venueName,
        BookingStatus status
) {
}
