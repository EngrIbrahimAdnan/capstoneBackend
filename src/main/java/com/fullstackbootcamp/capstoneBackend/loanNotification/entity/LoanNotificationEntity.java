package com.fullstackbootcamp.capstoneBackend.loanNotification.entity;

import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "loan_notifications")
public class LoanNotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_notifications_user")
    private UserEntity userEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity user) {
        userEntity = user;
    }
}
