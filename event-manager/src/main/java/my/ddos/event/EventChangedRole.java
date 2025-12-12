package my.ddos.event;

import jakarta.validation.constraints.NotBlank;

public record EventChangedRole(String username,
                               String changedBy,
                               String role) {
}
