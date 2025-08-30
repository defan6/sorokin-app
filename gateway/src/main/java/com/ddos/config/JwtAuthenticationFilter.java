package com.ddos.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter implements GatewayFilterFactory<JwtAuthenticationFilter.Config> {


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = extractToken(request);

            if(token!=null && validateToken(token)){
                return chain.filter(exchange);
            }
            exchange.getResponse().setRawStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }


    private String extractToken(ServerHttpRequest request){
        String authHeader = request.getHeaders().getFirst("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }

    public static class Config {
        // конфигурационные параметры
    }
}
