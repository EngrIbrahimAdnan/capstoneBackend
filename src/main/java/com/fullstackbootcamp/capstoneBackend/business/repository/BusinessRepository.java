package com.fullstackbootcamp.capstoneBackend.business.repository;

import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntityWithImages;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<BusinessEntity, Long> {
    Optional<BusinessEntity> findByBusinessOwnerUser(UserEntity user);

}
