package com.fullstackbootcamp.capstoneBackend.auth.service;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers("/auth/**").permitAll() // Public route for login
                .anyRequest().authenticated() // All other routes require authentication
                .and()
                .sessionManagement().disable(); // Disable session management for stateless authentication

        return http.build();
    }
}

