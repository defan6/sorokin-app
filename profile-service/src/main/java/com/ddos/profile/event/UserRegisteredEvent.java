package com.ddos.profile.event;


import jakarta.validation.constraints.NotNull;

public record UserRegisteredEvent(
        @NotNull Long userId,
        @NotNull String username,
        @NotNull String fullName,
        @NotNull String role
) {
}
