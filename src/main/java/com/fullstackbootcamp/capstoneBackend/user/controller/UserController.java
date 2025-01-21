package com.fullstackbootcamp.capstoneBackend.user.controller;

import com.fullstackbootcamp.capstoneBackend.auth.dto.LoadUsersResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/user/v1")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        String id = jwt.getId();
        String username = jwt.getSubject();

        Object civilId = jwt.getClaims().get("civilId");
        Object role = jwt.getClaims().get("roles");

        Instant issue = jwt.getIssuedAt();
        Instant expire = jwt.getExpiresAt();

        System.out.println(id);
        System.out.println(username);
        System.out.println(role);
        System.out.println(issue);
        System.out.println(expire);


        if (role.equals(Roles.ADMIN.toString())){
            return ResponseEntity.ok("Welcome, Admin!");
        }
        return ResponseEntity.ok(Map.of("civil ID", civilId, "roles", jwt.getClaims().get("roles")));



    }
}