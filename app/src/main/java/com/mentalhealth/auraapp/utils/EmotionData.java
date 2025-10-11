package com.mentalhealth.auraapp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmotionData {

    // Comprehensive emotion mapping (40+ emotions)
    private static Map<String, List<String>> emotionMap;

    static {
        emotionMap = new HashMap<>();

        // HIGH ENERGY + POSITIVE (Activated Positive)
        List<String> highEnergyPositive = new ArrayList<>();
        highEnergyPositive.add("Excited");
        highEnergyPositive.add("Happy");
        highEnergyPositive.add("Energetic");
        highEnergyPositive.add("Joyful");
        highEnergyPositive.add("Enthusiastic");
        highEnergyPositive.add("Playful");
        highEnergyPositive.add("Confident");
        highEnergyPositive.add("Proud");
        highEnergyPositive.add("Motivated");
        highEnergyPositive.add("Inspired");
        highEnergyPositive.add("Adventurous");
        highEnergyPositive.add("Passionate");
        highEnergyPositive.add("Euphoric");
        highEnergyPositive.add("Ecstatic");
        emotionMap.put("high-energy-positive", highEnergyPositive);

        // LOW ENERGY + POSITIVE (Deactivated Positive)
        List<String> lowEnergyPositive = new ArrayList<>();
        lowEnergyPositive.add("Calm");
        lowEnergyPositive.add("Peaceful");
        lowEnergyPositive.add("Content");
        lowEnergyPositive.add("Relaxed");
        lowEnergyPositive.add("Grateful");
        lowEnergyPositive.add("Satisfied");
        lowEnergyPositive.add("Serene");
        lowEnergyPositive.add("Comfortable");
        lowEnergyPositive.add("Safe");
        lowEnergyPositive.add("Loved");
        lowEnergyPositive.add("Hopeful");
        lowEnergyPositive.add("Tranquil");
        lowEnergyPositive.add("Balanced");
        lowEnergyPositive.add("At ease");
        emotionMap.put("low-energy-positive", lowEnergyPositive);

        // HIGH ENERGY + NEGATIVE (Activated Negative)
        List<String> highEnergyNegative = new ArrayList<>();
        highEnergyNegative.add("Anxious");
        highEnergyNegative.add("Angry");
        highEnergyNegative.add("Stressed");
        highEnergyNegative.add("Frustrated");
        highEnergyNegative.add("Overwhelmed");
        highEnergyNegative.add("Panicked");
        highEnergyNegative.add("Irritated");
        highEnergyNegative.add("Restless");
        highEnergyNegative.add("Agitated");
        highEnergyNegative.add("Tense");
        highEnergyNegative.add("Nervous");
        highEnergyNegative.add("Worried");
        highEnergyNegative.add("Scared");
        highEnergyNegative.add("Defensive");
        emotionMap.put("high-energy-negative", highEnergyNegative);

        // LOW ENERGY + NEGATIVE (Deactivated Negative)
        List<String> lowEnergyNegative = new ArrayList<>();
        lowEnergyNegative.add("Sad");
        lowEnergyNegative.add("Depressed");
        lowEnergyNegative.add("Lonely");
        lowEnergyNegative.add("Tired");
        lowEnergyNegative.add("Hopeless");
        lowEnergyNegative.add("Bored");
        lowEnergyNegative.add("Numb");
        lowEnergyNegative.add("Empty");
        lowEnergyNegative.add("Disconnected");
        lowEnergyNegative.add("Apathetic");
        lowEnergyNegative.add("Withdrawn");
        lowEnergyNegative.add("Melancholic");
        lowEnergyNegative.add("Defeated");
        lowEnergyNegative.add("Drained");
        emotionMap.put("low-energy-negative", lowEnergyNegative);
    }

    public static List<String> getEmotionsForQuadrant(String quadrant) {
        return emotionMap.getOrDefault(quadrant, new ArrayList<>());
    }

    public static String getQuadrantDescription(String quadrant) {
        switch (quadrant) {
            case "high-energy-positive":
                return "High Energy + Positive";
            case "low-energy-positive":
                return "Low Energy + Positive";
            case "high-energy-negative":
                return "High Energy + Negative";
            case "low-energy-negative":
                return "Low Energy + Negative";
            default:
                return "Unknown";
        }
    }

    // Emotion valence score (-100 to +100)
    public static int getEmotionValence(String quadrant) {
        switch (quadrant) {
            case "high-energy-positive":
                return 80;  // Very positive
            case "low-energy-positive":
                return 60;  // Moderately positive
            case "high-energy-negative":
                return -70; // Very negative
            case "low-energy-negative":
                return -85; // Extremely negative
            default:
                return 0;
        }
    }

    // Crisis-related emotions that need immediate attention
    public static boolean isCrisisEmotion(String emotion) {
        String[] crisisEmotions = {
                "Hopeless", "Defeated", "Empty", "Numb",
                "Panicked", "Overwhelmed", "Disconnected"
        };

        for (String crisis : crisisEmotions) {
            if (emotion.equalsIgnoreCase(crisis)) {
                return true;
            }
        }
        return false;
    }
}
