package com.example.to_do.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.to_do.repository.UsuarioRepository;
import com.example.to_do.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterToken extends OncePerRequestFilter {


    @Autowired
    public UsuarioRepository usuarioRepository;

    @Autowired
    public TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token;
        var authorizationHeader= request.getHeader("authorization");

        if (authorizationHeader!= null) {
            token=authorizationHeader.replace("Bearer ", "");
            if (token != null && !token.isEmpty()) {
            var subject= this.tokenService.getSubject(token);
            var usuario= this.usuarioRepository.findByEmail(subject);
            var authentication=new UsernamePasswordAuthenticationToken(usuario, null,usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
        }
    }
        filterChain.doFilter(request, response);
    }

}
