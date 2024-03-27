package com.amdrejr.phrases.services.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import com.amdrejr.phrases.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

// @Service
@Component
public class JwtService {

    // TODO: Deixar a chave secreta em um arquivo de configuração
    String chaveUltraSecreta = "secreta";
  
    // Token gerado a partir do JwtEncoder
    // Será usado para autenticar o usuário.
    public String generateToken(User user) {
        return JWT.create()
            .withIssuer("login-api")
            .withSubject(user.getUsername())
            .withClaim("id", user.getId())
            .withExpiresAt( LocalDateTime.now().plusMinutes(60).toInstant(ZoneOffset.of("-03:00")) )
            .sign(Algorithm.HMAC256(chaveUltraSecreta));
    }
   
    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(chaveUltraSecreta))
            .withIssuer("login-api")
            .build()
            .verify(token) // se tiver expirado, lança TokenExpiredException
            .getSubject();
    }
}   
