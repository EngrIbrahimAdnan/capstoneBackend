package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface BusinessService{
    getBusinessDTO getBusiness(Authentication authentication);
    AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication) throws JsonProcessingException;
}
