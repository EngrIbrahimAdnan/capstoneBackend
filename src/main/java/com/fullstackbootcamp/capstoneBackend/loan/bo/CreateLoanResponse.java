package com.fullstackbootcamp.capstoneBackend.loan.bo;

import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateLoanResponse {

    @NotNull(message = "The 'loanRequestId' field is required and it's missing")
    private Long loanRequestId;

    private LoanRequestStatus responseStatus; // APPROVED, REJECTED, COUNTER_OFFER, RESCINDED
    private String reason;


    @NotNull(message = "The 'amount' field is required and it's missing")
    private BigDecimal amount;

    @NotNull(message = "The 'loanTerm' field is required and it's missing")
    private String loanTerm;

    @NotNull(message = "The 'repaymentPlan' field is required and it's missing")
    private String repaymentPlan; // expects



    public Long getLoanRequestId() {
        return loanRequestId;
    }

    public void setLoanRequestId(Long loanRequestId) {
        this.loanRequestId = loanRequestId;
    }

    public LoanRequestStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(LoanRequestStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(String repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }
}
