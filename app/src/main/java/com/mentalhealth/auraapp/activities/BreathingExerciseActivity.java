package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mentalhealth.auraapp.R;

public class BreathingExerciseActivity extends AppCompatActivity {

    private View breathingCircle;
    private TextView instructionText;
    private TextView timerText;
    private Button startButton;

    private Handler handler;
    private int cycleCount = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_exercise);

        breathingCircle = findViewById(R.id.breathingCircle);
        instructionText = findViewById(R.id.instructionText);
        timerText = findViewById(R.id.timerText);
        startButton = findViewById(R.id.startButton);

        handler = new Handler();

        startButton.setOnClickListener(v -> {
            if (!isRunning) {
                startBreathingExercise();
            }
        });
    }

    private void startBreathingExercise() {
        isRunning = true;
        cycleCount = 0;
        startButton.setEnabled(false);

        runBreathingCycle();
    }

    private void runBreathingCycle() {
        if (cycleCount >= 5) {
            // Exercise complete
            instructionText.setText("Great job! You completed 5 cycles.");
            timerText.setText("Exercise Complete ✓");
            startButton.setEnabled(true);
            startButton.setText("Start Again");
            isRunning = false;
            return;
        }

        cycleCount++;
        timerText.setText("Cycle " + cycleCount + " of 5");

        // Inhale (4 seconds)
        instructionText.setText("Breathe In...");
        animateCircle(1.5f, 4000);

        handler.postDelayed(() -> {
            // Hold (4 seconds)
            instructionText.setText("Hold...");

            handler.postDelayed(() -> {
                // Exhale (4 seconds)
                instructionText.setText("Breathe Out...");
                animateCircle(1.0f, 4000);

                handler.postDelayed(() -> {
                    // Next cycle
                    runBreathingCycle();
                }, 4000);
            }, 4000);
        }, 4000);
    }

    private void animateCircle(float scale, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(breathingCircle, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(breathingCircle, "scaleY", scale);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        scaleX.start();
        scaleY.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
