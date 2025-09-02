package my.ddos.model.dto;

import lombok.Data;
import my.ddos.enums.BookingStatus;
import my.ddos.enums.ReadStatus;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private String message;
    private Long eventId;
    private BookingStatus bookingStatus;
    private ReadStatus readStatus;
    private LocalDateTime registeredAt;
//    String message, Long eventId, BookingStatus bookingStatus, LocalDateTime registeredAt, Long userId
}
