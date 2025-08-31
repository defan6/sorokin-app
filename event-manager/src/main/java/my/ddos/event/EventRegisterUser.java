package my.ddos.event;

import jakarta.validation.constraints.NotNull;

public record EventRegisterUser(@NotNull String username, @NotNull String fullName, @NotNull String password) {
}
