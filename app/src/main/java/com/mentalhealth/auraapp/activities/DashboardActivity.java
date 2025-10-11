package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.adapters.MoodHistoryAdapter;
import com.mentalhealth.auraapp.models.MoodEntry;
import com.mentalhealth.auraapp.utils.RecommendationEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    private TextView totalEntriesText;
    private TextView averageSentimentText;
    private TextView dominantEmotionText;
    private TextView weeklyTrendText;
    private TextView crisisAlertsText;
    private LinearLayout recommendationsLayout;
    private RecyclerView moodHistoryRecycler;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private List<MoodEntry> moodEntries;
    private MoodHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        totalEntriesText = findViewById(R.id.totalEntriesText);
        averageSentimentText = findViewById(R.id.averageSentimentText);
        dominantEmotionText = findViewById(R.id.dominantEmotionText);
        weeklyTrendText = findViewById(R.id.weeklyTrendText);
        crisisAlertsText = findViewById(R.id.crisisAlertsText);
        recommendationsLayout = findViewById(R.id.recommendationsLayout);
        moodHistoryRecycler = findViewById(R.id.moodHistoryRecycler);

        // Set up RecyclerView
        moodEntries = new ArrayList<>();
        adapter = new MoodHistoryAdapter(this, moodEntries);
        moodHistoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        moodHistoryRecycler.setAdapter(adapter);

        // Load mood data
        loadMoodData();
    }

    private void loadMoodData() {
        String userId = mAuth.getCurrentUser().getUid();

        // Get last 30 days of mood entries
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        long thirtyDaysAgo = calendar.getTimeInMillis();

        db.collection("mood_entries")
                .whereEqualTo("userId", userId)
                .whereGreaterThan("timestamp", thirtyDaysAgo)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    moodEntries.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        MoodEntry entry = document.toObject(MoodEntry.class);
                        moodEntries.add(entry);
                    }

                    Log.d(TAG, "Loaded " + moodEntries.size() + " mood entries");

                    // Update UI with statistics
                    calculateAndDisplayStats();

                    // Display personalized recommendations
                    displayRecommendations();

                    // Update RecyclerView
                    adapter.notifyDataSetChanged();

                    if (moodEntries.isEmpty()) {
                        Toast.makeText(this, "No mood entries yet. Start tracking!",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading mood entries", e);
                    Toast.makeText(this, "Error loading data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void calculateAndDisplayStats() {
        if (moodEntries.isEmpty()) {
            totalEntriesText.setText("Total Entries: 0");
            averageSentimentText.setText("No data yet");
            dominantEmotionText.setText("Start tracking your mood!");
            weeklyTrendText.setText("--");
            crisisAlertsText.setText("No alerts");
            return;
        }

        // Total entries
        totalEntriesText.setText("Total Entries: " + moodEntries.size());

        // Calculate average sentiment
        int totalSentiment = 0;
        Map<String, Integer> emotionCount = new HashMap<>();
        int crisisCount = 0;
        int last7DaysPositive = 0;
        int last7DaysNegative = 0;

        Calendar sevenDaysAgo = Calendar.getInstance();
        sevenDaysAgo.add(Calendar.DAY_OF_YEAR, -7);
        long sevenDaysTimestamp = sevenDaysAgo.getTimeInMillis();

        for (MoodEntry entry : moodEntries) {
            // Sentiment
            totalSentiment += entry.getSentimentScore();

            // Emotion frequency
            String emotion = entry.getSpecificEmotion();
            emotionCount.put(emotion, emotionCount.getOrDefault(emotion, 0) + 1);

            // Crisis detection
            if (entry.isCrisisDetected()) {
                crisisCount++;
            }

            // Weekly trend
            if (entry.getTimestamp() >= sevenDaysTimestamp) {
                if (entry.getSentimentScore() >= 0) {
                    last7DaysPositive++;
                } else {
                    last7DaysNegative++;
                }
            }
        }

        // Average sentiment
        int avgSentiment = totalSentiment / moodEntries.size();
        String sentimentText;
        String sentimentEmoji;

        if (avgSentiment >= 50) {
            sentimentText = "Very Positive";
            sentimentEmoji = "😄";
        } else if (avgSentiment >= 0) {
            sentimentText = "Positive";
            sentimentEmoji = "🙂";
        } else if (avgSentiment >= -50) {
            sentimentText = "Slightly Negative";
            sentimentEmoji = "😐";
        } else {
            sentimentText = "Negative";
            sentimentEmoji = "😔";
        }

        averageSentimentText.setText(sentimentEmoji + " " + sentimentText + " (" + avgSentiment + ")");

        // Dominant emotion
        String dominantEmotion = "";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : emotionCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                dominantEmotion = entry.getKey();
            }
        }
        dominantEmotionText.setText("Most Common: " + dominantEmotion + " (" + maxCount + "x)");

        // Weekly trend
        String trend;
        if (last7DaysPositive > last7DaysNegative) {
            trend = "📈 Improving - More positive moods this week";
        } else if (last7DaysNegative > last7DaysPositive) {
            trend = "📉 Declining - More negative moods this week";
        } else {
            trend = "➡️ Stable - Balanced mood this week";
        }
        weeklyTrendText.setText(trend);

        // Crisis alerts
        if (crisisCount > 0) {
            crisisAlertsText.setText("⚠️ " + crisisCount + " crisis indicator(s) detected");
            crisisAlertsText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            crisisAlertsText.setText("✅ No crisis indicators");
            crisisAlertsText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        // Check for sustained negative pattern (5+ consecutive days)
        checkSustainedNegativePattern();
    }

    private void displayRecommendations() {
        recommendationsLayout.removeAllViews();

        // Get personalized recommendations
        List<String> recommendations = RecommendationEngine.getRecommendations(moodEntries);
        String severity = RecommendationEngine.getCrisisSeverity(moodEntries);

        // Display each recommendation
        for (String recommendation : recommendations) {
            TextView tv = new TextView(this);
            tv.setText(recommendation);
            tv.setTextSize(16);
            tv.setTextColor(getResources().getColor(android.R.color.black));
            tv.setPadding(16, 12, 16, 12);
            tv.setLineSpacing(4, 1);
            recommendationsLayout.addView(tv);
        }

        // Show crisis warning if needed
        if (!severity.equals("NONE")) {
            showCrisisWarning(severity);
        }

        Log.d(TAG, "Displayed " + recommendations.size() + " recommendations. Crisis level: " + severity);
    }

    private void showCrisisWarning(String severity) {
        String title = "Mental Health Check";
        String message = "We've detected patterns that may need attention. ";

        if (severity.equals("HIGH")) {
            message += "Please consider professional help immediately. Your well-being is important.";
        } else if (severity.equals("MODERATE")) {
            message += "Consider reaching out to support resources or talking to someone you trust.";
        } else {
            message += "Keep monitoring your mood and don't hesitate to seek support if needed.";
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("View Resources", (dialog, which) -> {
                    Intent intent = new Intent(this, SupportResourcesActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("I'm OK", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void checkSustainedNegativePattern() {
        // Sort entries by timestamp (oldest first)
        List<MoodEntry> sortedEntries = new ArrayList<>(moodEntries);
        sortedEntries.sort((e1, e2) -> Long.compare(e1.getTimestamp(), e2.getTimestamp()));

        int consecutiveNegative = 0;
        for (MoodEntry entry : sortedEntries) {
            if (entry.getSentimentScore() < 0) {
                consecutiveNegative++;
                if (consecutiveNegative >= 5) {
                    // Show warning
                    showSustainedNegativeWarning();
                    break;
                }
            } else {
                consecutiveNegative = 0;
            }
        }
    }

    private void showSustainedNegativeWarning() {
        new AlertDialog.Builder(this)
                .setTitle("Pattern Detected")
                .setMessage("We've noticed a pattern of negative moods over several days. " +
                        "Would you like to access support resources?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(this, SupportResourcesActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Not now", null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}
