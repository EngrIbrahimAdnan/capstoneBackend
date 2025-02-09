package com.fullstackbootcamp.capstoneBackend.user.dto;

import java.util.Map;

public class DashboardResponse {
    // TODO: Make this its own object perhaps
    Map<String, Object> notifications;
    // pendingReview format:
    // { "pending": 0, "dinarsInReview": 0 }
    Map<String, Object> pendingReview;
    Map<String, Object> fiveMostRecentRequests;
    Map<String, Object> fourMostRecentChats;
    Map<String, Object> recentHistory;
}
