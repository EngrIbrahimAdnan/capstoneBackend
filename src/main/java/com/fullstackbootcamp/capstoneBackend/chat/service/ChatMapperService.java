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

    public ChatDTO convertToDTO(ChatEntity chatEntity, String username) {
        if (chatEntity == null) {
            return null;
        }

        // Figure out if the "banker" in this chat is the currently logged-in user
        boolean bankerIsYou = chatEntity.getBanker().getUsername().equals(username);

        return new ChatDTO(
                chatEntity.getId(),
                convertUserToDTO(chatEntity.getBanker(), bankerIsYou),
                convertUserToDTO(chatEntity.getBusinessOwner(), !bankerIsYou),
                convertMessages(chatEntity, username) // Pass username down
        );
    }

    private UserDTO convertUserToDTO(UserEntity user, boolean isYou) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getBank().toString(),
                isYou
        );
    }

    private List<MessageDTO> convertMessages(ChatEntity chatEntity, String username) {
        if (chatEntity.getMessages() == null) {
            return null;
        }
        return chatEntity.getMessages().stream()
                .map(messageEntity -> {
                    boolean senderIsYou = messageEntity.getSender().getUsername().equals(username);

                    return new MessageDTO(
                            messageEntity.getId(),
                            messageEntity.getCharacters(),
                            messageEntity.getSender().getFirstName(),
                            messageEntity.getTimestamp(),
                            senderIsYou
                    );
                })
                .collect(Collectors.toList());
    }

}
