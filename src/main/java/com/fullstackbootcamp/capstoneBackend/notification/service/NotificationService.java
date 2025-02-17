package com.fullstackbootcamp.capstoneBackend.notification.service;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.notification.bo.NotificationRequest;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.notification.repository.NotificationRepository;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository,
                               JwtUtil jwtUtil) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<NotificationEntity> getAllNotificationsForUser(String authHeader) {
        String username = jwtUtil.extractUserUsernameFromToken(authHeader);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(user.get() );
    }

    public void sendNotification(NotificationEntity notificationRequest) {
        notificationRepository.save(notificationRequest);
    }

}