package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestRetrievalStatus;

public class GetLoanRequestDTO {
    private LoanRequestRetrievalStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "Successfully retrieved loan request entity" or error details)
    private LoanRequest entity;

    public LoanRequestRetrievalStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestRetrievalStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoanRequest getEntity() {
        return entity;
    }

    public void setEntity(LoanRequest entity) {
        this.entity = entity;
    }
}
