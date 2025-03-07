package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanResponseStatus;

public class GetLoanRequestDTO {
    private LoanRequestRetrievalStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "Successfully retrieved loan request entity" or error details)
    private LoanRequestEntity entity;
    private LoanResponseStatus responseStatus;
    private String rejectionReason;
    private Long chatId;

    public GetLoanRequestDTO() {}

    public GetLoanRequestDTO(LoanRequestRetrievalStatus status, String message, LoanRequestEntity entity, Long chatId) {
        this.status = status;
        this.message = message;
        this.entity = entity;
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LoanResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(LoanResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

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

    public LoanRequestEntity getEntity() {
        return entity;
    }

    public void setEntity(LoanRequestEntity entity) {
        this.entity = entity;
    }
}
