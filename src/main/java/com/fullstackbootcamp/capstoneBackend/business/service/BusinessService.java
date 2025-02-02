package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BusinessService{
    getBusinessDTO getBusiness(Authentication authentication);
    AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication);
    Optional<BusinessEntity> getBusinessOwnerEntity(UserEntity user);
}
