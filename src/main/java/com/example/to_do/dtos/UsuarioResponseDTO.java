package com.example.to_do.dtos;

import com.example.to_do.entidades.Usuario;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

@Getter
@Setter
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    @Hidden
    public static UsuarioResponseDTO from(Usuario usuario) {
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setNome(usuario.getNome());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        return usuarioResponseDTO;
    }
}
