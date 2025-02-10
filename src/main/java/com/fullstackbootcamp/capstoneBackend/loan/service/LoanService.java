package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.dto.*;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

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
    GetAllLoanRequestsDTO getAllLoanRequestsForBusinessOwner(Authentication authentication);
}

