package com.fullstackbootcamp.capstoneBackend.notification.controller;

import com.fullstackbootcamp.capstoneBackend.notification.bo.NotificationRequest;
import com.fullstackbootcamp.capstoneBackend.notification.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public NotificationController(SimpMessagingTemplate messagingTemplate,
                                  NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    /**
     * Listens for messages on /app/chat
     * Then re-broadcasts them to /topic/queue
     */
    @MessageMapping("/chat")
    public void send(NotificationRequest request) {
        try {
            notificationService.sendNotification(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}