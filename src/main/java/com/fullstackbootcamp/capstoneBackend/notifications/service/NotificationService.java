package com.fullstackbootcamp.capstoneBackend.notifications.service;

import com.fullstackbootcamp.capstoneBackend.notifications.dto.NotificationPayload;
import com.fullstackbootcamp.capstoneBackend.notifications.entity.NotificationPayloadEntity;
import com.fullstackbootcamp.capstoneBackend.notifications.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.notifications.repository.NotificationPayloadRepository;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import jakarta.transaction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationPayloadRepository notificationRepository;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate,
                               NotificationPayloadRepository notificationRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    public void sendBankerNotification(Long bankerId, NotificationPayload payload) {
        NotificationPayloadEntity notification = new NotificationPayloadEntity(payload);
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/banker/" + bankerId, payload);
    }

    public void sendBusinessOwnerNotification(Long businessOwnerId, NotificationPayload payload) {
        NotificationPayloadEntity notification = new NotificationPayloadEntity(payload);
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/business-owner/" + businessOwnerId, payload);
    }

    // Helper methods for different notification types
    public void notifyNewLoanRequest(Long bankerId, Long businessOwnerId, Long loanRequestId) {
        NotificationPayload payload = new NotificationPayload();
        payload.setType(NotificationType.NEW_LOAN_REQUEST);
        payload.setMessage("New loan request received");
        payload.setSenderId(businessOwnerId);
        payload.setRecipientId(bankerId);
        payload.setSenderRole(Roles.BUSINESS_OWNER);

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("loanRequestId", loanRequestId);
        payload.setAdditionalData(additionalData);

        sendBankerNotification(bankerId, payload);
    }

    public List<NotificationPayload> getUnreadNotifications(Long userId) {
        return notificationRepository.findByRecipientIdAndReadFalseOrderByTimestampDesc(userId)
                .stream()
                .map(this::convertToPayload)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId);
    }

    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    private NotificationPayload convertToPayload(NotificationPayloadEntity entity) {
        NotificationPayload payload = new NotificationPayload();
        payload.setId(entity.getId());
        payload.setMessage(entity.getMessage());
        payload.setType(entity.getType());
        payload.setSenderId(entity.getSenderId());
        payload.setRecipientId(entity.getRecipientId());
        payload.setSenderRole(entity.getSenderRole());
        payload.setTimestamp(entity.getTimestamp());
        payload.setRead(entity.isRead());
        payload.setAdditionalData(entity.getAdditionalData());
        return payload;
    }
}