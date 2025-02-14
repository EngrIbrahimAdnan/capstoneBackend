package com.fullstackbootcamp.capstoneBackend.notification.repository;

import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByRecipientNameOrderByCreatedAtDesc(String recipientName);

    List<NotificationEntity> findByRecipientNameAndIsReadOrderByCreatedAtDesc(String recipientName, boolean isRead);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.recipientName = :recipientName")
    void markAllAsRead(String recipientName);

    long countByRecipientNameAndIsRead(String recipientName, boolean isRead);
}