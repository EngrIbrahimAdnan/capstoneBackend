package com.fullstackbootcamp.capstoneBackend.loanNotification.service;

import com.fullstackbootcamp.capstoneBackend.loanNotification.entity.LoanNotificationEntity;
import com.fullstackbootcamp.capstoneBackend.loanNotification.repository.LoanNotificationsRepository;
import org.springframework.stereotype.Service;

@Service
public class LoanNotificationsServiceImpl implements LoanNotificationsService {
    private final LoanNotificationsRepository loanNotificationsRepository;

    public LoanNotificationsServiceImpl(LoanNotificationsRepository loanNotificationsRepository) {
        this.loanNotificationsRepository = loanNotificationsRepository;
    }

    // TODO: update notification from false to true for view
    // TODO: search if notifications for a user exists, if not

    public LoanNotificationEntity saveNotificationEntity(LoanNotificationEntity notification){
        return loanNotificationsRepository.save(notification);
    }


}
