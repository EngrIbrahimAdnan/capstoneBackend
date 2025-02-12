package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanResponseStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanTerm;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import lombok.AllArgsConstructor;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetLoanRequestsOfBusinessDTO {
    private Long id;
    private LoanResponseStatus loanResponseStatus;
    private String businessName;
    private String businessOwner;
    private BigDecimal amount;
    private LoanTerm paymentPeriod;
    private LocalDateTime date;

    public GetLoanRequestsOfBusinessDTO(LoanRequestEntity request, Bank bank) {
        this.id = request.getId();

        this.businessName = request.getBusiness().getBusinessNickname();
        this.businessOwner = request.getBusiness().getBusinessOwnerUser().getFirstName() +
                " " +
                request.getBusiness().getBusinessOwnerUser().getLastName();
        this.amount = request.getAmount();
        this.paymentPeriod = request.getLoanTerm();
        this.date = request.getStatusDate();
    }
}