package com.ddos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers("/event-manager/api/venues/admin/**").hasRole("ADMIN")
                                .pathMatchers("/event-manager/api/events/admin/**").hasRole("ADMIN")
                                .pathMatchers("/event-manager/api/users/admin/**").hasRole("ADMIN")
                                .pathMatchers("/event-manager/api/bookings/admin/**").hasRole("ADMIN")
                                .pathMatchers("/event-manager/api/**").authenticated()
                                .anyExchange().permitAll())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}
