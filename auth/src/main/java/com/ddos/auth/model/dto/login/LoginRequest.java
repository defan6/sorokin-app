package com.ddos.auth.model.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username size must be between ${min} and ${max}")
    private String username;


    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, max = 30, message = "Password size must be between ${min} and ${max}")
    private String password;
}
