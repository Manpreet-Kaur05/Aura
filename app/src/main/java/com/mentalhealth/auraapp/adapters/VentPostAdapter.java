package com.mentalhealth.auraapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mentalhealth.auraapp.R;
import com.mentalhealth.auraapp.activities.VentActivity;
import com.mentalhealth.auraapp.models.VentPost;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentPostAdapter extends RecyclerView.Adapter<VentPostAdapter.ViewHolder> {

    private Context context;
    private List<VentPost> posts;
    private SimpleDateFormat dateFormat;

    public VentPostAdapter(Context context, List<VentPost> posts) {
        this.context = context;
        this.posts = posts;
        this.dateFormat = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_vent_post, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VentPost post = posts.get(position);

        holder.anonymousNameText.setText(post.getAnonymousName());
        holder.messageText.setText(post.getMessage());
        holder.timeText.setText(dateFormat.format(new Date(post.getTimestamp())));
        holder.supportCountText.setText(post.getSupportCount() + " 💙");

        holder.sendSupportButton.setOnClickListener(v -> {
            if (context instanceof VentActivity) {
                ((VentActivity) context).sendSupport(post.getPostId(), post.getAnonymousName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView anonymousNameText;
        TextView messageText;
        TextView timeText;
        TextView supportCountText;
        Button sendSupportButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anonymousNameText = itemView.findViewById(R.id.anonymousNameText);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
            supportCountText = itemView.findViewById(R.id.supportCountText);
            sendSupportButton = itemView.findViewById(R.id.sendSupportButton);
        }
    }
}
