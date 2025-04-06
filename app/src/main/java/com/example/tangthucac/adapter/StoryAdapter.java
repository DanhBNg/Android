package com.example.tangthucac.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tangthucac.R;
import com.example.tangthucac.model.Story;
import com.example.tangthucac.activity.StoryDetailActivity;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {
    private Context context;
    private List<Story> storyList;

    public StoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = storyList.get(position);
        holder.textTitle.setText(story.getTitle());
        holder.textAuthor.setText("Tác giả: " + story.getAuthor());

        // Load ảnh từ URL với Glide
        Glide.with(context).load(story.getImage()).into(holder.imageStory);

        holder.itemView.setOnClickListener(v -> {
            Log.d("RecyclerView", "Clicked on: " + story.getTitle());
            if (story != null) {
                Intent intent = new Intent(context, StoryDetailActivity.class);
                intent.putExtra("story", story);
                context.startActivity(intent);
            } else {
                Log.e("RecyclerView", "Story is null!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageStory;
        TextView textTitle, textAuthor;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageStory = itemView.findViewById(R.id.imageStory);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAuthor = itemView.findViewById(R.id.textAuthor);
        }
    }
}
