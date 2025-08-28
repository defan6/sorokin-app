package my.ddos.model.dto.booking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.ddos.enums.BookingStatus;
import my.ddos.model.entity.Event;
import my.ddos.model.entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private User user;
    private Event event;
    private BookingStatus bookingStatus;
    private LocalDateTime registeredAt;
}
