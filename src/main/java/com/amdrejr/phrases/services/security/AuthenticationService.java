package com.amdrejr.phrases.services.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.exceptions.customExceptions.UserInexistentOrInvalidPassword;
import com.amdrejr.phrases.security.UserCredentials;

// Serviço responsável por autenticar o usuário
@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    public String signin(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();

        // Criando um objeto de autenticação
        Authentication auth;

        try {
            auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new UserInexistentOrInvalidPassword("Usuário inexistente ou senha inválida!");
        }

        var user = (User) auth.getPrincipal();
        
        return jwtService.generateToken(user);
    }

    public Authentication authenticate(UsernamePasswordAuthenticationToken auth) {
        return authManager.authenticate(auth);
    }
}
