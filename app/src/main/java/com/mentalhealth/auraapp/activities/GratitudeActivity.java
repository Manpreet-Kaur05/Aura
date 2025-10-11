package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mentalhealth.auraapp.R;
import java.util.HashMap;
import java.util.Map;

public class GratitudeActivity extends AppCompatActivity {

    private static final String TAG = "GratitudeActivity";

    private EditText gratitude1, gratitude2, gratitude3;
    private Button saveButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gratitude);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        gratitude1 = findViewById(R.id.gratitude1);
        gratitude2 = findViewById(R.id.gratitude2);
        gratitude3 = findViewById(R.id.gratitude3);
        saveButton = findViewById(R.id.saveGratitudeButton);

        saveButton.setOnClickListener(v -> saveGratitude());
    }

    private void saveGratitude() {
        String g1 = gratitude1.getText().toString().trim();
        String g2 = gratitude2.getText().toString().trim();
        String g3 = gratitude3.getText().toString().trim();

        if (g1.isEmpty() && g2.isEmpty() && g3.isEmpty()) {
            Toast.makeText(this, "Add at least one thing you're grateful for",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate positivity score BEFORE creating the map
        final int positivityScore = calculatePositivityScore(g1, g2, g3);

        Map<String, Object> gratitudeEntry = new HashMap<>();
        gratitudeEntry.put("userId", mAuth.getCurrentUser().getUid());
        gratitudeEntry.put("timestamp", System.currentTimeMillis());
        gratitudeEntry.put("item1", g1);
        gratitudeEntry.put("item2", g2);
        gratitudeEntry.put("item3", g3);
        gratitudeEntry.put("positivityScore", positivityScore);

        db.collection("gratitude_entries")
                .add(gratitudeEntry)
                .addOnSuccessListener(doc -> {
                    Log.d(TAG, "Gratitude saved: " + doc.getId());
                    Toast.makeText(this, "Gratitude saved! 🌟 Positivity: " + positivityScore + "%",
                            Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving gratitude", e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private int calculatePositivityScore(String g1, String g2, String g3) {
        int score = 0;
        if (!g1.isEmpty()) score += 33;
        if (!g2.isEmpty()) score += 33;
        if (!g3.isEmpty()) score += 34;
        return score;
    }
}
