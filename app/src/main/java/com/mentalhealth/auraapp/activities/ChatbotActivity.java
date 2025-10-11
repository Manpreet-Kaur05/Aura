package com.mentalhealth.auraapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.adapters.ChatAdapter;
import com.mentalhealth.auraapp.models.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView chatRecycler;
    private EditText messageInput;
    private ImageButton sendButton;
    private ChatAdapter adapter;
    private List<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        chatRecycler = findViewById(R.id.chatRecycler);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messages = new ArrayList<>();
        adapter = new ChatAdapter(messages);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(adapter);

        // Welcome message
        addBotMessage("Hi! I'm your Aura assistant. How can I help you today?\n\n" +
                "Try: 'I feel anxious', 'Need coping strategies', 'Vent mode'");

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;

        addUserMessage(text);
        messageInput.setText("");

        // Generate bot response
        String response = generateResponse(text);
        addBotMessage(response);

        chatRecycler.smoothScrollToPosition(messages.size() - 1);
    }

    private String generateResponse(String userMessage) {
        String msg = userMessage.toLowerCase();

        // Emotional support
        if (msg.contains("anxious") || msg.contains("anxiety") || msg.contains("worried")) {
            return "I understand anxiety can be overwhelming. Try this:\n\n" +
                    "🌬️ Take 3 deep breaths (4 seconds in, 4 out)\n" +
                    "🧘 Try our breathing exercise\n" +
                    "📝 Write down what's worrying you\n\n" +
                    "Would you like me to guide you through a calming exercise?";
        }

        if (msg.contains("sad") || msg.contains("depressed") || msg.contains("down")) {
            return "I'm sorry you're feeling this way. Your feelings are valid. 💙\n\n" +
                    "Small steps that might help:\n" +
                    "☀️ Get some sunlight (even 5 mins)\n" +
                    "🎵 Listen to uplifting music\n" +
                    "📞 Reach out to someone you trust\n\n" +
                    "Would you like to see support resources?";
        }

        if (msg.contains("stress") || msg.contains("overwhelmed")) {
            return "Stress is tough. Let's break it down:\n\n" +
                    "✅ What's one small thing you can do now?\n" +
                    "⏰ Try the 5-5-5 rule: 5 things you see, hear, feel\n" +
                    "🚶 Take a 5-minute walk\n\n" +
                    "Remember: You don't have to do everything at once.";
        }

        // Positive responses
        if (msg.contains("happy") || msg.contains("good") || msg.contains("great")) {
            return "That's wonderful! 🌟 I'm so glad you're feeling good.\n\n" +
                    "Want to capture this moment?\n" +
                    "✨ Add to your gratitude journal\n" +
                    "📝 Track this positive mood\n\n" +
                    "What made today good?";
        }

        // Features
        if (msg.contains("track") || msg.contains("mood")) {
            return "Great! You can track your mood from the home screen.\n\n" +
                    "📝 Track Your Mood button lets you:\n" +
                    "• Select your emotion\n" +
                    "• Set intensity level\n" +
                    "• Write in your journal\n\n" +
                    "Regular tracking helps identify patterns!";
        }

        if (msg.contains("help") || msg.contains("support") || msg.contains("crisis")) {
            return "I'm here for you. 💙\n\n" +
                    "📞 24/7 Crisis Helplines:\n" +
                    "• AASRA: 9152987821\n" +
                    "• iCall: 022-25521111\n\n" +
                    "Or tap 'Support Resources' on the home screen.\n\n" +
                    "You're not alone in this.";
        }

        if (msg.contains("vent") || msg.contains("talk") || msg.contains("share")) {
            return "Sometimes you just need to get it out. That's completely okay.\n\n" +
                    "💬 You can:\n" +
                    "• Write in your journal (private)\n" +
                    "• Use vent mode (coming soon - anonymous sharing)\n" +
                    "• Call a helpline\n\n" +
                    "I'm listening. Tell me more if you'd like.";
        }

        if (msg.contains("coping") || msg.contains("strategies")) {
            return "Here are some coping strategies:\n\n" +
                    "🌬️ Breathing Exercise - Try our guided exercise\n" +
                    "🚶 Move Your Body - Even 5 mins helps\n" +
                    "💧 Stay Hydrated - Dehydration affects mood\n" +
                    "📝 Journal - Express your feelings\n" +
                    "🎵 Music Therapy - Calming playlists\n\n" +
                    "Which sounds good to you?";
        }

        if (msg.contains("sleep") || msg.contains("tired")) {
            return "Sleep is so important for mental health! 😴\n\n" +
                    "Tips for better sleep:\n" +
                    "📱 Reduce screen time before bed\n" +
                    "⏰ Consistent sleep schedule\n" +
                    "🧘 Try our breathing exercise\n" +
                    "☕ Avoid caffeine after 2pm\n\n" +
                    "Check your phone usage stats to see late-night patterns.";
        }

        // Default responses
        if (msg.contains("thank") || msg.contains("thanks")) {
            return "You're very welcome! 😊 I'm always here to help.\n\n" +
                    "Remember: Taking care of your mental health is strength, not weakness.";
        }

        if (msg.contains("vent") || msg.contains("share anonymously") || msg.contains("anonymous")) {
            return "I understand you need a safe space to express yourself. 💜\n\n" +
                    "You can share your feelings anonymously in our community vent space.\n\n" +
                    "Would you like to:\n" +
                    "📝 Share your thoughts anonymously\n" +
                    "💙 Read and support others\n\n" +
                    "Tap the 'Anonymous Vent' option from the home screen to get started.";
        }

        // Fallback
        return "I'm here to support you. I can help with:\n\n" +
                "💙 Emotional support & coping strategies\n" +
                "📊 Understanding your mood patterns\n" +
                "🆘 Crisis resources\n" +
                "✨ Mindfulness & gratitude\n\n" +
                "Just tell me what you need. How are you feeling today?";
    }

    private void addUserMessage(String text) {
        messages.add(new ChatMessage(text, true, System.currentTimeMillis()));
        adapter.notifyItemInserted(messages.size() - 1);
    }

    private void addBotMessage(String text) {
        // Slight delay for natural feel
        new android.os.Handler().postDelayed(() -> {
            messages.add(new ChatMessage(text, false, System.currentTimeMillis()));
            adapter.notifyItemInserted(messages.size() - 1);
            chatRecycler.smoothScrollToPosition(messages.size() - 1);
        }, 500);
    }
}
