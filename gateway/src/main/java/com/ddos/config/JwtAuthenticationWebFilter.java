package com.ddos.config;

import com.ddos.config.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@Order(1)
public class JwtAuthenticationWebFilter implements WebFilter {

    private static final String USER_ID_HEADER = "X-User-Id";

    private static final String USER_USERNAME_HEADER = "X-Username";

    private static final String USER_ROLES_HEADER = "X-User-Roles";


    private final JwtService jwtService;

    public JwtAuthenticationWebFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = jwtService.extractToken(request);

        if (token == null) {
            return chain.filter(exchange);
        }

        log.info("GATEWAY: Extracted Token: {}", token);
        log.info("GATEWAY: Token syntax is valid.");

        return jwtService.validateToken(token)
                .then(chain.filter(exchange
                                .mutate()
                                .request(createMutatedRequest(token, exchange))
                                .build()
                        )
                )
                .contextWrite(ReactiveSecurityContextHolder
                        .withAuthentication(createAuthentication(token)))
                .onErrorResume(WebClientResponseException.Forbidden.class, e -> {
                    log.error("GATEWAY: Authentication process error: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }


    private ServerHttpRequest createMutatedRequest(String token, ServerWebExchange exchange) {
        Long userId = jwtService.getUserId(token);
        String username = jwtService.getUsername(token);
        List<String> roles = jwtService.getUserRoles(token);


        log.info("GATEWAY: User ID from token: {}", userId);
        log.info("GATEWAY: Username from token: {}", username);
        log.info("GATEWAY: Roles from token: {}", roles);

        return exchange.getRequest()
                .mutate()
                .header(USER_ID_HEADER, String.valueOf(userId))
                .header(USER_USERNAME_HEADER, username)
                .header(USER_ROLES_HEADER, String.join(",", roles))
                .build();
    }


    private Authentication createAuthentication(String token) {
        String username = jwtService.getUsername(token);
        List<String> roles = jwtService.getUserRoles(token);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
