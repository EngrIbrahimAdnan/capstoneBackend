package com.fullstackbootcamp.capstoneBackend.notification.service;

import com.fullstackbootcamp.capstoneBackend.notification.bo.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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