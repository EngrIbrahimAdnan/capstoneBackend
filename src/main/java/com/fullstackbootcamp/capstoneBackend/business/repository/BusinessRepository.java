package com.fullstackbootcamp.capstoneBackend.business.repository;

import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
    BusinessEntity findByBusinessName(String businessName);
    BusinessEntity findByBusinessLicense(String businessLicense);
}
