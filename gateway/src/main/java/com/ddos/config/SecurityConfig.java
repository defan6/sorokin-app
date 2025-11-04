package com.ddos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationWebFilter jwtAuthenticationWebFilter;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        return http
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource())) // Добавляем конфигурацию CORS
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
