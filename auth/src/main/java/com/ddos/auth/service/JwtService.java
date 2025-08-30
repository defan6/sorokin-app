package com.ddos.auth.service;


import com.ddos.auth.model.dto.login.LoginRequest;
import com.ddos.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    public String createJwtToken(String username, Set<String> roles){
        return jwtUtil.generateToken(username, roles);
    }

}
