package com.fullstackbootcamp.capstoneBackend.notification.service;

import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.notification.repository.NotificationsRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationsServiceImpl implements NotificationsService{
    private final NotificationsRepository notificationsRepository;

    public NotificationsServiceImpl(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    // TODO: update notification from false to true for view
    // TODO: search if notifications for a user exists, if not

    public NotificationEntity saveNotificationEntity(NotificationEntity notification){
        return notificationsRepository.save(notification);
    }


}
