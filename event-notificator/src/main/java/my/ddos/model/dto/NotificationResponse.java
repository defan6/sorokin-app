package my.ddos.model.dto;

import my.ddos.enums.BookingStatus;
import my.ddos.enums.ReadStatus;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String message,
        Long eventId,
        BookingStatus bookingStatus,
        ReadStatus readStatus,
        LocalDateTime registeredAt
) {
}
