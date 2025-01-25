package com.fullstackbootcamp.capstoneBackend.chat.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateChatRequest {
    @NotNull(message = "Chat target user ID is required")
    private Long chatTargetId;
}