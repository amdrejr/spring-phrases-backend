package com.amdrejr.phrases.services.security;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.amdrejr.phrases.dto.security.UserCredentials;
import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.exceptions.customExceptions.UserInexistentOrInvalidPassword;
import com.amdrejr.phrases.services.RoleService;
import com.amdrejr.phrases.services.UserService;

// Serviço responsável por autenticar o usuário
@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    // Encriptador de senhas
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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

    // criar conta
    public String signup(UserCredentials userCredentials) {
        User newUser = new User();
        
        newUser.setUsername(userCredentials.getUsername());
        newUser.setPassword(encoder.encode(userCredentials.getPassword()));
        newUser.setRoles(List.of(roleService.findById(1))); 
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
        
        userService.save(newUser);

        return signin(userCredentials);
    }

    public Boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
