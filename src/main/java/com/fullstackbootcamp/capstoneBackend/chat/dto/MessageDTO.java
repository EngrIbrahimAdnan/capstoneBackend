package com.fullstackbootcamp.capstoneBackend.chat.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String message;
    private String senderFirstName;
    private LocalDateTime sentAt;
    private Boolean isYou;

    public MessageDTO(Long id, String message, String sentBy, LocalDateTime sentAt, Boolean isYou) {
        this.id = id;
        this.message = message;
        this.senderFirstName = sentBy;
        this.sentAt = sentAt;
        this.isYou = isYou;
    }

    public Boolean getIsYou() {
        return isYou;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }
}
