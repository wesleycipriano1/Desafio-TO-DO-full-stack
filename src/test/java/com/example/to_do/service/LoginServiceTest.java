package com.example.to_do.service;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.to_do.entidades.Usuario;
import com.example.to_do.excecao.SenhaInvalidaException;
import com.example.to_do.excecao.UsuarioNaoEncontradoException;
import com.example.to_do.repository.UsuarioRepository;
@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private LoginService loginService;

    
@Test
public void testVerificarUsuarioNaoExisteEmail() {
    
    String email = "test@example.com";
    String senha = "password";

    when(usuarioRepository.findUsuarioByEmail(email)).thenReturn(null);
    try {
        loginService.verificarUsuario(email, senha);

        
        fail("Esperava que UsuarioNaoEncontradoException fosse lançada");
    } catch (UsuarioNaoEncontradoException e) {
       
    }
}
@Test
public void testVerificarUsuarioSenhaInvalida() {

    String email = "test@example.com";
    String senha = "password";
    String senhaIncorreta = "senhaIncorreta";

    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

    when(usuarioRepository.findUsuarioByEmail(email)).thenReturn(usuario);
    try {
        loginService.verificarUsuario(email, senhaIncorreta);

        fail("Esperava que SenhaInvalidaException fosse lançada");
    } catch (SenhaInvalidaException e) {
       
    }
}
@Test

public void testVerificarUsuarioSenhaValida() {
    
    String email = "test@example.com";
    String senha = "password";
    String senhainvalida="uma senha que não seja a do usuario";

    Usuario usuario = new Usuario();
    usuario.setEmail(email);
    usuario.setSenha(new BCryptPasswordEncoder().encode(senha));

    when(usuarioRepository.findUsuarioByEmail(email)).thenReturn(usuario);
    try {
        loginService.verificarUsuario(email, senha);
    } catch (SenhaInvalidaException e) {
        fail("Não esperava que SenhaInvalidaException fosse lançada");
    }
}




}
