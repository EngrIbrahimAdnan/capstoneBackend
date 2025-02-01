package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.business.dto.BusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessMapperService {

    public BusinessDTO businessToBusinessDTO(BusinessEntity businessEntity) {
        return new BusinessDTO(
            businessEntity.getBusinessOwnerUser().getUsername()
//            businessEntity.getBusinessName(), TODO: due to entity restructure, business name would be extracted from financial statement entity
//            businessEntity.getBusinessLicense() TODO: similarly, this would be extracted from business license entity (once its made)
            );
    }

    public List<BusinessDTO> businessEntitiesToBusinessDTOs(List<BusinessEntity> businessEntities) {
        return businessEntities.stream().map(this::businessToBusinessDTO).toList();
    }
}
