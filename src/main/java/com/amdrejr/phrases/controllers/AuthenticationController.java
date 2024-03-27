package com.amdrejr.phrases.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amdrejr.phrases.dto.AuthResponse;
import com.amdrejr.phrases.security.UserCredentials;
import com.amdrejr.phrases.services.security.AuthenticationService;

// Endpoint para autenticação
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthResponse authenticate(@RequestBody UserCredentials userCredentials) {
        String token = authenticationService.signin(userCredentials);
        return new AuthResponse(token);
    }
}
