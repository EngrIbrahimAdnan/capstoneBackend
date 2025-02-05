package com.fullstackbootcamp.capstoneBackend.notification.service;

import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import org.springframework.stereotype.Service;

@Service
public interface NotificationsService {
    NotificationEntity saveNotificationEntity(NotificationEntity notification);
}
