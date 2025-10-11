package com.mentalhealth.auraapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.models.MoodEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodHistoryAdapter extends RecyclerView.Adapter<MoodHistoryAdapter.ViewHolder> {

    private Context context;
    private List<MoodEntry> moodEntries;
    private SimpleDateFormat dateFormat;

    public MoodHistoryAdapter(Context context, List<MoodEntry> moodEntries) {
        this.context = context;
        this.moodEntries = moodEntries;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_mood_history, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoodEntry entry = moodEntries.get(position);

        // Format date
        String dateTime = dateFormat.format(new Date(entry.getTimestamp()));
        holder.dateTimeText.setText(dateTime);

        // Emotion with emoji
        String emotionEmoji = getEmotionEmoji(entry.getQuadrant());
        holder.emotionText.setText(emotionEmoji + " " + entry.getSpecificEmotion());

        // Quadrant
        holder.quadrantText.setText(getQuadrantName(entry.getQuadrant()));

        // Intensity
        holder.intensityText.setText("Intensity: " + entry.getEmotionIntensity() + "/10");

        // Journal preview (first 50 characters)
        String journal = entry.getJournalText();
        if (journal != null && !journal.isEmpty()) {
            String preview = journal.length() > 50 ?
                    journal.substring(0, 50) + "..." : journal;
            holder.journalPreviewText.setText(preview);
            holder.journalPreviewText.setVisibility(View.VISIBLE);
        } else {
            holder.journalPreviewText.setVisibility(View.GONE);
        }

        // Crisis indicator
        if (entry.isCrisisDetected()) {
            holder.crisisIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.crisisIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return moodEntries.size();
    }

    private String getEmotionEmoji(String quadrant) {
        switch (quadrant) {
            case "high-energy-positive": return "🌟";
            case "low-energy-positive": return "😌";
            case "high-energy-negative": return "😰";
            case "low-energy-negative": return "😔";
            default: return "😐";
        }
    }

    private String getQuadrantName(String quadrant) {
        switch (quadrant) {
            case "high-energy-positive": return "High Energy + Positive";
            case "low-energy-positive": return "Low Energy + Positive";
            case "high-energy-negative": return "High Energy + Negative";
            case "low-energy-negative": return "Low Energy + Negative";
            default: return "Unknown";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeText;
        TextView emotionText;
        TextView quadrantText;
        TextView intensityText;
        TextView journalPreviewText;
        TextView crisisIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
            emotionText = itemView.findViewById(R.id.emotionText);
            quadrantText = itemView.findViewById(R.id.quadrantText);
            intensityText = itemView.findViewById(R.id.intensityText);
            journalPreviewText = itemView.findViewById(R.id.journalPreviewText);
            crisisIndicator = itemView.findViewById(R.id.crisisIndicator);
        }
    }
}
