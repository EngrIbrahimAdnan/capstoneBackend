package com.fullstackbootcamp.capstoneBackend.chat.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "chats")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "banker_id")
    private UserEntity banker;

    @ManyToOne
    @JoinColumn(name = "business_owner_id")
    private UserEntity businessOwner;

    @JsonManagedReference
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> messages;

    public Long getId() {
        return id;
    }

    public UserEntity getBanker() {
        return banker;
    }

    public void setBanker(UserEntity banker) {
        this.banker = banker;
    }

    public UserEntity getBusinessOwner() {
        return businessOwner;
    }

    public void setBusinessOwner(UserEntity businessOwner) {
        this.businessOwner = businessOwner;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public void setLastMessage(MessageEntity message) {
        this.messages.add(message);
    }
}