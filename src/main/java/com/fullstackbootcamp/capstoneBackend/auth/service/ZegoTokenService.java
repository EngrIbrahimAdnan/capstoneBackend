package com.fullstackbootcamp.capstoneBackend.auth.service;

import org.springframework.stereotype.Service;

@Service
public interface ZegoTokenService {
    String generateToken(String authHeader);
}
