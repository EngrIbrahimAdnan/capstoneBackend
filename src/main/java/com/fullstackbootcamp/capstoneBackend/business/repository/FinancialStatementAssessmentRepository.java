package com.fullstackbootcamp.capstoneBackend.business.repository;

import com.fullstackbootcamp.capstoneBackend.business.entity.FinancialStatementAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialStatementAssessmentRepository extends JpaRepository<FinancialStatementAssessmentEntity, Long> {

}
