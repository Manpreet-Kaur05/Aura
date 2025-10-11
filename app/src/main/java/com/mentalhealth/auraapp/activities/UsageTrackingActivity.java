package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.models.UsageData;
import com.mentalhealth.auraapp.utils.UsageStatsHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UsageTrackingActivity extends AppCompatActivity {

    private static final String TAG = "UsageTrackingActivity";

    private TextView permissionStatusText;
    private TextView screenTimeText;
    private TextView topAppsText;
    private Button grantPermissionButton;
    private Button saveUsageButton;

    private UsageStatsHelper usageStatsHelper;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_tracking);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize helper
        usageStatsHelper = new UsageStatsHelper(this);

        // Initialize views
        permissionStatusText = findViewById(R.id.permissionStatusText);
        screenTimeText = findViewById(R.id.screenTimeText);
        topAppsText = findViewById(R.id.topAppsText);
        grantPermissionButton = findViewById(R.id.grantPermissionButton);
        saveUsageButton = findViewById(R.id.saveUsageButton);

        // Check permission and display stats
        checkPermissionAndDisplayStats();

        // Grant permission button
        grantPermissionButton.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "Please enable usage access for Aura", Toast.LENGTH_LONG).show();
        });

        // Save usage data button
        saveUsageButton.setOnClickListener(v -> saveUsageDataToFirestore());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh stats when returning from settings
        checkPermissionAndDisplayStats();
    }

    private void checkPermissionAndDisplayStats() {
        if (usageStatsHelper.hasPermission()) {
            permissionStatusText.setText("✅ Permission Granted");
            grantPermissionButton.setEnabled(false);
            saveUsageButton.setEnabled(true);

            displayUsageStats();
        } else {
            permissionStatusText.setText("❌ Permission Required");
            screenTimeText.setText("Enable usage access to see your screen time");
            topAppsText.setText("");
            grantPermissionButton.setEnabled(true);
            saveUsageButton.setEnabled(false);
        }
    }

    private void displayUsageStats() {
        // Get today's screen time
        long screenTime = usageStatsHelper.getTodayScreenTime();
        long hours = TimeUnit.MILLISECONDS.toHours(screenTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(screenTime) % 60;

        screenTimeText.setText("Today's Screen Time: " + hours + "h " + minutes + "m");

        // Get today's app usage
        String userId = mAuth.getCurrentUser().getUid();
        List<UsageData> usageDataList = usageStatsHelper.getTodayUsageStats(userId);

        // Display top 5 apps
        StringBuilder topApps = new StringBuilder("Top Apps Today:\n\n");
        int count = 0;
        for (UsageData data : usageDataList) {
            if (count >= 5) break;

            long appHours = TimeUnit.MILLISECONDS.toHours(data.getTotalTimeInForeground());
            long appMinutes = TimeUnit.MILLISECONDS.toMinutes(data.getTotalTimeInForeground()) % 60;

            String category = usageStatsHelper.categorizeApp(data.getPackageName());

            topApps.append(++count).append(". ")
                    .append(data.getAppName())
                    .append("\n   ")
                    .append(appHours).append("h ").append(appMinutes).append("m")
                    .append(" • ").append(category)
                    .append("\n\n");
        }

        if (usageDataList.isEmpty()) {
            topAppsText.setText("No app usage data available");
        } else {
            topAppsText.setText(topApps.toString());
        }

        Log.d(TAG, "Displayed usage stats for " + usageDataList.size() + " apps");
    }

    private void saveUsageDataToFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        List<UsageData> usageDataList = usageStatsHelper.getTodayUsageStats(userId);

        if (usageDataList.isEmpty()) {
            Toast.makeText(this, "No usage data to save", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create summary document
        Map<String, Object> usageSummary = new HashMap<>();
        usageSummary.put("userId", userId);
        usageSummary.put("timestamp", System.currentTimeMillis());
        usageSummary.put("totalScreenTime", usageStatsHelper.getTodayScreenTime());
        usageSummary.put("appCount", usageDataList.size());

        // Save to Firestore
        String docId = userId + "_" + System.currentTimeMillis();
        db.collection("usage_stats")
                .document(docId)
                .set(usageSummary)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Usage data saved successfully");
                    Toast.makeText(this, "Usage data saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving usage data", e);
                    Toast.makeText(this, "Error saving data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void analyzeBehavioralPatterns() {
        long screenTime = usageStatsHelper.getTodayScreenTime();
        long hours = TimeUnit.MILLISECONDS.toHours(screenTime);

        TextView insightsText = findViewById(R.id.insightsText);
        StringBuilder insights = new StringBuilder("📊 Behavioral Insights:\n\n");

        if (hours > 6) {
            insights.append("⚠️ High screen time detected (").append(hours).append(" hours)\n");
            insights.append("Consider: Digital detox breaks\n\n");
        }

        if (hours < 2) {
            insights.append("✅ Healthy screen time usage\n\n");
        }

        // Add to end of displayUsageStats()
        insightsText.setText(insights.toString());
    }

}
