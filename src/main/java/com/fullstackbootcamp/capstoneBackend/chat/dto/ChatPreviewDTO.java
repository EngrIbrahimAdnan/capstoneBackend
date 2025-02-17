package com.fullstackbootcamp.capstoneBackend.chat.dto;

import java.io.Serializable;

public class ChatPreviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private UserDTO otherUser;
    private String latestMessage;
    private boolean latestMessageSenderIsYou;

    public ChatPreviewDTO() {}

    public ChatPreviewDTO(Long id,
                          UserDTO otherUser,
                          String latestMessage,
                          boolean latestMessageSenderIsYou) {
        this.id = id;
        this.otherUser = otherUser;
        this.latestMessage = latestMessage;
        this.latestMessageSenderIsYou = latestMessageSenderIsYou;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public boolean isLatestMessageSenderIsYou() {
        return latestMessageSenderIsYou;
    }

    public void setLatestMessageSenderIsYou(boolean latestMessageSenderIsYou) {
        this.latestMessageSenderIsYou = latestMessageSenderIsYou;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(UserDTO otherUser) {
        this.otherUser = otherUser;
    }
}
