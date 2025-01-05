package com.fullstackbootcamp.capstoneBackend.auth.service;

import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.dto.SignupResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    SignupResponseDTO processSignupRequest(CreateUserRequest request);
}
