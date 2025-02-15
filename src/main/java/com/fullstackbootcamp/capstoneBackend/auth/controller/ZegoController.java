package com.fullstackbootcamp.capstoneBackend.auth.controller;

import com.fullstackbootcamp.capstoneBackend.auth.service.ZegoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ZegoController {

    @Autowired
    private ZegoTokenService zegoTokenService;

    @GetMapping("/zego/token")
    public ResponseEntity<String> getZegoToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = zegoTokenService.generateToken(authHeader);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating token: " + e.getMessage());
        }
    }
}