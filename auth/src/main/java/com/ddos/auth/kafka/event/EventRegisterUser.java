package com.ddos.auth.kafka.event;

import jakarta.validation.constraints.NotNull;

public record EventRegisterUser(
        @NotNull Long userId,
        @NotNull String username,
        @NotNull String fullName,
        @NotNull String role
) {
}
