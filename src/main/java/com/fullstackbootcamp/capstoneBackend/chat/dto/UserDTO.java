package com.fullstackbootcamp.capstoneBackend.chat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDTO {
    private Long id;
    private String firstName;
    private String username;
    private String bank;
    private String business;
    private String profilePicture;
    private Boolean isYou;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String bank, Boolean isYou, String business, String profilePicture, String username) {
        this.id = id;
        this.firstName = firstName;
        this.bank = bank;
        this.isYou = isYou;
        this.business = business;
        this.profilePicture = profilePicture;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Boolean getYou() {
        return isYou;
    }

    public void setYou(Boolean you) {
        isYou = you;
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
