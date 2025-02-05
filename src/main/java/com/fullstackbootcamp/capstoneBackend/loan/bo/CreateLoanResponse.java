package com.fullstackbootcamp.capstoneBackend.loan.bo;

import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanResponseStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanTerm;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RepaymentPlan;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateLoanResponse {

    @NotNull(message = "The 'loanRequestId' field is required and it's missing")
    private Long loanRequestId;

    @NotNull(message = "The 'responseStatus' field is required and it's missing")
    private LoanResponseStatus responseStatus; // APPROVED, REJECTED, COUNTER_OFFER, RESCINDED

    // the following are nullable in the case of approve for response Status
    private String reason;
    private BigDecimal amount;
    private LoanTerm loanTerm;
    private RepaymentPlan repaymentPlan; // expects

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getLoanRequestId() {
        return loanRequestId;
    }

    public void setLoanRequestId(Long loanRequestId) {
        this.loanRequestId = loanRequestId;
    }

    public LoanResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(LoanResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LoanTerm getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(LoanTerm loanTerm) {
        this.loanTerm = loanTerm;
    }

    public RepaymentPlan getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(RepaymentPlan repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }
}
