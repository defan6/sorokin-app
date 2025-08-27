package my.ddos.service;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.login.LoginRequest;
import my.ddos.model.dto.login.LoginResponse;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;
import my.ddos.model.entity.User;
import my.ddos.repository.UserRepository;
import my.ddos.validator.AuthValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserService userService;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuthValidator validator;
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        validator.validateRegisterRequest(registerRequest);
        return userService.save(registerRequest);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        validator.validateLoginRequest(loginRequest);
        User user = userRepository.findByUsername(loginRequest.getUsername());
        String jwt = jwtService.createJwtToken(loginRequest);
        return LoginResponse.builder()
                .id(user.getId())
                .accessToken(jwt)
                .roles(user.getUserRoles())
                .username(user.getUsername())
                .build();
    }
}
