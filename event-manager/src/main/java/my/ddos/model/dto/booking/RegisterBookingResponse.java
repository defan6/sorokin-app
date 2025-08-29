package my.ddos.model.dto.booking;

import my.ddos.enums.BookingStatus;

import java.time.LocalDateTime;

public record RegisterBookingResponse(Long id, String eventName, String username, BookingStatus bookingStatus, LocalDateTime registeredAt, String message) {
}
