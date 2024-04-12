package com.amdrejr.phrases.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amdrejr.phrases.dto.security.AuthResponse;
import com.amdrejr.phrases.dto.security.UserCredentials;
import com.amdrejr.phrases.exceptions.customExceptions.UsernameAlreadyExistsExceprion;
import com.amdrejr.phrases.services.UserService;
import com.amdrejr.phrases.services.security.AuthenticationService;

// Endpoint para autenticação
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody UserCredentials userCredentials) {
        String token = authenticationService.signin(userCredentials);
        return new AuthResponse(token);
    }

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody UserCredentials userCredentials) {
        if(userService.findByUsername(userCredentials.getUsername()) != null) {
            throw new UsernameAlreadyExistsExceprion("Username already exists.");
        }

        String token = authenticationService.signup(userCredentials);
        return new AuthResponse(token);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody AuthResponse token) {
        Boolean isValid = false;

        try {
            isValid = authenticationService.validateToken(token.getToken());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(isValid);
        }

        return ResponseEntity.ok().body(isValid);
    }
}
