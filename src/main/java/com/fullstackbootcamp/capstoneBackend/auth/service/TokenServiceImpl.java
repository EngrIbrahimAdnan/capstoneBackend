package com.fullstackbootcamp.capstoneBackend.auth.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenServiceImpl implements TokenService {
    private final Map<String, Boolean> tokenBlacklist = new ConcurrentHashMap<>();

    public void invalidateToken(String tokenId) {
        tokenBlacklist.put(tokenId, true);
    }

    public boolean isTokenValid(String tokenId) {
        return !tokenBlacklist.containsKey(tokenId);
    }
}

