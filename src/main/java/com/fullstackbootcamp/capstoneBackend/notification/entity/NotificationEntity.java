package com.fullstackbootcamp.capstoneBackend.notification.entity;


import com.fullstackbootcamp.capstoneBackend.chat.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.notification.converter.JsonConverter;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "sender_first_name")
    private String senderFirstName;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_role", nullable = false)
    private Roles senderRole;

    @Column(name = "business_name") // Bank or Business
    private String businessName;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Convert(converter = JsonConverter.class)
    @Column(name = "additional_data", columnDefinition = "json")
    private Map<String, Object> additionalData = new HashMap<>();

    public NotificationEntity() {
    }

    public NotificationEntity(String message, NotificationType type, String senderName, String senderFirstName, String recipientName, Roles senderRole, String businessName, boolean isRead, LocalDateTime createdAt, Map<String, Object> additionalData) {
        this.message = message;
        this.type = type;
        this.senderName = senderName;
        this.senderFirstName = senderFirstName;
        this.recipientName = recipientName;
        this.senderRole = senderRole;
        this.businessName = businessName;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.additionalData = additionalData;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Roles getSenderRole() {
        return senderRole;
    }

    public void setSenderRole(Roles senderRole) {
        this.senderRole = senderRole;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
}
