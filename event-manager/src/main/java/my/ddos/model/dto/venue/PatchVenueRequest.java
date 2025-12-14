package my.ddos.model.dto.venue;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Optional;

public record PatchVenueRequest(String name, String address, @Positive Long capacity) {
}
