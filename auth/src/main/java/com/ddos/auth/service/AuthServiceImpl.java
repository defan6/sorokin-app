package com.ddos.auth.service;

import com.ddos.auth.mapper.AuthMapper;
import com.ddos.auth.model.dto.login.LoginRequest;
import com.ddos.auth.model.dto.login.LoginResponse;
import com.ddos.auth.model.dto.register.RegisterRequest;
import com.ddos.auth.model.dto.register.RegisterResponse;
import com.ddos.auth.model.entity.Auth;
import com.ddos.auth.repository.AuthRepository;
import com.ddos.auth.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
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
public class AuthServiceImpl implements AuthService {


    private final JwtService jwtService;


    private final AuthValidator validator;

    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthMapper authMapper;

    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        validator.validateRegisterRequest(registerRequest);
        Auth auth = authMapper.toAuth(registerRequest);
        auth.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return authMapper.toRegisterResponse(authRepository.save(auth));
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        validator.validateLoginRequest(loginRequest);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String username = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
        String jwt = jwtService.createJwtToken(username, roles);
        return new LoginResponse(username, roles, jwt);
    }
}
