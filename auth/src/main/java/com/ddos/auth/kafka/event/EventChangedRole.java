package com.ddos.auth.kafka.event;

public record EventChangedRole(String username,
                               String changedBy,
                               String role) {
}
