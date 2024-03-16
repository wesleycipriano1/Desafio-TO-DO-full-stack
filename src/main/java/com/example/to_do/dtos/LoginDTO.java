package com.example.to_do.dtos;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO implements UserDetails {
    private String email;
    private String senha;

    @Hidden
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
    @Hidden
    @Override
    public String getPassword() {
        return senha;
    }
    @Hidden
    @Override
    public String getUsername() {
        return email;
    }
    @Hidden
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Hidden
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Hidden
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Hidden
    @Override
    public boolean isEnabled() {
        return true;
    }
}
