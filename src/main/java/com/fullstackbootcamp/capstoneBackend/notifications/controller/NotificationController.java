package com.fullstackbootcamp.capstoneBackend.notifications.controller;

import com.fullstackbootcamp.capstoneBackend.notifications.bo.NotificationTest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Listens for messages on /app/chat
     * Then re-broadcasts them to /topic/messages
     */
    @MessageMapping("/chat")
    public NotificationTest send(NotificationTest notificationTest) {
        messagingTemplate.convertAndSendToUser(
                "ab", "/topic/queue", notificationTest);
        return notificationTest;
    }
}