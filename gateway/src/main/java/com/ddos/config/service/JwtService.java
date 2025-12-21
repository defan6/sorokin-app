package com.ddos.config.service;


import com.ddos.properties.GatewayConfigurationProperties;
import com.ddos.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class JwtService {

    private static final String BEARER_PREFIX = "Bearer ";


    private final JwtUtil jwtUtil;
    private final WebClient authServiceClient;

    public JwtService(JwtUtil jwtUtil, GatewayConfigurationProperties properties) {
        this.jwtUtil = jwtUtil;
        this.authServiceClient = WebClient.builder()
                .baseUrl(properties.getAuthServiceUrl())
                .build();
    }


    public Mono<Void> validateToken(String token){
        if(!jwtUtil.isTokenValid(token)){
            return Mono.error(new JwtException("Invalid token structure or signature"));
        }

        return authServiceClient.post()
                .uri("/api/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, createAuthHeader(token))
                .retrieve()
                .onStatus(HttpStatus.UNAUTHORIZED::equals,
                       clientResponse -> Mono.error(new JwtException("Token is blacklisted or invalid")))
                .bodyToMono(Void.class);

    }

    private String createAuthHeader(String token) {
        return BEARER_PREFIX + token;
    }


    public String extractToken(ServerHttpRequest request){
        return jwtUtil.extractToken(request);
    }

    public Long getUserId(String token){
        return jwtUtil.extractUserId(token);
    }

    public String getUsername(String token){
        return jwtUtil.extractUsername(token);
    }


    public List<String> getUserRoles(String token){
        return jwtUtil.extractRoles(token);
    }
}
