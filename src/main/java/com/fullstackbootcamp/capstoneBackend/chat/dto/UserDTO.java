package com.fullstackbootcamp.capstoneBackend.chat.dto;

public class UserDTO {
    private Long id;
    private String firstName;
    private String bank;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String bank) {
        this.id = id;
        this.firstName = firstName;
        this.bank = bank;
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
}
