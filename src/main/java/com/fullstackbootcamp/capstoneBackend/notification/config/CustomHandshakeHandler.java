package com.fullstackbootcamp.capstoneBackend.notification.config;

import java.security.Principal;
import java.util.Map;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomHandshakeHandler.class);
    private final JwtUtil jwtUtil;

    public CustomHandshakeHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String token = null;
        try {
            String query = request.getURI().getQuery();
            logger.debug("Query string: {}", query);

            if (query != null && query.contains("token=")) {
                token = query.split("token=")[1];
                // Handle potential additional query parameters
                if (token.contains("&")) {
                    token = token.split("&")[0];
                }

                logger.debug("Extracted token: {}", token);
                Claims claims = jwtUtil.validateToken(token);
                String username = claims.getSubject();

                if (username != null) {
                    logger.debug("Authentication successful for user: {}", username);
                    return () -> username;
                }
            }
            logger.error("No token found in query string");
        } catch (Exception e) {
            logger.error("Authentication failed. Token: {}. Error: {}", token, e.getMessage());
        }
        return null;
    }
}