package com.fullstackbootcamp.capstoneBackend.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fullstackbootcamp.capstoneBackend.auth.dto.LoadUsersResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.dto.TokenResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.bo.LoginRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthService {
    SignupResponseDTO processSignupRequest(CreateUserRequest request);

    <T> LoadUsersResponseDTO loadEntites(
            String file,
            TypeReference<List<T>> typeReference
    );
    TokenResponseDTO refreshAccessToken(String refreshToken);
    TokenResponseDTO login(LoginRequest loginRequest);

}
