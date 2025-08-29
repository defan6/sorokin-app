package my.ddos.model.dto.event;

import java.time.LocalDateTime;

public record EventResponse(Long id, String title, String description, LocalDateTime eventDate, Long venueId, Long organizerId) {
}
