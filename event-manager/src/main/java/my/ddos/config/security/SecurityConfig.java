package my.ddos.config.security;

import my.ddos.CreateAuthenticationObjectFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.PathMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CreateAuthenticationObjectFilter createAuthenticationObjectFilter, PathMatcher pathMatcher) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(createAuthenticationObjectFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


    // если указывать путь и hasRole("...") то в случае если такой роли не будет сработает НАШ CustomAuthenticationEntryPoint
    // если на методе стоит PreAuthorize("hasRole('...')") то этот CustomAccessDeniedHandler не будет отрабатывать.
    // в этом случае нужно использовать GlobalExceptionHandler


    @Bean
    public CreateAuthenticationObjectFilter createAuthenticationObjectFilter() throws Exception {
        return new CreateAuthenticationObjectFilter();
    }
}
