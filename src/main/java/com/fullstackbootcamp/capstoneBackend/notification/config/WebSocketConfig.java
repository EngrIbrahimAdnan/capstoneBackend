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
        // Use the Bean instead of creating a new instance
        registry.addEndpoint("/ws")
                .setHandshakeHandler(customHandshakeHandler())  // Use the Bean method
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();

        // Add a native WebSocket endpoint for React Native
        registry.addEndpoint("/ws")
                .setHandshakeHandler(customHandshakeHandler())
                .setAllowedOrigins("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request,
                                                   ServerHttpResponse response,
                                                   WebSocketHandler wsHandler,
                                                   Map<String, Object> attributes) throws Exception {
                        System.out.println("Handshake attempt from: " + request.getRemoteAddress());
                        System.out.println("Request URI: " + request.getURI());
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request,
                                               ServerHttpResponse response,
                                               WebSocketHandler wsHandler,
                                               Exception exception) {
                        if (exception != null) {
                            System.out.println("Handshake failed: " + exception.getMessage());
                        } else {
                            System.out.println("Handshake successful");
                        }
                    }
                });
    }
}