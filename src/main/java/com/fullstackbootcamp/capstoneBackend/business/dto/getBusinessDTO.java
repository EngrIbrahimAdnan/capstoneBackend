package com.fullstackbootcamp.capstoneBackend.business.dto;

import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;

public class getBusinessDTO {
    private BusinessRetrievalStatus status; // SUCCESS, FAIL, BUSINESS_ALREADY_EXISTS
    private String message;         // Description of the result (e.g., "User created successfully" or error details)
    private BusinessEntity entity;

    public BusinessRetrievalStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessRetrievalStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessEntity getEntity() {
        return entity;
    }

    public void setEntity(BusinessEntity entity) {
        this.entity = entity;
    }
}
