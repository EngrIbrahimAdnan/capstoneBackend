package com.fullstackbootcamp.capstoneBackend.chat.repository;

import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByBankerId(Long bankerId);
    List<ChatEntity> findByBusinessOwnerId(Long businessOwnerId);

    @Query("SELECT c FROM ChatEntity c WHERE c.banker.id = :bankerId AND c.businessOwner.id = :businessOwnerId")
    ChatEntity findByBankerAndBusinessOwner(Long bankerId, Long businessOwnerId);
}