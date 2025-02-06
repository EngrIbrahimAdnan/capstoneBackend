package com.fullstackbootcamp.capstoneBackend.notifications.dto;

import com.fullstackbootcamp.capstoneBackend.notifications.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;

import java.time.LocalDateTime;
import java.util.Map;

public class NotificationPayload {
    private Long id;
    private String message;
    private NotificationType type;
    private Long senderId;
    private Long recipientId;
    private Roles senderRole;  // "BANKER" or "BUSINESS_OWNER"
    private LocalDateTime timestamp;
    private boolean read;
    private Map<String, Object> additionalData;  // For type-specific data

    // Getters, setters, and constructor

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Roles getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(Roles senderRole) {
        this.senderRole = senderRole;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    public void setId(Long id) {
    }

    public Long getId() {
        return id;
    }
}