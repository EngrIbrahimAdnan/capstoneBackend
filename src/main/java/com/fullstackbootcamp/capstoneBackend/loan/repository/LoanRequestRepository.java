package com.fullstackbootcamp.capstoneBackend.loan.repository;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRequestRepository extends JpaRepository<LoanRequestEntity, Long> {
    Optional<LoanRequestEntity> findById(Long id);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks")
    List<LoanRequestEntity> findBySelectedBank(Bank bank);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks")
    Page<LoanRequestEntity> findBySelectedBankPageable(@Param("bank") Bank bank, Pageable pageable);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks AND lr.status = :status")
    Page<LoanRequestEntity> findBySelectedBankAndStatusPageable(@Param("bank") Bank bank,
                                                                @Param("status") LoanRequestStatus status,
                                                                Pageable pageable);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks " +
            "AND (LOWER(lr.business.businessNickname) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(lr.business.businessOwnerUser.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(lr.business.businessOwnerUser.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<LoanRequestEntity> findBySelectedBankAndSearchPageable(@Param("bank") Bank bank,
                                                                @Param("search") String search,
                                                                Pageable pageable);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks " +
            "AND lr.status = :status " +
            "AND (LOWER(lr.business.businessNickname) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(lr.business.businessOwnerUser.firstName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "     OR LOWER(lr.business.businessOwnerUser.lastName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<LoanRequestEntity> findBySelectedBankAndStatusAndSearchPageable(@Param("bank") Bank bank,
                                                                         @Param("status") LoanRequestStatus status,
                                                                         @Param("search") String search,
                                                                         Pageable pageable);

    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks AND lr.status = 'PENDING'")
    List<LoanRequestEntity> findBySelectedBankAndStatusPending(Bank bank);

    @Query("""
       SELECT lr 
       FROM LoanRequestEntity lr
       WHERE :bank MEMBER OF lr.selectedBanks
         AND lr.status <> 'PENDING'
       """)
    List<LoanRequestEntity> findBySelectedBankAndStatusNotPending(Bank bank);

    @Query("""
       SELECT lr 
       FROM LoanRequestEntity lr
       WHERE :bank MEMBER OF lr.selectedBanks
         AND lr.status <> 'PENDING'
       """)
    List<LoanRequestEntity> findBySelectedBankAndStatusNotPendingPageable(Bank bank, Pageable pageable);


}
