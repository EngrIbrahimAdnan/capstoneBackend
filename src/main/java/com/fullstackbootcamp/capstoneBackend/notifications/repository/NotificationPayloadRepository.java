package com.fullstackbootcamp.capstoneBackend.notifications.repository;

import com.fullstackbootcamp.capstoneBackend.notifications.entity.NotificationPayloadEntity;
import com.fullstackbootcamp.capstoneBackend.notifications.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationPayloadRepository extends JpaRepository<NotificationPayloadEntity, Long> {
    // Find all notifications for a specific recipient
    List<NotificationPayloadEntity> findByRecipientIdOrderByTimestampDesc(Long recipientId);

    // Find unread notifications for a recipient
    List<NotificationPayloadEntity> findByRecipientIdAndReadFalseOrderByTimestampDesc(Long recipientId);

    // Find notifications by type for a recipient
    List<NotificationPayloadEntity> findByRecipientIdAndTypeOrderByTimestampDesc(Long recipientId, NotificationType type);

    // Count unread notifications
    long countByRecipientIdAndReadFalse(Long recipientId);

    // Mark notifications as read
    @Modifying
    @Query("UPDATE NotificationPayloadEntity n SET n.read = true WHERE n.recipientId = :recipientId AND n.read = false")
    void markAllAsRead(Long recipientId);

    @Modifying
    @Query("UPDATE NotificationPayloadEntity n SET n.read = true WHERE n.id = :notificationId")
    void markAsRead(Long notificationId);

    @Modifying
    @Query(value = "DELETE FROM notification_payload_entity n " +
            "WHERE n.timestamp < CURRENT_TIMESTAMP - INTERVAL '30' DAY",
            nativeQuery = true)
    void deleteOldNotifications();

}