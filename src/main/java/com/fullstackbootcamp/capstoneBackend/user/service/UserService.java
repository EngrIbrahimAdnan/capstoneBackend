package com.fullstackbootcamp.capstoneBackend.user.service;

import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.dto.DashboardResponse;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public interface UserService {
    SignupResponseDTO createUser (CreateUserRequest request);
    Optional<UserEntity> getUserByCivilId(String civilId);
    Optional<UserEntity> getUserByUsername(String username);
    DashboardResponse getDashboardData(String token);
    Map<String, Object> getPendingReview(UserEntity user);
    Map<String, Object> getNotifications(UserEntity user);
    Map<String, Object> getFiveMostRecentRequests(UserEntity user);
    Map<String, Object> getFourMostRecentChats(UserEntity user);
    Map<String, Object> getRecentHistory(UserEntity user);
}
