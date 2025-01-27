package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequestWithFiles;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface BusinessService{
    getBusinessDTO getBusiness(Authentication authentication);

    AddBusinessDTO addBusiness(AddBusinessRequestWithFiles request, Authentication authentication);

}
