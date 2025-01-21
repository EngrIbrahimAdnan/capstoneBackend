package com.fullstackbootcamp.capstoneBackend.auth.util;

import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil(JwtKeyGenerator jwtKeyGenerator) {
        this.secretKey = jwtKeyGenerator.getSecretKey();
    }

    public String generateAccessToken(UserEntity user) {
        String tokenId = UUID.randomUUID().toString();
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(user.getUsername())
                .claim("roles", user.getRole().name())
                .claim("civilId", user.getCivilId())
                .claim("type", "access") // specify the type to be access
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(UserEntity user) {
        String tokenId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setId(tokenId)
                .setSubject(user.getUsername())
                .claim("type", "refresh") // specify the type to refresh
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 604800000)) // 7 days
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid token", e);
        }
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        Claims claims = validateToken(refreshToken);
        return claims.getExpiration().after(new Date()); // Ensure it's not expired
    }

}
