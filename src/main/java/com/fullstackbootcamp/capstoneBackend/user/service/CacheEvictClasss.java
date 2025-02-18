package com.fullstackbootcamp.capstoneBackend.user.service;

import org.springframework.stereotype.Service;

@Service
public class CacheEvictClasss {
    @org.springframework.cache.annotation.CacheEvict(value = "dashboardData", allEntries = true)
    public void clearAllDashboardCache() {
        // Method can be empty
    }
    @org.springframework.cache.annotation.CacheEvict(value = "loanRequests", allEntries = true)
    public void clearAllLoanRequestsPageable() {
        // Method can be empty
    }
}
