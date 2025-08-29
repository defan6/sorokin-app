package my.ddos.model.dto.event;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record EventRequest(@NotBlank String title, @NotBlank String description, @NotBlank LocalDateTime eventDate, @NotBlank Long venueId) {
}
