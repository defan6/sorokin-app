package my.ddos.model.dto.venue;

import jakarta.validation.constraints.NotBlank;

import java.util.Optional;

public record PatchVenueRequest(Optional<String> name, Optional<String> address, Optional<Long> capacity) {
}
