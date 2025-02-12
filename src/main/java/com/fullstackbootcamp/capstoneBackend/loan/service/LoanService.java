package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface LoanService {
    LoanRequestDTO createLoanRequest(CreateLoanRequest request, Authentication authentication);
    LoanResponseDTO createLoanResponse(CreateLoanResponse request, Authentication authentication);
    GetLoanRequestDTO getLoanRequestById(Long id,Authentication authentication);
    CheckNotificationDTO viewRequest(Long id, Authentication authentication);
    Map<String, Object> getLoanRequestsPageable(
            int page,
            String status,
            String search,
            int limit,
            Authentication authentication);

    List<GetLoanRequestsOfBusinessDTO> getLoanRequestsOfBusiness(Long businessId, Authentication authentication);
    GetAllLoanRequestsDTO getAllLoanRequestsForBusinessOwner(Authentication authentication);

    OfferResponseDTO acceptOffer(Long loanRequestId, Long loanResponseId,Authentication authentication);
    OfferResponseDTO withdrawOffer(Long loanRequestId,Authentication authentication);
    OfferResponseDTO rejectOffer(Long loanRequestId, Long loanResponseId,Authentication authentication);
}

