package com.example.to_do.service;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.to_do.dtos.UsuarioDTO;
import com.example.to_do.dtos.UsuarioResponseDTO;
import com.example.to_do.entidades.Usuario;
import com.example.to_do.excecao.SenhaInvalidaException;
import com.example.to_do.excecao.UsuarioJaExisteException;
import com.example.to_do.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    
    public UsuarioResponseDTO registrarUsuario(UsuarioDTO usuarioDTO) {
        UserDetails userDetails = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        if(userDetails != null) {
            throw new UsuarioJaExisteException("Já existe um usuário com este e-mail");
        }

        String senha = usuarioDTO.getSenha();
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=./?]).*$";



        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);

        if (!matcher.matches()) {
            throw new SenhaInvalidaException("A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setSenha(passwordEncoder.encode(senha));

        Usuario savedUser = usuarioRepository.save(usuario);

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(savedUser.getId());
        usuarioResponseDTO.setNome(savedUser.getNome());
        usuarioResponseDTO.setEmail(savedUser.getEmail());

        return usuarioResponseDTO;
    }
}
