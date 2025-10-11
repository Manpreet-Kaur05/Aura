package com.mentalhealth.auraapp.models;

public class User {
    private String userId;
    private String email;
    private String displayName;
    private long createdAt;

    // Empty constructor required for Firebase
    public User() {}

    public User(String userId, String email, String displayName, long createdAt) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
