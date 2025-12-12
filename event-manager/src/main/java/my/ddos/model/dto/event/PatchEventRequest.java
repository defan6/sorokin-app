package my.ddos.model.dto.event;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Optional;

public record PatchEventRequest(@NotNull Optional<String> title, @NotNull Optional<String> description, @NotNull Optional<LocalDateTime> eventDate) {
}
