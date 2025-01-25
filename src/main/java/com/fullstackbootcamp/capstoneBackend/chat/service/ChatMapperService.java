package com.fullstackbootcamp.capstoneBackend.chat.service;

import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatDTO;
import com.fullstackbootcamp.capstoneBackend.chat.dto.MessageDTO;
import com.fullstackbootcamp.capstoneBackend.chat.dto.UserDTO;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMapperService {

    public ChatDTO convertToDTO(ChatEntity chatEntity) {
        if (chatEntity == null) {
            return null;
        }

        return new ChatDTO(
                chatEntity.getId(),
                convertUserToDTO(chatEntity.getBanker()),
                convertUserToDTO(chatEntity.getBusinessOwner()),
                convertMessages(chatEntity)
        );
    }

    private UserDTO convertUserToDTO(UserEntity user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getBank().toString()
        );
    }

    List<MessageDTO> convertMessages(ChatEntity chatEntity) {
        if (chatEntity.getMessages() == null) {
            return null;
        }
        return chatEntity.getMessages().stream()
                .map(messageEntity -> new MessageDTO(
                        messageEntity.getId(),
                        messageEntity.getCharacters(),
                        messageEntity.getSender().getFirstName(),
                        messageEntity.getTimestamp()))
                .collect(Collectors.toList());
    }

}
