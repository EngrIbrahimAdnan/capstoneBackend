
package com.fullstackbootcamp.capstoneBackend.auth.service;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.auth.util.TokenServerAssistant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ZegoTokenService {

    private final JwtUtil jwtUtil;

    public ZegoTokenService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    //${myapp.zego.app.id:12345} for quick testing
    @Value("${myapp.zego.app.id}")
    private long appId;

    //myapp.zego.server.secret:placeholder-secret} for quick testing
    @Value("${myapp.zego.server.secret}")
    private String serverSecret;

    public String generateToken(String authHeader) {
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
            return token.data;
        } else {
            throw new RuntimeException("Failed to generate Zego token: " + token.error.message);
        }
    }
}
