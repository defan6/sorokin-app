package com.ddos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/api/auth/**").permitAll()
                                .pathMatchers("/api/manager/venues/admin/**").hasRole("ADMIN")
                                .pathMatchers("/api/manager/events/admin/**").hasRole("ADMIN")
                                .pathMatchers("/api/manager/users/admin/**").hasRole("ADMIN")
                                .pathMatchers("/api/manager/bookings/admin/**").hasRole("ADMIN")
                                .pathMatchers("/api/manager/**").authenticated()
                                .pathMatchers("/api/notificator/**").authenticated()
                                .anyExchange().permitAll())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .addFilterAt(jwtAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION) //was problem here
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}
