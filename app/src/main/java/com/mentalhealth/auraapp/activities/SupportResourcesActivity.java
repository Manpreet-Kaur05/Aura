package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mentalhealth.auraapp.R;

public class SupportResourcesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_resources);

        // Crisis Helplines Section
        setupHelplineButtons();

        // Coping Strategies Section
        setupCopingStrategies();

        // Professional Help Section
        setupProfessionalHelp();

        // Mindfulness Resources
        setupMindfulnessResources();
    }

    private void setupHelplineButtons() {
        Button aasraButton = findViewById(R.id.aasraButton);
        Button iCallButton = findViewById(R.id.iCallButton);
        Button snehaButton = findViewById(R.id.snehaButton);
        Button emergencyButton = findViewById(R.id.emergencyButton);

        aasraButton.setOnClickListener(v -> callHelpline("tel:9152987821"));
        iCallButton.setOnClickListener(v -> callHelpline("tel:02225521111"));
        snehaButton.setOnClickListener(v -> callHelpline("tel:04424640050"));
        emergencyButton.setOnClickListener(v -> callHelpline("tel:112"));
    }

    private void callHelpline(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        startActivity(intent);
    }

    private void setupCopingStrategies() {
        LinearLayout strategiesLayout = findViewById(R.id.strategiesLayout);

        String[] strategies = {
                "🌬️ Deep Breathing: Inhale for 4, hold for 4, exhale for 4",
                "🚶 Take a Walk: Change your environment for 10 minutes",
                "💧 Drink Water: Hydration affects mood",
                "📝 Write It Down: Journal your feelings without judgment",
                "👥 Call Someone: Reach out to a trusted friend",
                "🎵 Listen to Music: Choose calming or uplifting songs",
                "🧘 5-5-5 Grounding: Name 5 things you see, hear, feel",
                "⏰ Set a Timer: Give yourself 15 minutes before deciding anything",
        };

        for (String strategy : strategies) {
            TextView tv = new TextView(this);
            tv.setText(strategy);
            tv.setTextSize(16);
            tv.setPadding(16, 16, 16, 16);
            strategiesLayout.addView(tv);
        }
    }

    private void setupProfessionalHelp() {
        Button findTherapistButton = findViewById(R.id.findTherapistButton);
        Button teletherapyButton = findViewById(R.id.teletherapyButton);

        findTherapistButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.practo.com/mental-health"));
            startActivity(intent);
        });

        teletherapyButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.betterhelp.com"));
            startActivity(intent);
        });
    }

    private void setupMindfulnessResources() {
        Button breathingExerciseButton = findViewById(R.id.breathingExerciseButton);
        Button meditationButton = findViewById(R.id.meditationButton);

        breathingExerciseButton.setOnClickListener(v -> {
            // Navigate to breathing exercise
            Intent intent = new Intent(this, BreathingExerciseActivity.class);
            startActivity(intent);
        });

        meditationButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.headspace.com"));
            startActivity(intent);
        });
    }
}
