package com.ddos.config;

import com.ddos.properties.GatewayConfigurationProperties;
import com.ddos.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
    private final WebClient authServiceClient;

    public JwtAuthenticationWebFilter(JwtUtil jwtUtil, WebClient.Builder webClientBuilder, GatewayConfigurationProperties properties) {
        this.jwtUtil = jwtUtil;
        this.authServiceClient = webClientBuilder.baseUrl(properties.getAuthServiceUrl()).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register/user")) {
            return chain.filter(exchange);
        }
        ServerHttpRequest request = exchange.getRequest();
        String token = jwtUtil.extractToken(request);
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (token != null && jwtUtil.validateToken(token)) {
            log.info("GATEWAY: Extracted Token: {}", token);
            log.info("GATEWAY: Token syntax is valid.");

            return authServiceClient.post()
                    .uri("/api/auth/validate")
                    .header("Authorization", authHeader)
                    .retrieve()
                    .onStatus(HttpStatus.UNAUTHORIZED::equals,
                            response -> Mono.error(new RuntimeException("Token is blacklisted by Auth Service")))
                    .bodyToMono(Void.class)
                    .then(Mono.defer(() -> {
                        Long userId = jwtUtil.extractUserId(token);
                        String username = jwtUtil.extractUsername(token);
                        List<String> roles = jwtUtil.extractRoles(token);
                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                        log.info("GATEWAY: User ID from token: {}", userId);
                        log.info("GATEWAY: Username from token: {}", username);
                        log.info("GATEWAY: Roles from token: {}", roles);
                        log.info("GATEWAY: Granted Authorities: {}", authorities);

                        Authentication authentication =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", String.valueOf(userId))
                                .header("X-Username", username)
                                .header("X-User-Roles", String.join(",", roles))
                                .build();

                        log.info("GATEWAY: Setting Authentication in ReactiveSecurityContextHolder.");
                        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                    }))
                    .onErrorResume(RuntimeException.class, e -> {
                        log.error("GATEWAY: Authentication process error: {}", e.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        return chain.filter(exchange);
    }
}
