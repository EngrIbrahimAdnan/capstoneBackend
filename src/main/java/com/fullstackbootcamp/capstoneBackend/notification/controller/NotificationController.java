package com.fullstackbootcamp.capstoneBackend.notification.controller;

import com.fullstackbootcamp.capstoneBackend.notification.bo.NotificationRequest;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.notification.service.NotificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    public NotificationController(SimpMessagingTemplate messagingTemplate,
                                  NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public List<NotificationEntity> notifications(@RequestHeader("Authorization") String authHeader) {
        return notificationService.getAllNotificationsForUser(authHeader);
    }

}