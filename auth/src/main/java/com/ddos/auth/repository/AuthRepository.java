package com.ddos.auth.repository;

import com.ddos.auth.model.entity.Auth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUsername(String username);

    boolean existsByUsername(@NotBlank(message = "Username cannot be empty") @Size(min = 3, max = 20, message = "Username size must be between ${min} and ${max}") String username);
}
