package com.fullstackbootcamp.capstoneBackend.notifications.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // The broker will carry messages back to the client on destinations prefixed with /topic or /queue
        registry.enableSimpleBroker("/topic", "/queue");
        // All messages sent from the client with destinations starting with /app
        // will be routed to @MessageMapping methods in @Controller classes
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint for the client to connect
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                // If you want to support fallback options like SockJS for browsers that don’t support WebSocket
                .withSockJS();
    }
}