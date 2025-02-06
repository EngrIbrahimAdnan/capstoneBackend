package com.fullstackbootcamp.capstoneBackend.notifications.entity;

import com.fullstackbootcamp.capstoneBackend.notifications.dto.NotificationPayload;
import com.fullstackbootcamp.capstoneBackend.notifications.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class NotificationPayloadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "sender_role", nullable = false)
    private Roles senderRole;  // "BANKER" or "BUSINESS_OWNER"

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean read;

    // Store additional data as JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "additional_data")
    private Map<String, Object> additionalData = new HashMap<>();

    // Constructors
    public NotificationPayloadEntity() {
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    // Copy constructor from DTO
    public NotificationPayloadEntity(NotificationPayload payload) {
        this.message = payload.getMessage();
        this.type = payload.getType();
        this.senderId = payload.getSenderId();
        this.recipientId = payload.getRecipientId();
        this.senderRole = payload.getSenderRole();
        this.timestamp = LocalDateTime.now();
        this.read = false;
        this.additionalData = payload.getAdditionalData();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
