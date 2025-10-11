package com.mentalhealth.auraapp.models;

public class MoodEntry {
    private String entryId;
    private String userId;
    private long timestamp;
    private String quadrant;
    private String specificEmotion;
    private String journalText;
    private int sentimentScore; // -100 to +100
    private int emotionIntensity; // 1-10 scale
    private long selectionDuration; // Time taken to select emotion (milliseconds)
    private boolean isCrisisDetected;
    private String triggerContext; // What triggered this emotion

    // Journal analysis fields
    private int wordCount;
    private int selfReferenceCount;
    private int negativeKeywordCount;
    private int positiveKeywordCount;
    private boolean containsCrisisKeywords;

    // Empty constructor required for Firebase
    public MoodEntry() {}

    public MoodEntry(String entryId, String userId, long timestamp,
                     String quadrant, String specificEmotion, String journalText) {
        this.entryId = entryId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.quadrant = quadrant;
        this.specificEmotion = specificEmotion;
        this.journalText = journalText;
        this.emotionIntensity = 5; // Default medium intensity
        this.isCrisisDetected = false;
        this.containsCrisisKeywords = false;
    }

    // Full constructor with all fields
    public MoodEntry(String entryId, String userId, long timestamp,
                     String quadrant, String specificEmotion, String journalText,
                     int emotionIntensity, long selectionDuration, String triggerContext) {
        this.entryId = entryId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.quadrant = quadrant;
        this.specificEmotion = specificEmotion;
        this.journalText = journalText;
        this.emotionIntensity = emotionIntensity;
        this.selectionDuration = selectionDuration;
        this.triggerContext = triggerContext;
        this.isCrisisDetected = false;
        this.containsCrisisKeywords = false;
    }

    // Getters and Setters
    public String getEntryId() { return entryId; }
    public void setEntryId(String entryId) { this.entryId = entryId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getQuadrant() { return quadrant; }
    public void setQuadrant(String quadrant) { this.quadrant = quadrant; }

    public String getSpecificEmotion() { return specificEmotion; }
    public void setSpecificEmotion(String specificEmotion) {
        this.specificEmotion = specificEmotion;
    }

    public String getJournalText() { return journalText; }
    public void setJournalText(String journalText) { this.journalText = journalText; }

    public int getSentimentScore() { return sentimentScore; }
    public void setSentimentScore(int sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public int getEmotionIntensity() { return emotionIntensity; }
    public void setEmotionIntensity(int emotionIntensity) {
        this.emotionIntensity = emotionIntensity;
    }

    public long getSelectionDuration() { return selectionDuration; }
    public void setSelectionDuration(long selectionDuration) {
        this.selectionDuration = selectionDuration;
    }

    public boolean isCrisisDetected() { return isCrisisDetected; }
    public void setCrisisDetected(boolean crisisDetected) {
        this.isCrisisDetected = crisisDetected;
    }

    public String getTriggerContext() { return triggerContext; }
    public void setTriggerContext(String triggerContext) {
        this.triggerContext = triggerContext;
    }

    // Journal analysis getters and setters
    public int getWordCount() { return wordCount; }
    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getSelfReferenceCount() { return selfReferenceCount; }
    public void setSelfReferenceCount(int selfReferenceCount) {
        this.selfReferenceCount = selfReferenceCount;
    }

    public int getNegativeKeywordCount() { return negativeKeywordCount; }
    public void setNegativeKeywordCount(int negativeKeywordCount) {
        this.negativeKeywordCount = negativeKeywordCount;
    }

    public int getPositiveKeywordCount() { return positiveKeywordCount; }
    public void setPositiveKeywordCount(int positiveKeywordCount) {
        this.positiveKeywordCount = positiveKeywordCount;
    }

    public boolean isContainsCrisisKeywords() { return containsCrisisKeywords; }
    public void setContainsCrisisKeywords(boolean containsCrisisKeywords) {
        this.containsCrisisKeywords = containsCrisisKeywords;
    }
}
