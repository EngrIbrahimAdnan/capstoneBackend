package com.fullstackbootcamp.capstoneBackend.notification.repository;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequest;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface NotificationsRepository extends JpaRepository<NotificationEntity, Long> {

}
