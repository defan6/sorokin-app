package my.ddos.service.auth;

import my.ddos.model.dto.login.LoginRequest;
import my.ddos.model.dto.login.LoginResponse;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);
}
