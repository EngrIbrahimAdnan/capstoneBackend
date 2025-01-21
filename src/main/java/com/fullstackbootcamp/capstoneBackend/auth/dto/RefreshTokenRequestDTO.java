package com.fullstackbootcamp.capstoneBackend.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequestDTO {

    @NotNull(message = "The 'refreshToken' field is required and it's missing")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}