package com.fullstackbootcamp.capstoneBackend.notification.entity;

import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "notifications_entity")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "notifications_user", nullable = false)
//    private UserEntity userEntity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public UserEntity getUser() {
//        return userEntity;
//    }
//
//    public void setUser(UserEntity user) {
//        userEntity = user;
//    }
}
