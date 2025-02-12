package com.fullstackbootcamp.capstoneBackend.loan.repository;

import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanResponseStatus;
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
    Optional<List<LoanRequestEntity>> findByBusinessAndStatusNot(BusinessEntity business, LoanRequestStatus status);

    @Query("""
    SELECT lr FROM LoanRequestEntity lr 
    WHERE :bank MEMBER OF lr.selectedBanks 
    AND lr.business.businessOwnerUser.id = :ownerId
    AND NOT EXISTS (
        SELECT resp FROM lr.loanResponseEntities resp 
        WHERE resp.bank = :bank
    )
""")
    Page<LoanRequestEntity> findPendingRequestsByBankAndBusinessOwner(
            @Param("bank") Bank bank,
            @Param("ownerId") Long businessOwnerId,
            Pageable pageable
    );

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

    /**
     * Finds PENDING loan requests for a specific bank:
     * - Requests where the bank is in selectedBanks
     * - Do not have any loan response from that bank
     */
    @Query("""
            SELECT lr FROM LoanRequestEntity lr 
            WHERE :bank MEMBER OF lr.selectedBanks 
            AND NOT EXISTS (
                SELECT resp FROM lr.loanResponseEntities resp 
                WHERE resp.bank = :bank
            )
            """)
    Page<LoanRequestEntity> findPendingRequestsByBank(
            @Param("bank") Bank bank,
            Pageable pageable
    );

    /**
     * Finds APPROVED loan requests for a specific bank:
     * - Requests where the bank is in selectedBanks
     * - Have a loan response with APPROVED status from that bank
     */
    @Query("""
            SELECT lr FROM LoanRequestEntity lr 
            WHERE :bank MEMBER OF lr.selectedBanks 
            AND EXISTS (
                SELECT resp FROM lr.loanResponseEntities resp 
                WHERE resp.bank = :bank 
                AND resp.status = :approvedStatus
            )
            """)
    Page<LoanRequestEntity> findApprovedRequestsByBank(
            @Param("bank") Bank bank,
            @Param("approvedStatus") LoanResponseStatus approvedStatus,
            Pageable pageable
    );

    /**
     * Finds REJECTED loan requests for a specific bank:
     * - Requests where the bank is in selectedBanks
     * - Have a loan response with REJECTED status from that bank
     */
    @Query("""
            SELECT lr FROM LoanRequestEntity lr 
            WHERE :bank MEMBER OF lr.selectedBanks 
            AND EXISTS (
                SELECT resp FROM lr.loanResponseEntities resp 
                WHERE resp.bank = :bank 
                AND resp.status = :rejectedStatus
            )
            """)
    Page<LoanRequestEntity> findRejectedRequestsByBank(
            @Param("bank") Bank bank,
            @Param("rejectedStatus") LoanResponseStatus rejectedStatus,
            Pageable pageable
    );

    /**
     * Get all requests for a bank with a specific status
     * This is a more generic version that can be used for any status
     */
    @Query("""
            SELECT lr FROM LoanRequestEntity lr 
            WHERE :bank MEMBER OF lr.selectedBanks 
            AND EXISTS (
                SELECT resp FROM lr.loanResponseEntities resp 
                WHERE resp.bank = :bank 
                AND resp.status = :status
            )
            """)
    Page<LoanRequestEntity> findRequestsByBankAndStatus(
            @Param("bank") Bank bank,
            @Param("status") LoanResponseStatus status,
            Pageable pageable
    );

    /**
     * Find all requests for a bank regardless of status
     */
    @Query("SELECT lr FROM LoanRequestEntity lr WHERE :bank MEMBER OF lr.selectedBanks")
    Page<LoanRequestEntity> findRequestsByBank(
            @Param("bank") Bank bank,
            Pageable pageable
    );

    Optional<List<LoanRequestEntity>> findByBusiness(BusinessEntity business);
}
