package com.fullstackbootcamp.capstoneBackend.user.dto;

import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatPreviewDTO;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponse {
    // TODO: Make this its own object perhaps
    Map<String, Object> notifications;
    // pendingReview format:
    // { "pending": 0, "dinarsInReview": 0 }
    Map<String, BigDecimal> pendingReview;
    Map<String, Object> fiveMostRecentRequests;
    List<ChatPreviewDTO>  fourMostRecentChats;
    List<Map<String, Object>> recentHistory;

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
