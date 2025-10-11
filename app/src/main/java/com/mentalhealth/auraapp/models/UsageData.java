package com.mentalhealth.auraapp.models;

public class UsageData {
    private String userId;
    private String packageName;
    private String appName;
    private long totalTimeInForeground; // milliseconds
    private int launchCount;
    private long timestamp;

    // Empty constructor required for Firebase
    public UsageData() {}

    public UsageData(String userId, String packageName, String appName,
                     long totalTimeInForeground, int launchCount, long timestamp) {
        this.userId = userId;
        this.packageName = packageName;
        this.appName = appName;
        this.totalTimeInForeground = totalTimeInForeground;
        this.launchCount = launchCount;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }

    public long getTotalTimeInForeground() { return totalTimeInForeground; }
    public void setTotalTimeInForeground(long totalTimeInForeground) {
        this.totalTimeInForeground = totalTimeInForeground;
    }

    public int getLaunchCount() { return launchCount; }
    public void setLaunchCount(int launchCount) { this.launchCount = launchCount; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
