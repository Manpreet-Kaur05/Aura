package com.mentalhealth.auraapp.utils;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.mentalhealth.auraapp.models.UsageData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class UsageStatsHelper {

    private static final String TAG = "UsageStatsHelper";
    private Context context;
    private UsageStatsManager usageStatsManager;
    private PackageManager packageManager;

    public UsageStatsHelper(Context context) {
        this.context = context;
        this.usageStatsManager = (UsageStatsManager)
                context.getSystemService(Context.USAGE_STATS_SERVICE);
        this.packageManager = context.getPackageManager();
    }

    /**
     * Check if app has usage stats permission
     */
    public boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager)
                    context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    context.getPackageName()
            );
            return mode == AppOpsManager.MODE_ALLOWED;
        }
        return false;
    }

    /**
     * Get total screen time for today (in milliseconds)
     */
    public long getTodayScreenTime() {
        long totalTime = 0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(
                startTime, endTime
        );

        for (UsageStats usageStats : stats.values()) {
            totalTime += usageStats.getTotalTimeInForeground();
        }

        Log.d(TAG, "Total screen time today: " + (totalTime / 1000 / 60) + " minutes");
        return totalTime;
    }

    /**
     * Get app usage statistics for a time range
     */
    public List<UsageData> getUsageStats(long startTime, long endTime, String userId) {
        List<UsageData> usageDataList = new ArrayList<>();

        if (!hasPermission()) {
            Log.w(TAG, "Usage stats permission not granted");
            return usageDataList;
        }

        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(
                startTime, endTime
        );

        for (UsageStats usageStats : stats.values()) {
            String packageName = usageStats.getPackageName();
            long totalTime = usageStats.getTotalTimeInForeground();
            int launchCount = usageStats.getLastTimeUsed() > 0 ? 1 : 0;

            // Only include apps with significant usage (>1 minute)
            if (totalTime > 60000) {
                String appName = getAppName(packageName);

                UsageData usageData = new UsageData(
                        userId,
                        packageName,
                        appName,
                        totalTime,
                        launchCount,
                        System.currentTimeMillis()
                );

                usageDataList.add(usageData);
            }
        }

        Log.d(TAG, "Retrieved " + usageDataList.size() + " app usage records");
        return usageDataList;
    }

    /**
     * Get today's usage statistics
     */
    public List<UsageData> getTodayUsageStats(String userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        return getUsageStats(startTime, endTime, userId);
    }

    /**
     * Get app name from package name
     */
    private String getAppName(String packageName) {
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
            return packageManager.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return packageName;
        }
    }

    /**
     * Categorize app usage (social media, productivity, etc.)
     */
    public String categorizeApp(String packageName) {
        packageName = packageName.toLowerCase();

        if (packageName.contains("facebook") || packageName.contains("instagram") ||
                packageName.contains("twitter") || packageName.contains("snapchat") ||
                packageName.contains("tiktok") || packageName.contains("whatsapp")) {
            return "Social Media";
        } else if (packageName.contains("game") || packageName.contains("play")) {
            return "Gaming";
        } else if (packageName.contains("youtube") || packageName.contains("netflix") ||
                packageName.contains("spotify") || packageName.contains("prime")) {
            return "Entertainment";
        } else if (packageName.contains("gmail") || packageName.contains("office") ||
                packageName.contains("docs") || packageName.contains("calendar")) {
            return "Productivity";
        } else if (packageName.contains("chrome") || packageName.contains("browser")) {
            return "Web Browsing";
        } else {
            return "Other";
        }
    }

    /**
     * Calculate average daily screen time for last 7 days
     */
    public long getAverageWeeklyScreenTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long startTime = calendar.getTimeInMillis();
        long endTime = System.currentTimeMillis();

        Map<String, UsageStats> stats = usageStatsManager.queryAndAggregateUsageStats(
                startTime, endTime
        );

        long totalTime = 0;
        for (UsageStats usageStats : stats.values()) {
            totalTime += usageStats.getTotalTimeInForeground();
        }

        return totalTime / 7; // Average per day
    }
}
