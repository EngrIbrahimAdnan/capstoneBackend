package com.fullstackbootcamp.capstoneBackend.loan.repository;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponseEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanResponseRepository extends JpaRepository<LoanResponseEntity, Long> {
    Optional<LoanResponseEntity> findByBanker(UserEntity user);
}
