package com.ddos.auth.service;

import com.ddos.auth.model.entity.Auth;
import com.ddos.auth.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthRepository authRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername was called for " + username);
        Auth auth = authRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Auth not found with username: " + username));
        List<GrantedAuthority> authorities = auth.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        log.info("Create UserDetails for " + username);
        return new org.springframework.security.core.userdetails.User(auth.getUsername(),
                auth.getPassword(), authorities);
    }
}
