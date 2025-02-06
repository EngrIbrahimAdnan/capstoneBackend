package com.fullstackbootcamp.capstoneBackend.notifications.config;

import java.security.Principal;
import java.util.Map;

import com.fullstackbootcamp.capstoneBackend.auth.bo.CustomUserDetails;
import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtUtil jwtUtil;

    public CustomHandshakeHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        try {
            String token = request.getURI().getQuery().split("token=")[1];
            // Since we're getting raw token from query param, we don't need to handle Bearer prefix
            Claims claims = jwtUtil.validateToken(token);  // Call validateToken directly
            String username = claims.getSubject();

            if (username != null) {
                return new Principal() {
                    @Override
                    public String getName() {
                        return username;
                    }
                };
            }
        } catch (Exception e) {
            // Log the error but don't throw it
            // This prevents the websocket connection from failing due to auth errors
            e.printStackTrace();
        }

        // If no authenticated user, return null (WebSocket will reject unauthorized connection)
        return null;
    }
}