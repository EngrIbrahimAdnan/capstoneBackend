package com.fullstackbootcamp.capstoneBackend.user.service;

import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    SignupResponseDTO createUser (CreateUserRequest request);
    Optional<UserEntity> getUserByCivilId(String civilId);
    Optional<UserEntity> getUserByUsername(String username);
}
