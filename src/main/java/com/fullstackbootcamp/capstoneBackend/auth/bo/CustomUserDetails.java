package com.fullstackbootcamp.capstoneBackend.auth.bo;

import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    /**
     * Return the authorities (roles) granted to the user.
     * In this example, we simply wrap the user's role into a SimpleGrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Make sure user.getRole() is not null. Otherwise, handle accordingly.
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Return the password used to authenticate the user.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Return the username used to authenticate the user.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Indicates whether the user's account has expired.
     * Return true if the account is still valid (i.e., non-expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        // In a real-world scenario, you might store and check an expiration date.
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * Return true if the user is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        // You could have a field in UserEntity to track locked status.
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired.
     * Return true if the credentials are still valid.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // Similar to account expiration, you can store and check a date if needed.
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * Return true if the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        // This could be tied to a field (e.g., user.isEnabled()) in your UserEntity.
        return true;
    }

    /**
     * You can expose additional getters to retrieve custom fields from the user.
     * For example, firstName, lastName, civilId, mobileNumber, bank, etc.
     * That can be useful if you need them in your controllers or templates.
     */
    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getCivilId() {
        return user.getCivilId();
    }

    public String getMobileNumber() {
        return user.getMobileNumber();
    }

    public Bank getBank() {
        return user.getBank();
    }

    public Roles getRole() {
        return user.getRole();
    }

    public Long getId() {
        return user.getId();
    }
}