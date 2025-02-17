package com.fullstackbootcamp.capstoneBackend.notification.repository;

import com.fullstackbootcamp.capstoneBackend.chat.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // Find all notifications for a user, ordered by creation date descending (newest first)
    List<NotificationEntity> findByRecipientOrderByCreatedAtDesc(UserEntity recipient);

    // Find all unread notifications for a user
    List<NotificationEntity> findByRecipientAndIsReadFalseOrderByCreatedAtDesc(UserEntity recipient);

    // Count unread notifications for a user
    long countByRecipientAndIsReadFalse(UserEntity recipient);

    // Optional: Find notifications with pagination
    Page<NotificationEntity> findByRecipient(UserEntity recipient, Pageable pageable);

    // Optional: Find notifications by type for a user
    List<NotificationEntity> findByRecipientAndTypeOrderByCreatedAtDesc(UserEntity recipient, NotificationType type);

    List<NotificationEntity> findByRecipientNameOrderByCreatedAtDesc(String recipientName);

    List<NotificationEntity> findByRecipientNameAndIsReadOrderByCreatedAtDesc(String recipientName, boolean isRead);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.recipientName = :recipientName")
    void markAllAsRead(String recipientName);

    long countByRecipientNameAndIsRead(String recipientName, boolean isRead);
}