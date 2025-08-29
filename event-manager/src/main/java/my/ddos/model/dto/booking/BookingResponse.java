package my.ddos.model.dto.booking;

import my.ddos.enums.BookingStatus;

import java.time.LocalDateTime;

public record BookingResponse(Long eventId, BookingStatus bookingStatus, LocalDateTime registeredAt) {
}
