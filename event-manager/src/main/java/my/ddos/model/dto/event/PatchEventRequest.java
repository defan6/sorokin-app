package my.ddos.model.dto.event;

import java.time.LocalDateTime;
import java.util.Optional;

public record PatchEventRequest(Optional<String> title, Optional<String> description, Optional<LocalDateTime> eventDate) {
}
