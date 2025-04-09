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
import com.example.tangthucac.activity.StoryDetailActivity;
import com.example.tangthucac.model.Story;

import java.util.List;

public class HotStoryAdapter extends RecyclerView.Adapter<HotStoryAdapter.ViewHolder> {
    private Context context;
    private List<Story> storyList;

    public HotStoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hot_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = storyList.get(position);

        holder.tvTitle.setText(story.getTitle());
        holder.tvAuthor.setText(story.getAuthor());
        holder.tvViews.setText(String.format("%,d views", story.getViews()));

        Glide.with(context)
                .load(story.getImage())
                .placeholder(R.drawable._a3b01d12f250412a001beeaf6a86a4c)
                .into(holder.ivCover);

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivHotCover);
            tvTitle = itemView.findViewById(R.id.tvHotTitle);
            tvAuthor = itemView.findViewById(R.id.tvHotAuthor);
            tvViews = itemView.findViewById(R.id.tvHotViews);
        }
    }
}