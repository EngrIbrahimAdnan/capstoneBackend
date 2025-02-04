package com.fullstackbootcamp.capstoneBackend.loan.repository;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanResponseRepository extends JpaRepository<LoanResponse, Long> {
    Optional<LoanResponse> findById(Long id);
}
