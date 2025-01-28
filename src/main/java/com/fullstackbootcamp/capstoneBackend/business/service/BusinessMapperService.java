package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.business.dto.BusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessMapperService {

    public BusinessDTO businessToBusinessDTO(BusinessEntity businessEntity) {
        return new BusinessDTO(
            businessEntity.getBusinessOwnerUser().getUsername(),
            businessEntity.getBusinessName(),
            businessEntity.getBusinessLicense());
    }

    public List<BusinessDTO> businessEntitiesToBusinessDTOs(List<BusinessEntity> businessEntities) {
        return businessEntities.stream().map(this::businessToBusinessDTO).toList();
    }
}
