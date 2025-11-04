package my.ddos.model.dto;

import my.ddos.enums.BookingStatus;

import java.time.LocalDateTime;

public record EventBooking(String message, Long eventId, BookingStatus bookingStatus, LocalDateTime registeredAt, String username) {
}
