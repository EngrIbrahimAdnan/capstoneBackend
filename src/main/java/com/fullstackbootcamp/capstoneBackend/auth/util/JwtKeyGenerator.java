package com.fullstackbootcamp.capstoneBackend.auth.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtKeyGenerator {
    private final SecretKey secretKey;
    public JwtKeyGenerator() {

        /* NOTE:
            Use a fixed, Base64-decoded key for persistence.
            less secure but will save us a lot of time from logging in each time
         */
        String base64Key = "uE6C2zpZl+TOAt+GnC0L9aHl+4iLr+PY7hZpL6Nz7O8=";
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        // TODO: uncomment previous secure method once before demo for robustness
        // this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    public SecretKey getSecretKey() {
        return secretKey;
    }
}

