package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.GetBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BusinessService{
    GetBusinessDTO getBusiness(Authentication authentication);
    AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication) throws JsonProcessingException;
    Optional<BusinessEntity> getBusinessOwnerEntity(UserEntity user);
}
