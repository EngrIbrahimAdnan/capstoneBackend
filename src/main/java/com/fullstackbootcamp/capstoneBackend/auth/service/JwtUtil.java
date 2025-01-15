package com.fullstackbootcamp.capstoneBackend.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private String secretKey = "myStrongSecretKey1234567890myStrongSecretKey"; // This should be a secure key stored securely

    // Generate a token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token, String username) {
        String user = getUsernameFromToken(token);
        return (user.equals(username) && !isTokenExpired(token));
    }

    // Extract username from the token
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date getExpirationDateFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}

