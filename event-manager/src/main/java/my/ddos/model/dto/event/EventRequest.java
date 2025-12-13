package my.ddos.model.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record
EventRequest(@NotBlank String title,
                           @NotBlank String description,
                           @NotNull LocalDateTime eventDate,
                           @NotNull @Positive Long venueId
) {
}
