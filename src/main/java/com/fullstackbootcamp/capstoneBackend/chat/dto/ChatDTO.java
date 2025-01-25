package com.fullstackbootcamp.capstoneBackend.chat.dto;

import java.util.List;

public class ChatDTO {
    private Long id;
    private UserDTO banker;
    private UserDTO businessOwner;
    private List<MessageDTO> messages;

    public ChatDTO() {
    }

    public ChatDTO(Long id, UserDTO banker, UserDTO businessOwner, List<MessageDTO> messages) {
        this.id = id;
        this.banker = banker;
        this.businessOwner = businessOwner;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public UserDTO getBanker() {
        return banker;
    }

    public UserDTO getBusinessOwner() {
        return businessOwner;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }
}
