package com.fullstackbootcamp.capstoneBackend.chat.repository;

import com.fullstackbootcamp.capstoneBackend.chat.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByChatId(Long chatId);
    List<MessageEntity> findBySenderId(Long senderId);
    List<MessageEntity> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT m FROM MessageEntity m WHERE m.chat.id = :chatId ORDER BY m.timestamp DESC")
    List<MessageEntity> findLatestMessagesByChat(Long chatId);
}