package com.fullstackbootcamp.capstoneBackend.loanNotification.repository;

import com.fullstackbootcamp.capstoneBackend.loanNotification.entity.LoanNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface LoanNotificationsRepository extends JpaRepository<LoanNotificationEntity, Long> {

}
