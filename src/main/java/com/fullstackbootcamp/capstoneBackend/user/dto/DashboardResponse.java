package com.fullstackbootcamp.capstoneBackend.user.dto;

import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatPreviewDTO;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class DashboardResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Object> notifications = new HashMap<>();
    private Map<String, BigDecimal> pendingReview = new HashMap<>();
    private Map<String, Object> fiveMostRecentRequests = new HashMap<>();
    private List<ChatPreviewDTO> fourMostRecentChats = new ArrayList<>();
    private List<Map<String, Object>> recentHistory = new ArrayList<>();

    public Map<String, Object> getNotifications() {
        return notifications;
    }

    public void setNotifications(Map<String, Object> notifications) {
        this.notifications = notifications;
    }

    public Map<String, BigDecimal> getPendingReview() {
        return pendingReview;
    }

    public void setPendingReview(Map<String, BigDecimal> pendingReview) {
        this.pendingReview = pendingReview;
    }

    public Map<String, Object> getFiveMostRecentRequests() {
        return fiveMostRecentRequests;
    }

    public void setFiveMostRecentRequests(Map<String, Object> fiveMostRecentRequests) {
        this.fiveMostRecentRequests = fiveMostRecentRequests;
    }

    public List<ChatPreviewDTO> getFourMostRecentChats() {
        return fourMostRecentChats;
    }

    public void setFourMostRecentChats(List<ChatPreviewDTO> fourMostRecentChats) {
        this.fourMostRecentChats = fourMostRecentChats;
    }

    public List<Map<String, Object>> getRecentHistory() {
        return recentHistory;
    }

    public void setRecentHistory(List<Map<String, Object>> recentHistory) {
        this.recentHistory = recentHistory;
    }
}
