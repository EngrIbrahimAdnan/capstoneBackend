package com.fullstackbootcamp.capstoneBackend.loan.bo;

import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanTerm;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RepaymentPlan;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public class CreateLoanRequest {
    @NotNull(message = "The 'selectedBanks' field is required and it's missing")
    private List<Bank> selectedBanks;

    @NotNull(message = "The 'loanTitle' field is required and it's missing")
    private String loanTitle;

    @NotNull(message = "The 'loanPurpose' field is required and it's missing")
    private String loanPurpose;

    @NotNull(message = "The 'amount' field is required and it's missing")
    private BigDecimal amount;

    @NotNull(message = "The 'loanTerm' field is required and it's missing")
    private LoanTerm loanTerm;

    @NotNull(message = "The 'repaymentPlan' field is required and it's missing")
    private RepaymentPlan repaymentPlan;


    public List<Bank> getSelectedBanks() {
        return selectedBanks;
    }

    public void setSelectedBanks(List<Bank> selectedBanks) {
        this.selectedBanks = selectedBanks;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
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
