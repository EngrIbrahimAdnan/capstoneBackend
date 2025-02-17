package com.fullstackbootcamp.capstoneBackend.notification.config;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtUtil jwtUtil;

    @Bean
    public CustomHandshakeHandler customHandshakeHandler() {
        return new CustomHandshakeHandler(jwtUtil);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue", "/banker", "/businessOwner");
        registry.setUserDestinationPrefix("/user");
        registry.setApplicationDestinationPrefixes("/app");

        // Add this line to configure STOMP heartbeat
        registry.setPreservePublishOrder(true);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(customHandshakeHandler())
                .setAllowedOrigins("http://localhost:3000", "*")  // Combined origins
                .withSockJS()
                .setWebSocketEnabled(true)
                .setSessionCookieNeeded(false);

        // For native WebSocket
        registry.addEndpoint("/ws")
                .setHandshakeHandler(customHandshakeHandler())
                .setAllowedOrigins("*");
    }
}