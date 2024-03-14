package com.example.to_do.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.to_do.entidades.Usuario;
import com.example.to_do.excecao.SenhaInvalidaException;
import com.example.to_do.excecao.UsuarioNaoEncontradoException;
import com.example.to_do.repository.UsuarioRepository;

@Service
public class LoginService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario verificarUsuario(String email, String senha) {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(email);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("senha que vem da tela " +senha + "---->senha que vem do banco "+usuario.getSenha());
        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new SenhaInvalidaException("Senha inválida");
        }

        return usuario;
    }
}
