package my.ddos.model.dto.kafka;

import java.time.LocalDateTime;

public record EventChangedEvent(Long eventId, String title, String description, LocalDateTime eventDate, LocalDateTime updatedAt, String changedBy) {
}
