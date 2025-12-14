package my.ddos;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
//ужно явно регистрировать фильтр в SecurityConfig с помощью addFilterBefore(...)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class CreateAuthenticationObjectFilter extends OncePerRequestFilter {

    private static final String X_USER_ID_HEADER = "X-User-Id";

    private static final String X_USER_ROLES_HEADER = "X-User-Roles";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userIdHeader = request.getHeader(X_USER_ID_HEADER);
        String userRolesHeader = request.getHeader(X_USER_ROLES_HEADER);

        try {
            Long userId = Long.parseLong(userIdHeader);
            List<SimpleGrantedAuthority> grantedAuthorities = Arrays.stream(userRolesHeader.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, null, grantedAuthorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        } catch (NumberFormatException e) {
            log.warn("Invalid X-User-Id header format: {}", userIdHeader);
        }

        filterChain.doFilter(request, response);
    }
}
