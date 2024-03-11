package com.example.to_do.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.to_do.entidades.Usuario;
@Service
public class TokenService {
    final String secreto="chave";

    public String gerarToken(Usuario usuario) {
       return JWT.create().withIssuer("Usuario").withSubject(usuario.getEmail()).withClaim("id", usuario.getId())
       .withExpiresAt(LocalDateTime.now().plusMinutes(15).toInstant(ZoneOffset.of("-03:00")))
       .sign(Algorithm.HMAC256(secreto));
    }

}