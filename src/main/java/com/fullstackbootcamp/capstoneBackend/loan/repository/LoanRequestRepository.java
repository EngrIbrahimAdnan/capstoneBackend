package com.fullstackbootcamp.capstoneBackend.loan.repository;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequestEntity, Long> {
    Optional<LoanRequestEntity> findById(Long id);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks")
    List<LoanRequestEntity> findBySelectedBank(Bank bank);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks AND lr.status = 'PENDING'")
    List<LoanRequestEntity> findBySelectedBankAndStatusPending(Bank bank);

    @Query("""
       SELECT lr 
       FROM LoanRequestEntity lr
       WHERE :bank MEMBER OF lr.selectedBanks
         AND lr.status <> 'PENDING'
       """)
    List<LoanRequestEntity> findBySelectedBankAndStatusNotPending(Bank bank);


}
