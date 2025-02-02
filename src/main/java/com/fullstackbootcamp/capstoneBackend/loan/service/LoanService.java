package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface LoanService {
    LoanRequestDTO createLoanRequest(CreateLoanRequest request, Authentication authentication);
}
