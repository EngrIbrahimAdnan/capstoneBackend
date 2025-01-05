package com.fullstackbootcamp.capstoneBackend.user.repository;

import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByCivilId(String civilId);

}
