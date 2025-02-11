package com.fullstackbootcamp.capstoneBackend.chat.dto;

public class BusinessDTO {
    private String businessName;
    private Long chatId;
    private String profilePicture;
    private String lastMessage;

    public BusinessDTO(String businessName, Long chatId, String profilePicture, String lastMessage) {
        this.businessName = businessName;
        this.chatId = chatId;
        this.profilePicture = profilePicture;
        this.lastMessage = lastMessage;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
