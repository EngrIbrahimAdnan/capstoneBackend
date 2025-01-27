package com.fullstackbootcamp.capstoneBackend.business.repository;

import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
    BusinessEntity findByBusinessName(String businessName);
    BusinessEntity findByBusinessLicense(String businessLicense);
    List<BusinessEntity> findByBusinessOwnerUser(UserEntity user);
}
