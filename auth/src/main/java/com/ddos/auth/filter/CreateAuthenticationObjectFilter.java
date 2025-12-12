package com.ddos.auth.filter;

import com.ddos.auth.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CreateAuthenticationObjectFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(request.getServletPath().startsWith("/api/auth/login") || request.getServletPath().startsWith("/api/auth/register/user")) {
            filterChain.doFilter(request, response);
            return;
        }
        String userIdHeader = request.getHeader("X-User-Id");
        String usernameHeader = request.getHeader("X-Username");
        String userRolesHeader = request.getHeader("X-User-Roles");

        log.info("AUTH_SERVICE: Received X-User-Id: {}", userIdHeader);
        log.info("AUTH_SERVICE: Received X-Username: {}", usernameHeader);
        log.info("AUTH_SERVICE: Received X-User-Roles: {}", userRolesHeader);


        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (userRolesHeader != null && !userRolesHeader.isEmpty()) {
            authorities = Arrays.stream(userRolesHeader.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        }

        log.info("AUTH_SERVICE: Created Authorities from headers: {}", authorities);

        Authentication authentication = new UsernamePasswordAuthenticationToken(usernameHeader, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("AUTH_SERVICE: Authentication object set in SecurityContextHolder: {}", authentication);
        filterChain.doFilter(request, response);
    }
}
