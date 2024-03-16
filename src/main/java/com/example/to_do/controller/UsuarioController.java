package com.example.to_do.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.to_do.dtos.LoginDTO;
import com.example.to_do.dtos.UsuarioDTO;
import com.example.to_do.dtos.UsuarioResponseDTO;
import com.example.to_do.entidades.Usuario;
import com.example.to_do.excecao.SenhaInvalidaException;
import com.example.to_do.excecao.UsuarioJaExisteException;
import com.example.to_do.excecao.UsuarioNaoEncontradoException;
import com.example.to_do.service.LoginService;
import com.example.to_do.service.TokenService;
import com.example.to_do.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    public LoginService loginService;

    @Operation(summary = "cadastra um usuario se não exitir email cadastrado e a senha atingir os criterios ", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "cadastro com sucesso"),
            @ApiResponse(responseCode = "403", description = "acesso negado"),
            @ApiResponse(responseCode = "409", description = "Email já existe ou senha invalida-->letra maisculas,minusculas,numeros e caracteres especiais"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioResponseDTO usuarioResponseDTO = usuarioService.registrarUsuario(usuarioDTO);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(usuarioResponseDTO.getId()).toUri();
            return ResponseEntity.created(uri).body(usuarioResponseDTO);
        } catch (UsuarioJaExisteException | SenhaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @Operation(summary = "Realiza o login de um usuario e retorna um token de acesso", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "logim com sucesso"),
            @ApiResponse(responseCode = "401", description = "usuario ou senha não encontradas"),
            @ApiResponse(responseCode = "403", description = "acesso negado"),
            @ApiResponse(responseCode = "500", description = "Erro no servidor"),
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Usuario usuario = loginService.verificarUsuario(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok(tokenService.gerarToken(usuario));
        } catch (UsuarioNaoEncontradoException | SenhaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
