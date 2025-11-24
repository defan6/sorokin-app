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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        validator.validateRegisterRequest(registerRequest);
        Auth auth = authMapper.toAuth(registerRequest);
        auth.getRoles().add("ROLE_USER");
        auth.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Auth savedAuth = authRepository.save(auth);
        EventRegisterUser eventRegisterUser = authMapper.toEventRegisterUser(savedAuth.getId(), "ROLE_USER", savedAuth);
        kafkaRegisterProducer.sendMessageToRegisterTopic(eventRegisterUser);
        return authMapper.toRegisterResponse(savedAuth);
    }

    @Override
    @Transactional
    public RegisterResponse registerAdmin(RegisterRequest registerRequest) {
        validator.validateRegisterRequest(registerRequest);
        Auth auth = authMapper.toAuth(registerRequest);
        auth.getRoles().add("ROLE_ADMIN");
        auth.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        Auth savedAuth = authRepository.save(auth);
        EventRegisterUser eventRegisterUser = authMapper.toEventRegisterUser(savedAuth.getId(), "ROLE_ADMIN", savedAuth);
        log.info("event-register-admin : {}", eventRegisterUser);
        kafkaRegisterProducer.sendMessageToRegisterTopic(eventRegisterUser);
        return authMapper.toRegisterResponse(savedAuth);
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        validator.validateLoginRequest(loginRequest);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String username = authentication.getName();
        Auth auth = authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
        Set<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        String jwt = jwtService.createJwtToken(auth.getId(), username, roles);
        return new LoginResponse(username, roles, jwt);
    }

    @Override
    public void changeRole(EventChangedRole eventChangedRole) {
        Auth auth = authRepository.findByUsername(eventChangedRole.username()).orElseThrow();
        auth.getRoles().add(eventChangedRole.role());
        authRepository.save(auth);
    }
}
