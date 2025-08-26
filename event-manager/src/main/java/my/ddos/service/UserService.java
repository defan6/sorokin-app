package my.ddos.service;

import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;

public interface UserService {
    RegisterResponse save(RegisterRequest registerRequest);
}
