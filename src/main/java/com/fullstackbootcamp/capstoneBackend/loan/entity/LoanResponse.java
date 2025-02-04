package com.fullstackbootcamp.capstoneBackend.loan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_response")
public class LoanResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Note:
     *  - banker is intentionally left nullable
     *  - This is to account for the case before it is assigned to a banker user
     *  - Once the banker assigns it to himself, UserEntity is assigned to this loanRequest
     */

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "banker_user_id", nullable = false)
    private UserEntity banker;


    @NotNull(message = "The 'amount' field is required and it's missing")
    private BigDecimal amount;

    /* Note: standard loanTerms in banks:
     *  - SIX_MONTHS, ONE_YEAR, TWO_YEARS, FIVE_YEARS
     */
    @Column(name = "loan_term", nullable = false)
    private String loanTerm; // expects

    @Column(name = "repayment_plan", nullable = false)
    private String repaymentPlan; // expects

    @NotNull(message = "The 'status' field is required and it's missing")
    private LoanRequestStatus status;

    // Note: data of the last request status update
    @Column(name = "data_status", nullable = false)
    private LocalDate statusDate;

    // Note: keeping track on whether the request is viewed by business owner
    // Note: changes to False each status update to alert user
    @NotNull(message = "The 'viewed' field is required and it's missing")
    private Boolean viewed;

    public Long getId() {
        return id;
    }

    public UserEntity getBanker() {
        return banker;
    }

    public void setBanker(UserEntity banker) {
        this.banker = banker;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LoanRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestStatus status) {
        this.status = status;
    }

    public LocalDate getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDate statusDate) {
        this.statusDate = statusDate;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }
}
