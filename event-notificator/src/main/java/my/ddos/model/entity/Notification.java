package my.ddos.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.ddos.enums.BookingStatus;
import my.ddos.enums.ReadStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String message;
    private Long eventId;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;
    private LocalDateTime registeredAt;
}
