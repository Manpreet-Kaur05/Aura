package com.mentalhealth.auraapp.models;

public class VentPost {
    private String postId;
    private String userId;
    private String anonymousName;
    private String message;
    private long timestamp;
    private int supportCount;
    private boolean isAnonymous;

    public VentPost() {
        // Required for Firestore
    }

    public VentPost(String postId, String userId, String anonymousName,
                    String message, long timestamp) {
        this.postId = postId;
        this.userId = userId;
        this.anonymousName = anonymousName;
        this.message = message;
        this.timestamp = timestamp;
        this.supportCount = 0;
        this.isAnonymous = true;
    }

    // Getters and setters
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAnonymousName() { return anonymousName; }
    public void setAnonymousName(String anonymousName) { this.anonymousName = anonymousName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getSupportCount() { return supportCount; }
    public void setSupportCount(int supportCount) { this.supportCount = supportCount; }

    public boolean isAnonymous() { return isAnonymous; }
    public void setAnonymous(boolean anonymous) { isAnonymous = anonymous; }
}
