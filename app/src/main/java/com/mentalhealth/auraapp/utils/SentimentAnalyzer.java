package com.mentalhealth.auraapp.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SentimentAnalyzer {

    private static final String TAG = "SentimentAnalyzer";

    // Hugging Face API endpoint for sentiment analysis
    private static final String API_URL = "https://api-inference.huggingface.co/models/distilbert-base-uncased-finetuned-sst-2-english";

    // You can use this free API without authentication for testing
    // For production, get your API key from: https://huggingface.co/settings/tokens
    private static final String API_KEY = ""; // Leave empty for public access

    /**
     * Analyze sentiment of text using Hugging Face API
     * Returns sentiment score: -100 (very negative) to +100 (very positive)
     */
    public interface SentimentCallback {
        void onResult(int sentimentScore, String dominantEmotion);
        void onError(String error);
    }

    public static void analyzeSentiment(String text, SentimentCallback callback) {
        // Run in background thread
        new Thread(() -> {
            try {
                // Prepare API request
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                if (!API_KEY.isEmpty()) {
                    conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
                }
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                // Create request body
                JSONObject requestBody = new JSONObject();
                requestBody.put("inputs", text);

                // Send request
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Read response
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8)
                    );
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    // Parse response
                    parseSentimentResponse(response.toString(), callback);

                } else {
                    Log.e(TAG, "API Error: " + responseCode);
                    // Fallback to basic analysis
                    int fallbackScore = performBasicSentimentAnalysis(text);
                    callback.onResult(fallbackScore, "Unknown");
                }

                conn.disconnect();

            } catch (Exception e) {
                Log.e(TAG, "Error analyzing sentiment", e);
                // Fallback to basic analysis
                int fallbackScore = performBasicSentimentAnalysis(text);
                callback.onResult(fallbackScore, "Error");
            }
        }).start();
    }

    private static void parseSentimentResponse(String response, SentimentCallback callback) {
        try {
            JSONArray results = new JSONArray(response);
            if (results.length() > 0) {
                JSONArray sentiments = results.getJSONArray(0);

                double positiveScore = 0;
                double negativeScore = 0;

                for (int i = 0; i < sentiments.length(); i++) {
                    JSONObject sentiment = sentiments.getJSONObject(i);
                    String label = sentiment.getString("label");
                    double score = sentiment.getDouble("score");

                    if (label.equalsIgnoreCase("POSITIVE")) {
                        positiveScore = score;
                    } else if (label.equalsIgnoreCase("NEGATIVE")) {
                        negativeScore = score;
                    }
                }

                // Convert to -100 to +100 scale
                int sentimentScore;
                String dominantEmotion;

                if (positiveScore > negativeScore) {
                    sentimentScore = (int) (positiveScore * 100);
                    dominantEmotion = "Positive";
                } else {
                    sentimentScore = (int) (-negativeScore * 100);
                    dominantEmotion = "Negative";
                }

                Log.d(TAG, "Sentiment: " + sentimentScore + " (" + dominantEmotion + ")");
                callback.onResult(sentimentScore, dominantEmotion);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing response", e);
            int fallbackScore = performBasicSentimentAnalysis(response);
            callback.onResult(fallbackScore, "Unknown");
        }
    }

    /**
     * Fallback basic sentiment analysis using keyword matching
     * Used when API is unavailable
     */
    private static int performBasicSentimentAnalysis(String text) {
        String lowerText = text.toLowerCase();

        // Positive keywords
        String[] positiveWords = {
                "happy", "joy", "great", "excellent", "wonderful", "amazing",
                "love", "excited", "grateful", "thankful", "blessed", "good",
                "better", "best", "fantastic", "awesome", "proud", "hopeful",
                "peaceful", "calm", "content", "satisfied", "pleased"
        };

        // Negative keywords
        String[] negativeWords = {
                "sad", "depressed", "anxious", "worried", "scared", "afraid",
                "terrible", "awful", "horrible", "worst", "hate", "angry",
                "frustrated", "stressed", "overwhelmed", "hopeless", "worthless",
                "lonely", "empty", "hurt", "pain", "crying", "bad", "upset"
        };

        int positiveCount = 0;
        int negativeCount = 0;

        // Count positive words
        for (String word : positiveWords) {
            if (lowerText.contains(word)) {
                positiveCount++;
            }
        }

        // Count negative words
        for (String word : negativeWords) {
            if (lowerText.contains(word)) {
                negativeCount++;
            }
        }

        // Calculate sentiment score
        int totalWords = positiveCount + negativeCount;
        if (totalWords == 0) {
            return 0; // Neutral
        }

        double positiveRatio = (double) positiveCount / totalWords;
        int sentimentScore = (int) ((positiveRatio * 2 - 1) * 100);

        Log.d(TAG, "Basic sentiment: " + sentimentScore +
                " (pos:" + positiveCount + ", neg:" + negativeCount + ")");

        return sentimentScore;
    }

    /**
     * Analyze emotional intensity (0-10 scale)
     */
    public static int analyzeIntensity(String text) {
        String lowerText = text.toLowerCase();

        // High intensity words
        String[] intensityWords = {
                "very", "extremely", "incredibly", "absolutely", "totally",
                "completely", "utterly", "really", "so much", "too much",
                "overwhelming", "intense", "strong"
        };

        // Exclamation marks indicate intensity
        int exclamationCount = text.length() - text.replace("!", "").length();

        // Capital words indicate intensity
        int capsWords = 0;
        String[] words = text.split("\\s+");
        for (String word : words) {
            if (word.length() > 2 && word.equals(word.toUpperCase())) {
                capsWords++;
            }
        }

        int intensityScore = 5; // Base intensity

        // Add intensity based on markers
        for (String word : intensityWords) {
            if (lowerText.contains(word)) {
                intensityScore++;
            }
        }

        intensityScore += Math.min(exclamationCount, 2);
        intensityScore += Math.min(capsWords, 2);

        // Cap at 10
        return Math.min(intensityScore, 10);
    }
}
