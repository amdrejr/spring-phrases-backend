package com.amdrejr.phrases.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.amdrejr.phrases.services.security.FilterToken;

// Configuração de segurança
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private FilterToken filter;

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of(
            "http://localhost:3000/",
            "http://localhost:4200/",
            "http://localhost:3000/**",
            "http://localhost:4200/**"
        ));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);

        http
            .exceptionHandling( exception -> exception.authenticationEntryPoint(authEntryPoint) )
            .csrf(csrf -> csrf.disable())
            .sessionManagement( session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers("auth", "auth/**").permitAll()
                    .anyRequest().authenticated()
            )
            .cors( cors -> cors.configurationSource(request -> corsConfig) )
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
