package com.fullstackbootcamp.capstoneBackend.loanNotification.service;

import com.fullstackbootcamp.capstoneBackend.loanNotification.entity.LoanNotificationEntity;
import org.springframework.stereotype.Service;

@Service
public interface LoanNotificationsService {
    LoanNotificationEntity saveNotificationEntity(LoanNotificationEntity notification);
}
