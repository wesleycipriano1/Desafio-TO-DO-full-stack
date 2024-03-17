package com.example.to_do.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.to_do.excecao.UsuarioNaoAutenticadoException;
import com.example.to_do.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TarefaService tarefaService;

    @Test
    void testGetAuthenticatedUserUsuarioNaoAutenticado() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new Object());

        try {
            tarefaService.getAuthenticatedUser();
            fail("Esperava que UsuarioNaoAutenticadoException fosse lançada");
        } catch (UsuarioNaoAutenticadoException e) {
            // Teste passou
        }
    }
    @Test
    void testGetAuthenticatedUserUsuarioAutenticado() {
        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        try {
            UserDetails result = tarefaService.getAuthenticatedUser();
            assertEquals(userDetails, result, "O resultado deve ser igual ao usuário autenticado");
        } catch (UsuarioNaoAutenticadoException e) {
            fail("Não esperava que UsuarioNaoAutenticadoException fosse lançada");
        }
    } 
}
