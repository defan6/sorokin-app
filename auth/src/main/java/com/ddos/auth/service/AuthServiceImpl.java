package com.ddos.auth.service;

import com.ddos.auth.kafka.controller.KafkaRegisterProducer;
import com.ddos.auth.kafka.event.EventChangedRole;
import com.ddos.auth.kafka.event.EventRegisterUser;
import com.ddos.auth.mapper.AuthMapper;
import com.ddos.auth.model.dto.login.LoginRequest;
import com.ddos.auth.model.dto.login.LoginResponse;
import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.model.dto.register.RegisterResponse;
import com.ddos.auth.model.entity.Auth;
import com.ddos.auth.repository.AuthRepository;
import com.ddos.auth.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final JwtService jwtService;


    private final AuthValidator validator;

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthMapper authMapper;

    private final AuthenticationManager authenticationManager;

    private final KafkaRegisterProducer kafkaRegisterProducer;


    @Override
    @Transactional
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        log.info("Started register default user...");
        validator.validateRegisterRequest(registerRequest);
        Auth auth = authMapper.toAuth(registerRequest);
        auth.getRoles().add("ROLE_USER");
        auth.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        EventRegisterUser eventRegisterUser = authMapper.toEventRegisterUser("ROLE_USER", auth);
        kafkaRegisterProducer.sendMessageToRegisterTopic(eventRegisterUser);
        log.info("Send new default user to kafka: {}", eventRegisterUser);
        Auth saved = authRepository.save(auth);
        log.info("Default user was registered!");
        return authMapper.toRegisterResponse(saved);
    }

    @Override
    @Transactional
    public RegisterResponse registerAdmin(RegisterRequest registerRequest) {
        log.info("Started register admin...");
        validator.validateRegisterRequest(registerRequest);
        Auth auth = authMapper.toAuth(registerRequest);
        auth.getRoles().add("ROLE_ADMIN");
        auth.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        EventRegisterUser eventRegisterUser = authMapper.toEventRegisterUser("ROLE_ADMIN", auth);
        kafkaRegisterProducer.sendMessageToRegisterTopic(eventRegisterUser);
        log.info("Send new admin to kafka: {}", eventRegisterUser);
        Auth saved = authRepository.save(auth);
        log.info("Admin was registered!");
        return authMapper.toRegisterResponse(saved);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        validator.validateLoginRequest(loginRequest);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        log.info("Started authenticate: " + loginRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String username = authentication.getName();
        Set<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toSet());
        String jwt = jwtService.createJwtToken(username, roles);
        log.info("Successfully authenticate: " + loginRequest.getUsername());
        return new LoginResponse(username, roles, jwt);
    }

    @Override
    public void changeRole(EventChangedRole eventChangedRole) {
        log.info("Starting change role for " + eventChangedRole.username());
        Auth auth = authRepository.findByUsername(eventChangedRole.username()).orElseThrow();
        auth.getRoles().add(eventChangedRole.role());
        log.info("Successfully changed role for " + eventChangedRole.username() + " by " + eventChangedRole.changedBy());
        authRepository.save(auth);
    }
}
