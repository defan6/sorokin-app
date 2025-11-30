package com.ddos.profile.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class ExtractUserIdHeaderFilter extends OncePerRequestFilter {

    private static final String X_USER_ID_HEADER = "X-User-Id";

    private static final String X_USER_ROLES_HEADER = "X-User-Roles";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader(X_USER_ID_HEADER);
        String userRolesHeader = request.getHeader(X_USER_ROLES_HEADER);
        if(userIdHeader != null && !userIdHeader.isEmpty()){
            try {
                Long userId = Long.parseLong(userIdHeader);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if(userRolesHeader != null && !userRolesHeader.isEmpty()){
                    authorities = Arrays.stream(userRolesHeader.split(","))
                            .map(String::trim)
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (NumberFormatException e) {
                log.warn("Invalid X-User-Id header format: {}", userIdHeader);
            }
        }
        filterChain.doFilter(request, response);
    }
}
