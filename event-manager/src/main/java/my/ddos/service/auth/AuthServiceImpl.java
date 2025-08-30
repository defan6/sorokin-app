package my.ddos.service.auth;

import lombok.RequiredArgsConstructor;
import my.ddos.model.dto.login.LoginRequest;
import my.ddos.model.dto.login.LoginResponse;
import my.ddos.model.dto.register.RegisterRequest;
import my.ddos.model.dto.register.RegisterResponse;
import my.ddos.model.entity.User;
import my.ddos.repository.UserRepository;
import my.ddos.service.jwt.JwtService;
import my.ddos.service.user.UserService;
import my.ddos.validator.AuthValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final AuthValidator validator;
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        validator.validateRegisterRequest(registerRequest);
        return userService.save(registerRequest);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        validator.validateLoginRequest(loginRequest);
        User user = userRepository.findByUsername(loginRequest.getUsername()).get();
        String jwt = jwtService.createJwtToken(loginRequest);
        return LoginResponse.builder()
                .id(user.getId())
                .accessToken(jwt)
                .roles(user.getUserRoles())
                .username(user.getUsername())
                .build();
    }
}
