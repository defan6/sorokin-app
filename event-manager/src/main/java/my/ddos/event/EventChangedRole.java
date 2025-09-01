package my.ddos.event;

import jakarta.validation.constraints.NotBlank;

public record EventChangedRole(@NotBlank String username,
                               @NotBlank String changedBy,
                               @NotBlank String role) {
}
