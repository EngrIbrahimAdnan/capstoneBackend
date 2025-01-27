package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.business.bo.CreateBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.BusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {
    private BusinessRepository businessRepository;
    private BusinessMapperService businessMapperService;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public BusinessService(BusinessRepository businessRepository,
                           BusinessMapperService businessMapperService,
                           UserRepository userRepository,
                           JwtUtil jwtUtil) {
        this.businessRepository = businessRepository;
        this.businessMapperService = businessMapperService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public BusinessDTO createBusiness(String authHeader, CreateBusinessRequest request) {
        String businessOwnerName = jwtUtil.extractUserUsernameFromToken(authHeader);
        UserEntity businessOwner = userRepository.findByUsername(businessOwnerName).orElseThrow(() -> new RuntimeException("Business owner not found."));

        BusinessEntity business = new BusinessEntity(businessOwner, request.getBusinessName(), request.getBusinessLicense());
        BusinessEntity savedBusiness = businessRepository.save(business);

        // TODO: Add financial statement and calculate financial score and business state

        return businessMapperService.businessToBusinessDTO(savedBusiness);
    }

    public List<BusinessDTO> getBusiness(String authHeader) {
        String businessOwnerName = jwtUtil.extractUserUsernameFromToken(authHeader);
        UserEntity businessOwner = userRepository.findByUsername(businessOwnerName).orElseThrow(() -> new RuntimeException("Business owner not found."));

        List<BusinessEntity> businesses = businessRepository.findByBusinessOwnerUser(businessOwner);

        return businessMapperService.businessEntitiesToBusinessDTOs(businesses);
    }
}
