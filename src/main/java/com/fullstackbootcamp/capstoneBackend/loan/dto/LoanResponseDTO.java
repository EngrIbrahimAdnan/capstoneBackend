package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.loan.enums.CreateLoanResponseStatus;

public class LoanResponseDTO {
    private CreateLoanResponseStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "User created successfully" or error details)

    public CreateLoanResponseStatus getStatus() {
        return status;
    }

    public void setStatus(CreateLoanResponseStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
