package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import com.mentalhealth.auraapp.utils.SentimentAnalyzer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.models.MoodEntry;
import com.mentalhealth.auraapp.utils.EmotionData;

import java.util.List;

public class MoodTrackingActivity extends AppCompatActivity {

    private static final String TAG = "MoodTrackingActivity";

    private RadioGroup quadrantRadioGroup;
    private Spinner emotionSpinner;
    private SeekBar intensitySeekBar;
    private TextView intensityValueText;
    private EditText journalEditText;
    private EditText triggerEditText;
    private Button saveMoodButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedQuadrant = "";
    private long selectionStartTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_tracking);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        quadrantRadioGroup = findViewById(R.id.quadrantRadioGroup);
        emotionSpinner = findViewById(R.id.emotionSpinner);
        intensitySeekBar = findViewById(R.id.intensitySeekBar);
        intensityValueText = findViewById(R.id.intensityValueText);
        journalEditText = findViewById(R.id.journalEditText);
        triggerEditText = findViewById(R.id.triggerEditText);
        saveMoodButton = findViewById(R.id.saveMoodButton);
        progressBar = findViewById(R.id.progressBar);

        // Start tracking selection time
        selectionStartTime = System.currentTimeMillis();

        // Set up quadrant selection
        setupQuadrantSelection();

        // Set up emotions spinner with default list
        setupEmotionsSpinner("high-energy-positive");

        // Set up intensity slider
        setupIntensitySlider();

        // Save button click
        saveMoodButton.setOnClickListener(v -> saveMoodEntry());
    }

    private void setupQuadrantSelection() {
        quadrantRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            if (radioButton != null) {
                String quadrant = radioButton.getTag().toString();
                selectedQuadrant = quadrant;
                setupEmotionsSpinner(quadrant);
                Log.d(TAG, "Selected quadrant: " + quadrant);
            }
        });
    }

    private void setupEmotionsSpinner(String quadrant) {
        List<String> emotionsList = EmotionData.getEmotionsForQuadrant(quadrant);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                emotionsList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        emotionSpinner.setAdapter(adapter);
    }

    private void setupIntensitySlider() {
        intensitySeekBar.setMax(10);
        intensitySeekBar.setProgress(5); // Default medium intensity
        intensityValueText.setText("5/10");

        intensitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ensure minimum value is 1
                if (progress < 1) {
                    seekBar.setProgress(1);
                    progress = 1;
                }
                intensityValueText.setText(progress + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void saveMoodEntry() {
        // Validation
        if (selectedQuadrant.isEmpty()) {
            Toast.makeText(this, "Please select a mood quadrant", Toast.LENGTH_SHORT).show();
            return;
        }

        String emotion = emotionSpinner.getSelectedItem().toString();
        String journalText = journalEditText.getText().toString().trim();
        String triggerContext = triggerEditText.getText().toString().trim();
        int intensity = intensitySeekBar.getProgress();

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        saveMoodButton.setEnabled(false);

        // Calculate selection duration
        long selectionDuration = System.currentTimeMillis() - selectionStartTime;

        // Create mood entry
        String userId = mAuth.getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();
        String entryId = db.collection("mood_entries").document().getId();

        MoodEntry moodEntry = new MoodEntry(
                entryId,
                userId,
                timestamp,
                selectedQuadrant,
                emotion,
                journalText,
                intensity,
                selectionDuration,
                triggerContext
        );

        // Set base sentiment score from quadrant
        moodEntry.setSentimentScore(EmotionData.getEmotionValence(selectedQuadrant));

        // Analyze journal text if provided
        if (!journalText.isEmpty()) {
            analyzeJournalText(moodEntry, journalText);

            // Use AI sentiment analysis to enhance score
            SentimentAnalyzer.analyzeSentiment(journalText, new SentimentAnalyzer.SentimentCallback() {
                @Override
                public void onResult(int aiSentimentScore, String dominantEmotion) {
                    // Blend AI score with quadrant score (60% AI, 40% quadrant)
                    int blendedScore = (int) (aiSentimentScore * 0.6 +
                            moodEntry.getSentimentScore() * 0.4);
                    moodEntry.setSentimentScore(blendedScore);

                    // Analyze intensity from text
                    int textIntensity = SentimentAnalyzer.analyzeIntensity(journalText);
                    // Average with user-selected intensity
                    int finalIntensity = (textIntensity + intensity) / 2;
                    moodEntry.setEmotionIntensity(finalIntensity);

                    Log.d(TAG, "AI Sentiment: " + aiSentimentScore +
                            ", Blended: " + blendedScore +
                            ", Intensity: " + finalIntensity);

                    // Save to Firestore after AI analysis
                    saveToFirestore(moodEntry);
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Sentiment analysis error: " + error);
                    // Continue with basic analysis
                    saveToFirestore(moodEntry);
                }
            });
        } else {
            // No journal text, save immediately
            saveToFirestore(moodEntry);
        }
    }

    private void saveToFirestore(MoodEntry moodEntry) {
        // Crisis detection
        boolean isCrisis = EmotionData.isCrisisEmotion(moodEntry.getSpecificEmotion()) ||
                moodEntry.isContainsCrisisKeywords();
        moodEntry.setCrisisDetected(isCrisis);

        // Save to Firestore
        db.collection("mood_entries")
                .document(moodEntry.getEntryId())
                .set(moodEntry)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Mood entry saved successfully");
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        saveMoodButton.setEnabled(true);

                        // Show crisis alert if needed
                        if (isCrisis) {
                            showCrisisSupport();
                        } else {
                            Toast.makeText(this, "Mood saved successfully!",
                                    Toast.LENGTH_SHORT).show();
                            finish(); // Return to MainActivity
                        }

                        // Clear form
                        clearForm();
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving mood entry", e);
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        saveMoodButton.setEnabled(true);
                        Toast.makeText(this, "Error saving mood: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    });
                });
    }


    private void analyzeJournalText(MoodEntry moodEntry, String text) {
        // Convert to lowercase for analysis
        String lowerText = text.toLowerCase();

        // Count words
        String[] words = text.trim().split("\\s+");
        moodEntry.setWordCount(words.length);

        // Count self-references (I, me, my, myself, mine)
        int selfRefCount = countOccurrences(lowerText, new String[]{
                "i ", " i ", "i'm", "i've", "i'd", "i'll",
                "me ", " me ", "my ", " my ", "myself", "mine"
        });
        moodEntry.setSelfReferenceCount(selfRefCount);

        // Count negative keywords
        String[] negativeKeywords = {
                "sad", "depressed", "anxious", "worried", "scared", "afraid",
                "hopeless", "worthless", "tired", "exhausted", "lonely", "alone",
                "hate", "angry", "frustrated", "overwhelmed", "stressed", "panic",
                "hurt", "pain", "crying", "cry", "death", "die", "suicide",
                "kill", "end", "give up", "can't", "never", "nothing", "nobody",
                "awful", "terrible", "horrible", "worst", "bad", "fail"
        };
        int negCount = countOccurrences(lowerText, negativeKeywords);
        moodEntry.setNegativeKeywordCount(negCount);

        // Count positive keywords
        String[] positiveKeywords = {
                "happy", "joy", "grateful", "thankful", "blessed", "love",
                "excited", "proud", "accomplished", "better", "good", "great",
                "wonderful", "amazing", "peaceful", "calm", "relaxed", "hope",
                "hopeful", "optimistic", "positive", "confident", "strong"
        };
        int posCount = countOccurrences(lowerText, positiveKeywords);
        moodEntry.setPositiveKeywordCount(posCount);

        // Crisis keyword detection
        String[] crisisKeywords = {
                "suicide", "suicidal", "kill myself", "end it all", "die",
                "death", "self harm", "cut myself", "hurt myself", "give up",
                "no point", "better off dead", "can't go on", "want to die"
        };
        boolean hasCrisisKeywords = containsAny(lowerText, crisisKeywords);
        moodEntry.setContainsCrisisKeywords(hasCrisisKeywords);

        Log.d(TAG, "Journal Analysis - Words: " + words.length +
                ", Self-refs: " + selfRefCount +
                ", Negative: " + negCount +
                ", Positive: " + posCount +
                ", Crisis: " + hasCrisisKeywords);
    }

    private int countOccurrences(String text, String[] keywords) {
        int count = 0;
        for (String keyword : keywords) {
            int index = 0;
            while ((index = text.indexOf(keyword, index)) != -1) {
                count++;
                index += keyword.length();
            }
        }
        return count;
    }

    private boolean containsAny(String text, String[] keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private void showCrisisSupport() {
        new AlertDialog.Builder(this)
                .setTitle("We're Here for You")
                .setMessage("It seems like you're going through a difficult time. " +
                        "Please know that you're not alone, and help is available.\n\n" +
                        "Would you like to access support resources?")
                .setPositiveButton("Show Resources", (dialog, which) -> {
                    showSupportResources();
                })
                .setNegativeButton("Not Now", (dialog, which) -> {
                    Toast.makeText(this, "Remember: You can access support anytime from the main menu",
                            Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNeutralButton("Emergency Help", (dialog, which) -> {
                    showEmergencyContacts();
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showSupportResources() {
        new AlertDialog.Builder(this)
                .setTitle("Support Resources")
                .setMessage("National Crisis Helplines:\n\n" +
                        "• Vandrevala Foundation: 1860-2662-345\n" +
                        "• AASRA: 91-22-27546669\n" +
                        "• iCall: 022-25521111\n" +
                        "• Sneha Foundation: 044-24640050\n\n" +
                        "These services are free, confidential, and available 24/7.")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void showEmergencyContacts() {
        new AlertDialog.Builder(this)
                .setTitle("Emergency Contacts")
                .setMessage("If you're in immediate danger:\n\n" +
                        "• Emergency Services: 112\n" +
                        "• Police: 100\n" +
                        "• Ambulance: 102\n\n" +
                        "Please reach out to a trusted friend, family member, or mental health professional.")
                .setPositiveButton("OK", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void clearForm() {
        quadrantRadioGroup.clearCheck();
        journalEditText.setText("");
        triggerEditText.setText("");
        intensitySeekBar.setProgress(5);
        selectedQuadrant = "";
        selectionStartTime = System.currentTimeMillis();
        setupEmotionsSpinner("high-energy-positive");
    }
}
