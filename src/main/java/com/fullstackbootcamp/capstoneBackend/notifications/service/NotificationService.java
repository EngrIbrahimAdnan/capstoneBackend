package com.fullstackbootcamp.capstoneBackend.notifications.service;

import com.fullstackbootcamp.capstoneBackend.notifications.bo.NotificationRequest;
import com.fullstackbootcamp.capstoneBackend.notifications.bo.NotificationTest;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendNotification(NotificationRequest request) {
        // TODO
        messagingTemplate.convertAndSendToUser(request.getRecipientName().toString(), "/topic/queue", request);
    }

}