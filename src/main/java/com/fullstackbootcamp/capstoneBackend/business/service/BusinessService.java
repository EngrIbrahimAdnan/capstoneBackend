package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface BusinessService{
    getBusinessDTO getBusiness(Authentication authentication);


    AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication);

    ResponseEntity<?> getBusiness2(Authentication authentication);
    AddBusinessDTO addBusiness2(AddBusinessRequest request, Authentication authentication);

}
