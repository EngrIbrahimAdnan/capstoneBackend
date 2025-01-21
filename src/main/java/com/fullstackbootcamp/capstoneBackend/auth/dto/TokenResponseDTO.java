package com.fullstackbootcamp.capstoneBackend.auth.dto;

import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenServiceStatus;

public class TokenResponseDTO {

    private TokenServiceStatus status;
    private String message;
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public TokenServiceStatus getStatus() {
        return status;
    }

    public void setStatus(TokenServiceStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

