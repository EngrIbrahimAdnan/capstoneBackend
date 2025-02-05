package com.fullstackbootcamp.capstoneBackend.notifications.controller;

import com.fullstackbootcamp.capstoneBackend.notifications.bo.NotificationTest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    /**
     * Listens for messages on /app/chat
     * Then re-broadcasts them to /topic/messages
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public NotificationTest send(NotificationTest notificationTest) {
        return notificationTest;
    }
}