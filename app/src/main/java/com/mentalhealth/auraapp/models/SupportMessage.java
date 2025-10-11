package com.mentalhealth.auraapp.models;

public class SupportMessage {
    private String messageId;
    private String postId;
    private String senderUserId;
    private String message;
    private long timestamp;

    public SupportMessage() {
        // Required for Firestore
    }

    public SupportMessage(String messageId, String postId, String senderUserId,
                          String message, long timestamp) {
        this.messageId = messageId;
        this.postId = postId;
        this.senderUserId = senderUserId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }

    public String getSenderUserId() { return senderUserId; }
    public void setSenderUserId(String senderUserId) { this.senderUserId = senderUserId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
