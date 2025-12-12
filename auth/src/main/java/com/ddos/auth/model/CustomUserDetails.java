package com.ddos.auth.model;

import com.ddos.auth.model.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Auth auth;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return auth.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .toList();
    }

    @Override
    public String getPassword() {
        return auth.getPassword();
    }

    @Override
    public String getUsername() {
        return auth.getUsername();
    }

    public Long getId(){
        return auth.getId();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
