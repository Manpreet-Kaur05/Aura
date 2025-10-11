package com.mentalhealth.auraapp.utils;

import com.mentalhealth.auraapp.models.MoodEntry;
import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {

    /**
     * Generate personalized recommendations based on mood patterns
     */
    public static List<String> getRecommendations(List<MoodEntry> recentEntries) {
        List<String> recommendations = new ArrayList<>();

        if (recentEntries.isEmpty()) {
            recommendations.add("Start tracking your mood daily to get personalized insights");
            return recommendations;
        }

        // Analyze patterns
        int negativeCount = 0;
        int consecutiveNegative = 0;
        boolean hasHighIntensity = false;
        boolean hasCrisis = false;

        for (MoodEntry entry : recentEntries) {
            if (entry.getSentimentScore() < 0) {
                negativeCount++;
                consecutiveNegative++;
            } else {
                consecutiveNegative = 0;
            }

            if (entry.getEmotionIntensity() >= 8) {
                hasHighIntensity = true;
            }

            if (entry.isCrisisDetected()) {
                hasCrisis = true;
            }
        }

        // Generate recommendations based on patterns
        if (hasCrisis) {
            recommendations.add("🆘 Consider reaching out to a mental health professional");
            recommendations.add("☎️ Crisis helplines are available 24/7 - tap Support Resources");
        }

        if (consecutiveNegative >= 3) {
            recommendations.add("📅 You've had several difficult days - consider talking to someone");
            recommendations.add("🧘 Try the guided breathing exercise to reduce stress");
        }

        if (hasHighIntensity) {
            recommendations.add("💪 Your emotions are intense - practice grounding techniques");
            recommendations.add("📱 Reduce screen time before bed for better sleep");
        }

        if (negativeCount >= recentEntries.size() * 0.7) {
            recommendations.add("🌟 Focus on small wins - track positive moments daily");
            recommendations.add("🚶 Physical activity can boost mood - try a 10-minute walk");
        } else {
            recommendations.add("✨ Great progress! Keep tracking to maintain awareness");
            recommendations.add("📊 Review your dashboard to identify helpful patterns");
        }

        return recommendations;
    }

    /**
     * Determine crisis severity level
     */
    public static String getCrisisSeverity(List<MoodEntry> recentEntries) {
        int crisisCount = 0;
        int consecutiveNegative = 0;

        for (MoodEntry entry : recentEntries) {
            if (entry.isCrisisDetected()) crisisCount++;
            if (entry.getSentimentScore() < -50) consecutiveNegative++;
        }

        if (crisisCount >= 3) return "HIGH";
        if (crisisCount >= 1 || consecutiveNegative >= 5) return "MODERATE";
        if (consecutiveNegative >= 3) return "LOW";
        return "NONE";
    }
}
