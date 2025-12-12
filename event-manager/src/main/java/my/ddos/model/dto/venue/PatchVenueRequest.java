package my.ddos.model.dto.venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record PatchVenueRequest(@NotNull Optional<String> name, @NotNull Optional<String> address, @NotNull Optional<Long> capacity) {
}
