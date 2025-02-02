package com.fullstackbootcamp.capstoneBackend.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
    private Long id;
    private String firstName;
    private String bank;
    private Boolean isYou;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String bank, Boolean isYou) {
        this.id = id;
        this.firstName = firstName;
        this.bank = bank;
        this.isYou = isYou;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBank() {
        return bank;
    }

    public Boolean getIsYou() {
        return isYou;
    }
}
