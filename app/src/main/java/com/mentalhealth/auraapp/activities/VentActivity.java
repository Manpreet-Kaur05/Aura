package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.adapters.VentPostAdapter;
import com.mentalhealth.auraapp.models.VentPost;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VentActivity extends AppCompatActivity {

    private static final String TAG = "VentActivity";

    private EditText ventInput;
    private Button postButton;
    private RecyclerView ventRecycler;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private List<VentPost> ventPosts;
    private VentPostAdapter adapter;

    private static final String[] ANONYMOUS_NAMES = {
            "Brave Soul", "Hope Seeker", "Silent Warrior", "Phoenix Rising",
            "Gentle Heart", "Courageous Mind", "Peaceful Spirit", "Strong Voice",
            "Calm Waters", "Bright Star", "Resilient One", "Kind Soul"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vent);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ventInput = findViewById(R.id.ventInput);
        postButton = findViewById(R.id.postButton);
        ventRecycler = findViewById(R.id.ventRecycler);
        progressBar = findViewById(R.id.progressBar);

        ventPosts = new ArrayList<>();
        adapter = new VentPostAdapter(this, ventPosts);
        ventRecycler.setLayoutManager(new LinearLayoutManager(this));
        ventRecycler.setAdapter(adapter);

        postButton.setOnClickListener(v -> postVent());

        loadVentPosts();
    }

    private void postVent() {
        String message = ventInput.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(this, "Please write something to share",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.length() < 10) {
            Toast.makeText(this, "Please write at least 10 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        postButton.setEnabled(false);

        // Generate anonymous name
        String anonymousName = ANONYMOUS_NAMES[new Random().nextInt(ANONYMOUS_NAMES.length)];

        // Create post
        String postId = db.collection("vent_posts").document().getId();
        VentPost post = new VentPost(
                postId,
                mAuth.getCurrentUser().getUid(),
                anonymousName,
                message,
                System.currentTimeMillis()
        );

        // Save to Firestore
        db.collection("vent_posts")
                .document(postId)
                .set(post)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Vent posted successfully");
                    ventInput.setText("");
                    progressBar.setVisibility(View.GONE);
                    postButton.setEnabled(true);

                    Toast.makeText(this, "Your message has been shared anonymously 💙",
                            Toast.LENGTH_LONG).show();

                    // Refresh posts
                    loadVentPosts();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error posting vent", e);
                    progressBar.setVisibility(View.GONE);
                    postButton.setEnabled(true);

                    Toast.makeText(this, "Error: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void loadVentPosts() {
        db.collection("vent_posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ventPosts.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        VentPost post = document.toObject(VentPost.class);
                        ventPosts.add(post);
                    }

                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Loaded " + ventPosts.size() + " vent posts");

                    if (ventPosts.isEmpty()) {
                        Toast.makeText(this, "Be the first to share! 💙",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading vent posts", e);
                    Toast.makeText(this, "Error loading posts: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    public void sendSupport(String postId, String anonymousName) {
        // Quick support messages
        String[] supportMessages = {
                "You're not alone in this. Stay strong! 💙",
                "Sending you strength and positive vibes ✨",
                "Things will get better. Hang in there! 🌟",
                "I hear you. Your feelings are valid 💜",
                "You're braver than you know. Keep going! 🌈",
                "Sending virtual hugs your way 🤗",
                "One day at a time. You've got this! 💪"
        };

        String message = supportMessages[new Random().nextInt(supportMessages.length)];

        // Create support message
        String messageId = db.collection("support_messages").document().getId();
        com.mentalhealth.auraapp.models.SupportMessage supportMsg =
                new com.mentalhealth.auraapp.models.SupportMessage(
                        messageId,
                        postId,
                        mAuth.getCurrentUser().getUid(),
                        message,
                        System.currentTimeMillis()
                );

        // Save to Firestore
        db.collection("support_messages")
                .document(messageId)
                .set(supportMsg)
                .addOnSuccessListener(aVoid -> {
                    // Update support count
                    db.collection("vent_posts").document(postId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                VentPost post = documentSnapshot.toObject(VentPost.class);
                                if (post != null) {
                                    post.setSupportCount(post.getSupportCount() + 1);
                                    db.collection("vent_posts").document(postId).set(post);
                                }
                            });

                    Toast.makeText(this, "Support sent to " + anonymousName + " 💙",
                            Toast.LENGTH_SHORT).show();

                    loadVentPosts();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error sending support", Toast.LENGTH_SHORT).show();
                });
    }
}
