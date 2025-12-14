package my.ddos.model.dto.kafka;

import my.ddos.enums.BookingStatus;

import java.time.LocalDateTime;

public record EventBooking(Long eventId, BookingStatus bookingStatus, LocalDateTime registeredAt, String username) {
}
