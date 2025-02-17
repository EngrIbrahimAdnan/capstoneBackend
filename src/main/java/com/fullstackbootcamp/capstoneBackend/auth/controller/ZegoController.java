package com.fullstackbootcamp.capstoneBackend.auth.controller;

import com.fullstackbootcamp.capstoneBackend.auth.service.ZegoTokenService;
import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.auth.util.TokenServerAssistant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;


@RequestMapping("/zego")
@RestController
public class ZegoController {
    private final JwtUtil jwtUtil;

    @Value("${myapp.zego.app.id}")
    private long appId;

    @Value("${myapp.zego.server.secret}")
    private String serverSecret;

    public ZegoController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/token")
    public ResponseEntity<String> getZegoToken(@RequestHeader("Authorization") String authHeader) {
        try {
            int effectiveTimeInSeconds = 3000;
            String userId = jwtUtil.extractUserUsernameFromToken(authHeader);

            if (userId == null) {
                throw new RuntimeException("Failed to extract user ID from token");
            }

            // You can customize the payload as needed
            String payload = String.format("{\"userId\":\"%s\"}", userId);

            // Generate the token
            TokenServerAssistant.TokenInfo token = TokenServerAssistant.generateToken04(
                    appId,
                    userId,
                    serverSecret,
                    effectiveTimeInSeconds,
                    payload
            );

            // Check if token generation was successful
            if (token.error == null || token.error.code == TokenServerAssistant.ErrorCode.SUCCESS) {
                return ResponseEntity.ok(token.data);
            } else {
                throw new RuntimeException("Failed to generate Zego token: " + token.error.message);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating token: " + e.getMessage());
        }
    }
}