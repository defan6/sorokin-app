package my.ddos.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.ddos.enums.BookingStatus;

import java.time.LocalDateTime;

//CREATE TABLE registrations (
//    id BIGSERIAL PRIMARY KEY,
//    event_id BIGINT NOT NULL,
//    user_id BIGINT NOT NULL,
//    status VARCHAR(50) NOT NULL, -- REGISTERED / CANCELED
//    registered_at TIMESTAMP DEFAULT now(),
//    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
//    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
//);
@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "booking_status")
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
}
