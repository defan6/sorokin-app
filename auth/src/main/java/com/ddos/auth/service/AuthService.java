package com.ddos.auth.service;

import com.ddos.auth.model.dto.login.LoginRequest;
import com.ddos.auth.model.dto.login.LoginResponse;
import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.model.dto.register.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
