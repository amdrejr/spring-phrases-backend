package com.amdrejr.phrases.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint privado, apenas autenticado acessa.
@RestController
@RequestMapping("/private")
public class PrivateController {
    
    @GetMapping
    public String getMessage() {
        return "Olá da API privada!";
    }
}
