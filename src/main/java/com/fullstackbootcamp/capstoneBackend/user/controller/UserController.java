package com.fullstackbootcamp.capstoneBackend.user.controller;

import com.fullstackbootcamp.capstoneBackend.auth.dto.LoadUsersResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
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

    // Refer to this when you wish to extract information from token
    @PostMapping("/token-info")
    public ResponseEntity<?> extractTokenInfo(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        /* NOTE:
            To ensure the access token is provided and NOT the refresh token,
            the refresh token, which contains less information, is only ever used to
            generate an access token
         */
        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
            return ResponseEntity.ok(Map.of("Error"," refresh token is required when Access token is provided"));
        }

        // All Obtainable information from access token
        String id = jwt.getId(); // token id
        String username = jwt.getSubject(); // username of the user
        Object type = jwt.getClaims().get("type"); // the type of token (ACCESS or REFRESH)
        Object role = jwt.getClaims().get("roles"); // roles: ADMIN, BANKER, BUSINESS_OWNER
        Object bank = jwt.getClaims().get("bank"); // bank: a value from the different enum banks

        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Instant issue = jwt.getIssuedAt(); // issue date of token
        Instant expire = jwt.getExpiresAt(); // expire date of token

        // Could be used for role specific endpoints
        if (role.equals(Roles.ADMIN.name())) {
            return ResponseEntity.ok("Welcome, Admin!");
        }

        return ResponseEntity.ok(Map.of(
                "Token Id", id,
                "username", username,
                "civil Id", civilId,
                "roles", role,
                "bank", bank,
                "type", type,
                "issue", issue,
                "expire", expire));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<>
}