package com.fullstackbootcamp.capstoneBackend.chat.service;

import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import com.fullstackbootcamp.capstoneBackend.chat.dto.*;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.chat.entity.MessageEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatMapperService {

    private final BusinessRepository businessRepository;

    public ChatMapperService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

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

        Optional<BusinessEntity> business = businessRepository.findByBusinessOwnerUser(user);

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getBank().name(),
                isYou,
                business.map(businessEntity -> businessEntity.getBusinessNickname()).orElse(null),
                // TODO: Once this is an actual profile picture, put it here
                "",
                user.getUsername()
        );
    }

    public List<MessageDTO> convertMessages(ChatEntity chatEntity, String username) {
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

    public List<ChatPreviewDTO> convertToPreviews(List<ChatEntity> chats, String username) {
        return chats.stream()
                .map(chat -> {

                    // Check who in the chat is "you"
                    boolean bankerIsYou = chat.getBanker() != null
                            && chat.getBanker().getUsername().equals(username);
                    boolean businessOwnerIsYou = chat.getBusinessOwner() != null
                            && chat.getBusinessOwner().getUsername().equals(username);

                    // Determine the other user
                    UserEntity otherUserEntity;
                    if (bankerIsYou) {
                        otherUserEntity = chat.getBusinessOwner();
                    } else if (businessOwnerIsYou) {
                        otherUserEntity = chat.getBanker();
                    } else {
                        // If neither the banker nor the business owner is "you"
                        otherUserEntity = null;
                    }

                    // Convert the other user to a DTO (we mark them as `isYou = false` because they are not the current user)
                    UserDTO otherUserDTO = convertUserToDTO(otherUserEntity, false);

                    // Find the most recent message in this chat
                    MessageEntity latestMessageEntity = chat.getMessages().stream()
                            .max(Comparator.comparing(MessageEntity::getTimestamp))
                            .orElse(null);

                    String latestMessage = null;
                    boolean latestMessageSenderIsYou = false;

                    if (latestMessageEntity != null) {
                        latestMessage = latestMessageEntity.getCharacters();
                        // Check if the latest message’s sender is "you"
                        latestMessageSenderIsYou = latestMessageEntity
                                .getSender()
                                .getUsername()
                                .equals(username);
                    }

                    return new ChatPreviewDTO(
                            chat.getId(),
                            otherUserDTO,
                            latestMessage,
                            latestMessageSenderIsYou
                    );
                })
                .collect(Collectors.toList());
    }
}
