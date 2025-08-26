package my.ddos.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//CREATE TABLE events (
//    id BIGSERIAL PRIMARY KEY,
//    title VARCHAR(255) NOT NULL,
//    description TEXT,
//    event_date TIMESTAMP NOT NULL,
//    capacity INT,
//    location_id BIGINT NOT NULL,
//    created_by BIGINT NOT NULL,
//    CONSTRAINT fk_location FOREIGN KEY (location_id) REFERENCES locations(id) ON DELETE CASCADE,
//    CONSTRAINT fk_organizer FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE
//);
@Entity
@Table(name = "events")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "venue_id")
    private Venue venue;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;
}
