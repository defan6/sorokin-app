package com.ddos.auth.service;

import com.ddos.auth.kafka.event.EventChangedRole;
import com.ddos.auth.model.dto.login.LoginRequest;
import com.ddos.auth.model.dto.login.LoginResponse;
import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.model.dto.register.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(RegisterRequest registerRequest);
    RegisterResponse registerAdmin(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    void changeRole(EventChangedRole eventChangedRole);
}
