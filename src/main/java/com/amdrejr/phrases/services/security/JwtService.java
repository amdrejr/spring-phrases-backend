package com.amdrejr.phrases.services.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.amdrejr.phrases.entities.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

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
   
    public String getSubject(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(chaveUltraSecreta))
            .withIssuer("login-api")
            .build()
            .verify(token) // se tiver expirado, lança TokenExpiredException
            .getSubject();
    }

    public Boolean validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(chaveUltraSecreta);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("login-api")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        // Verificar se o token não está expirado
        return jwt.getExpiresAt().after(new Date());
    }
}   
