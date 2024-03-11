package com.example.to_do.controller;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.to_do.dtos.LoginDTO;
import com.example.to_do.dtos.UsuarioDTO;
import com.example.to_do.dtos.UsuarioResponseDTO;
import com.example.to_do.entidades.Usuario;

import com.example.to_do.service.TokenService;
import com.example.to_do.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  TokenService  tokenService; 

    

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.cadastrarUsuario(usuarioDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(usuarioResponseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(usuarioResponseDTO);
    }

    


    

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO)  {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= 
      new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha());
       Authentication authenticate= this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
       var usuario= (Usuario)authenticate.getPrincipal();
       return tokenService.gerarToken(usuario);
       
    
}
}
