package my.ddos.model.dto.event;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public record PatchEventRequest(String title, String description, LocalDateTime eventDate) {
}
