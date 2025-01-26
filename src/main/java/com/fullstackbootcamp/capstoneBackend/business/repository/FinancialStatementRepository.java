package com.fullstackbootcamp.capstoneBackend.business.repository;

import com.fullstackbootcamp.capstoneBackend.business.entity.FinancialStatementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialStatementRepository extends JpaRepository<FinancialStatementEntity, Long> {
}
