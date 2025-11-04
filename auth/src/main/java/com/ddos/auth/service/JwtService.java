package com.ddos.auth.service;


import com.ddos.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtUtil jwtUtil;

    public String createJwtToken(String username, Set<String> roles){
        return jwtUtil.generateToken(username, roles);
    }

}
