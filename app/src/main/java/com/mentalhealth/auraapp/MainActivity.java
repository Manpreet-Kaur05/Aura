package com.mentalhealth.auraapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.mentalhealth.auraapp.activities.ChatbotActivity;
import com.mentalhealth.auraapp.activities.DashboardActivity;
import com.mentalhealth.auraapp.activities.GratitudeActivity;
import com.mentalhealth.auraapp.activities.LoginActivity;
import com.mentalhealth.auraapp.activities.MoodTrackingActivity;
import com.mentalhealth.auraapp.activities.SupportResourcesActivity;
import com.mentalhealth.auraapp.activities.UsageTrackingActivity;
import com.mentalhealth.auraapp.activities.VentActivity;
import com.mentalhealth.auraapp.activities.MultimodalCheckInActivity;

import com.mentalhealth.auraapp.activities.MultimodalCheckInActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private TextView welcomeTextView;
    private Button trackMoodButton;
    private Button usageTrackingButton;
    private Button viewHistoryButton;
    private Button supportResourcesButton;
    private Button gratitudeButton;
    private Button chatbotButton;
    private Button ventButton;
    private Button logoutButton;
    private Button multimodalButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // Initialize Firebase
            FirebaseApp.initializeApp(this);
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            storage = FirebaseStorage.getInstance();

            // Check if user is logged in
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                navigateToLogin();
                return;
            }

            // Initialize views
            welcomeTextView = findViewById(R.id.welcomeTextView);
            trackMoodButton = findViewById(R.id.trackMoodButton);
            usageTrackingButton = findViewById(R.id.usageTrackingButton);
            viewHistoryButton = findViewById(R.id.viewHistoryButton);
            supportResourcesButton = findViewById(R.id.supportResourcesButton);
            gratitudeButton = findViewById(R.id.gratitudeButton);
            chatbotButton = findViewById(R.id.chatbotButton);
            ventButton = findViewById(R.id.ventButton);
            logoutButton = findViewById(R.id.logoutButton);
            multimodalButton = findViewById(R.id.multimodalButton);


            // Set welcome message
            String email = currentUser.getEmail();
            String displayName = email != null ? email.split("@")[0] : "User";
            welcomeTextView.setText("Welcome back, " + displayName + "!");

            // Button click listeners
            trackMoodButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MoodTrackingActivity.class);
                startActivity(intent);
            });

            usageTrackingButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, UsageTrackingActivity.class);
                startActivity(intent);
            });

            viewHistoryButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                startActivity(intent);
            });

            supportResourcesButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SupportResourcesActivity.class);
                startActivity(intent);
            });

            gratitudeButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, GratitudeActivity.class);
                startActivity(intent);
            });

            chatbotButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ChatbotActivity.class);
                startActivity(intent);
            });

            ventButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, VentActivity.class);
                startActivity(intent);
            });

            logoutButton.setOnClickListener(v -> {
                mAuth.signOut();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            });

            multimodalButton = findViewById(R.id.multimodalButton);

            multimodalButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MultimodalCheckInActivity.class);
                startActivity(intent);
            });


            testFirebaseConnection();

        } catch (Exception e) {
            Log.e(TAG, "Firebase initialization failed: " + e.getMessage());
            Toast.makeText(this, "Firebase Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private void testFirebaseConnection() {
        if (mAuth != null && db != null && storage != null) {
            Log.d(TAG, "✅ Firebase Auth: Connected");
            Log.d(TAG, "✅ Firestore: Connected");
            Log.d(TAG, "✅ Storage: Connected");
        } else {
            Log.e(TAG, "❌ Firebase connection failed");
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth != null && mAuth.getCurrentUser() == null) {
            navigateToLogin();
        }
    }
}
