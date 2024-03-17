package com.example.to_do.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.to_do.entidades.Usuario;

@Service
public class TokenService {
    // mudar para chave aleatoria

    private final String secreto = "chave";

    public String gerarToken(Usuario usuario) {
        return JWT.create().withIssuer("Usuario").withSubject(usuario.getEmail()).withClaim("id", usuario.getId())
                .withExpiresAt(LocalDateTime.now().plusMinutes(60)// lembrar de mudar pra 15 depois dos testes
                        .toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256(secreto));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(secreto)).withIssuer("Usuario").build().verify(token).getSubject();

    }

}
