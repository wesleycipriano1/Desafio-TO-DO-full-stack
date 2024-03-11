package com.example.to_do.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.to_do.dtos.LoginDTO;
import com.example.to_do.entidades.Usuario;
import com.example.to_do.repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario verificarLogin(LoginDTO loginDTO) {
        Usuario usuario = usuarioRepository.findAllByEmail(loginDTO.getEmail())
            .stream()
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginDTO.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }
    
        return usuario;
    }
    
}

