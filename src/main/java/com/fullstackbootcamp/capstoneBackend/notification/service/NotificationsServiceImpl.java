package com.fullstackbootcamp.capstoneBackend.notification.service;

import com.fullstackbootcamp.capstoneBackend.notification.repository.NotificationsRepository;

public class NotificationsServiceImpl implements NotificationsService{
    private final NotificationsRepository notificationsRepository;

    public NotificationsServiceImpl(NotificationsRepository notificationsRepository) {
        this.notificationsRepository = notificationsRepository;
    }

    // TODO: update notification from false to true for view
    // TODO: search if notifications for a user exists, if not



}
