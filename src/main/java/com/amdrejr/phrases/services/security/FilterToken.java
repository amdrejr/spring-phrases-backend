package com.amdrejr.phrases.services.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.amdrejr.phrases.entities.User;
import com.amdrejr.phrases.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterToken extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
        ) throws ServletException, IOException {
            String token;

            var authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.replace("Bearer ", "");
                String subject = "";
                User user;
                Authentication authentication;

                try {
                    subject = this.jwtService.getSubject(token);
                    user = this.userRepository.findByUsername(subject);
                    authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                } catch (Exception e) {
                    // System.out.println("Token inválido, " + e.getMessage());
                    filterChain.doFilter(request, response);
                    return;
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } 

            filterChain.doFilter(request, response);
    }

}
