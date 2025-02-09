package com.fullstackbootcamp.capstoneBackend.notifications.bo;

import com.fullstackbootcamp.capstoneBackend.notifications.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;

import java.util.HashMap;
import java.util.Map;

public class NotificationRequest {
    private String message;
    private NotificationType type; // "NEW_LOAN_REQUEST", "NEW_MESSAGE", "BANKER_CALL", "LOAN_STATUS_CHANGE", "COUNTER_OFFER"
    private String senderName; // Username, not first name
    private String recipientName; // Username, not first name
    private Roles senderRole;  // "BANKER" or "BUSINESS_OWNER"
    private String businessName; // Bank or business name
    private Map<String, Object> additionalData = new HashMap<>();

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
}
