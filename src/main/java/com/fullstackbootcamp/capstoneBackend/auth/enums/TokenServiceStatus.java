package com.fullstackbootcamp.capstoneBackend.auth.enums;

public enum TokenServiceStatus {
    SUCCESS,
    FAILURE,
    TOKEN_GENERATED,
    TOKEN_EXPIRED,
    TOKEN_INVALID,
    TOKEN_REVOKED,
    TOKEN_REFRESHED,
    TOKEN_NOT_FOUND,
    UNAUTHORIZED,
    SERVER_ERROR,
    INVALID_REQUEST,
    MISSING_TOKEN
}
