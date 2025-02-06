package com.fullstackbootcamp.capstoneBackend.auth.service;

import com.fullstackbootcamp.capstoneBackend.auth.bo.CustomUserDetails;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by the given username and returns a CustomUserDetails instance.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        // Convert UserEntity to CustomUserDetails
        return new CustomUserDetails(user);
    }

    /**
     * Optional: If you want a direct method to convert a UserEntity to CustomUserDetails.
     */
    public CustomUserDetails toCustomUserDetails(UserEntity user) {
        return new CustomUserDetails(user);
    }
}
