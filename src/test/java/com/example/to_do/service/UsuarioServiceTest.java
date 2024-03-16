package com.example.to_do.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.to_do.dtos.UsuarioDTO;
import com.example.to_do.dtos.UsuarioResponseDTO;
import com.example.to_do.entidades.Usuario;
import com.example.to_do.excecao.SenhaInvalidaException;
import com.example.to_do.excecao.UsuarioJaExisteException;
import com.example.to_do.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void testRegistrarUsuarioComSucesso() {
        String email = "test@example.com";
        String senha = "Password1@";
        String nome = "Test User";

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail(email);
        usuarioDTO.setSenha(senha);
        usuarioDTO.setNome(nome);

        when(usuarioRepository.findByEmail(email)).thenReturn(null);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
        usuario.setNome(nome);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        try {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.registrarUsuario(usuarioDTO);
            assertNotNull(usuarioResponseDTO);
            assertEquals(email, usuarioResponseDTO.getEmail());
            assertEquals(nome, usuarioResponseDTO.getNome());
        } catch (UsuarioJaExisteException | SenhaInvalidaException e) {
            fail("Não esperava que exceções fossem lançadas");
        }
    }

    @Test
public void testRegistrarUsuarioEmailJaExisteFalha() {
    String email = "test@example.com";
    String senha = "Password1@";
    String nome = "Test User";

    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setEmail(email);
    usuarioDTO.setSenha(senha);
    usuarioDTO.setNome(nome);

    Usuario usuarioExistente = new Usuario();
    usuarioExistente.setEmail(email);
    usuarioExistente.setSenha(new BCryptPasswordEncoder().encode(senha));
    usuarioExistente.setNome(nome);

    when(usuarioRepository.findByEmail(email)).thenReturn(usuarioExistente);

    try {
        usuarioService.registrarUsuario(usuarioDTO);
        fail("Esperava que UsuarioJaExisteException fosse lançada");
    } catch (UsuarioJaExisteException e) {
        
    } catch (SenhaInvalidaException e) {
        fail("Não esperava que SenhaInvalidaException fosse lançada");
    }
}
@Test
public void testRegistrarUsuarioSenhaNaoAtendeRequisitosFalha() {
    String email = "test@example.com";
    String senha = "passwor"; 
    String nome = "Test User";

    UsuarioDTO usuarioDTO = new UsuarioDTO();
    usuarioDTO.setEmail(email);
    usuarioDTO.setSenha(senha);
    usuarioDTO.setNome(nome);

    when(usuarioRepository.findByEmail(email)).thenReturn(null);

    try {
        usuarioService.registrarUsuario(usuarioDTO);
        fail("Esperava que SenhaInvalidaException fosse lançada");
    } catch (SenhaInvalidaException e) {
    
    } catch (UsuarioJaExisteException e) {
        fail("Não esperava que UsuarioJaExisteException fosse lançada");
    }
}


}
