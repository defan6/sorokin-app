package com.ddos.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to auth-service: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri("http://auth:8080"))
                .route("event-manager", r -> r
                        .path("/api/manager/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to event-manager: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri("http://event-manager:8080"))
                .route("event-notificatior", r -> r
                        .path("/api/notificator/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to event-notification: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri("http://event-notificator:8080"))
                .build();
    }
}

