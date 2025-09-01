package com.ddos.config;

import com.ddos.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@Order(1)
public class JwtAuthenticationWebFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationWebFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        log.info("Start web filter");
        // Пути, которые не требуют JWT
        if (path.startsWith("/api/auth/")) {
            return chain.filter(exchange);
        }
        log.info("We are in web filter");
        ServerHttpRequest request = exchange.getRequest();
        String token = jwtUtil.extractToken(request);

        if (token != null) {
            boolean valid = jwtUtil.validateToken(token);
            log.info("Token valid: {}", valid);
        }


        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token);
            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            log.info("User {} with roles from token: {}", username, roles);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-Username", username)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build())
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }

        return chain.filter(exchange);
    }
}
