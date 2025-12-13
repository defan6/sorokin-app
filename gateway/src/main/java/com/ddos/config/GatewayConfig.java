package com.ddos.config;

import com.ddos.properties.GatewayConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final GatewayConfigurationProperties properties;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r
                        .path("/api/auth/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to auth-service: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri(properties.getAuthServiceUrl()))
                .route("event-manager", r -> r
                        .path("/api/manager/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to event-manager: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri(properties.getEventManagerServiceUrl()))
                .route("event-notificator", r -> r
                        .path("/api/notificator/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to event-notification: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri(properties.getEventNotificatorServiceUrl()))
                .route("profile-service", r -> r
                        .path("/api/profiles/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to profile-service: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri(properties.getProfileServiceUrl()))
                .route("file-storage-service", r -> r
                        .path("/api/files/**")
                        .filters(f -> f.filter((exchange, chain) -> {
                            System.out.println("[Gateway] Routing to file-storage-service: " + exchange.getRequest().getURI());
                            return chain.filter(exchange);
                        }))
                        .uri(properties.getFileStorageServiceUrl())
                )
                .build();
    }
}

