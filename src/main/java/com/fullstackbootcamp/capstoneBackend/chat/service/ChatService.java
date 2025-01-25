package com.fullstackbootcamp.capstoneBackend.chat.service;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatDTO;
import com.fullstackbootcamp.capstoneBackend.chat.dto.MessageDTO;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.chat.entity.MessageEntity;
import com.fullstackbootcamp.capstoneBackend.chat.repository.ChatRepository;
import com.fullstackbootcamp.capstoneBackend.chat.repository.MessageRepository;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatMapperService chatMapperService;
    private final JwtUtil jwtUtil;

    public ChatService(
            ChatRepository chatRepository,
            MessageRepository messageRepository,
            UserRepository userRepository,
            ChatMapperService chatMapperService,
            JwtUtil jwtUtil) {
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatMapperService = chatMapperService;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public ChatDTO createChat(String authHeader, Long chatTargetId) {
        String username = jwtUtil.extractUserUsernameFromToken(authHeader);
        UserEntity sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        UserEntity chatTarget = userRepository.findById(chatTargetId)
                .orElseThrow(() -> new RuntimeException("Chat target not found"));

        // Check if chat already exists between the two users
        ChatEntity existingChat;
        if(sender.getRole().equals(Roles.BANKER)) {
            existingChat = chatRepository.findByBankerAndBusinessOwner(sender.getId(), chatTarget.getId());
        } else {
            existingChat = chatRepository.findByBankerAndBusinessOwner(chatTarget.getId(), sender.getId());
        }
        if (existingChat != null) {
            return chatMapperService.convertToDTO(existingChat);
        }

        ChatEntity chat = new ChatEntity();
        // If sender role is banker, set bankerId, otherwise set businessOwnerId
        if (sender.getRole().equals(Roles.BANKER)) {
            chat.setBanker(sender);
            chat.setBusinessOwner(chatTarget);
        } else {
            chat.setBanker(chatTarget);
            chat.setBusinessOwner(sender);
        }
        ChatEntity chatEntity = chatRepository.save(chat);
        return chatMapperService.convertToDTO(chatEntity);
    }

    @Transactional
    public void sendMessage(Long chatId, String authHeader, String messageContent) {
        String username = jwtUtil.extractUserUsernameFromToken(authHeader);
        ChatEntity chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        UserEntity sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        MessageEntity message = new MessageEntity();
        message.setChat(chat);
        message.setSender(sender);
        message.setCharacters(messageContent);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        chat.setLastMessage(message);
        chatRepository.save(chat);
    }

    public ChatDTO getChatMessages(Long chatId) {
        Optional<ChatEntity> chatEntity = chatRepository.findById(chatId);
        return chatMapperService.convertToDTO(chatEntity.get());
    }
}