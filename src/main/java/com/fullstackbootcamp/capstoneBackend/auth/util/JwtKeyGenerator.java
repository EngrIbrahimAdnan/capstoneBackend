package com.fullstackbootcamp.capstoneBackend.auth.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtKeyGenerator {
    private final SecretKey secretKey;

    public JwtKeyGenerator() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}

