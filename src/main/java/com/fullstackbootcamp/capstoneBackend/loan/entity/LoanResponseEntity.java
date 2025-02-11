package com.fullstackbootcamp.capstoneBackend.loan.entity;

import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanResponseStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanTerm;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RepaymentPlan;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loan_response")
public class LoanResponseEntity {
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

    @Enumerated(EnumType.STRING)
    private Bank bank;

    /* Note: the following fields are all nullable in case status is approve:
     *  - amount, loan term, repayment plan
     */
    private BigDecimal amount;

    /* Note: standard loanTerms in banks:
     *  - SIX_MONTHS, ONE_YEAR, TWO_YEARS, FIVE_YEARS
     */
    @Column(name = "loan_term")
    private LoanTerm loanTerm; // expects

    @Column(name = "repayment_plan")
    private RepaymentPlan repaymentPlan; // expects

    @NotNull(message = "The 'status' field is required and it's missing")
    private LoanResponseStatus status;

    // Note: data of the last request status update
    @Column(name = "data_status", nullable = false)
    private LocalDateTime statusDate;

    // Note: keeping track on whether the request is viewed by each user
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_response_notifications")
    private List<NotificationEntity> loanResponseNotifications;

    private String rejectionReason;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Long getId() {
        return id;
    }

    public UserEntity getBanker() {
        return banker;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
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

    public LoanResponseStatus getStatus() {
        return status;
    }

    public void setStatus(LoanResponseStatus status) {
        this.status = status;
    }

    public LocalDateTime getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDateTime statusDate) {
        this.statusDate = statusDate;
    }

    public List<NotificationEntity> getLoanResponseNotifications() {
        return loanResponseNotifications;
    }

    public void setLoanResponseNotifications(List<NotificationEntity> loanResponseNotifications) {
        this.loanResponseNotifications = loanResponseNotifications;
    }
}
