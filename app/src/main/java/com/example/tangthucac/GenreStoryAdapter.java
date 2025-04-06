package com.example.tangthucac;

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
import com.squareup.picasso.Picasso;
import java.util.List;

public class GenreStoryAdapter extends RecyclerView.Adapter<GenreStoryAdapter.GenreStoryViewHolder> {

    private Context context;
    private List<Story> storyList;

    public GenreStoryAdapter(Context context, List<Story> storyList) {
        this.context = context;
        this.storyList = storyList;
    }

    @NonNull
    @Override
    public GenreStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre_story, parent, false);
        return new GenreStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreStoryViewHolder holder, int position) {
        Story story = storyList.get(position);

        // Gán dữ liệu cho các thành phần giao diện
        holder.tvTitle.setText(story.getTitle());
        holder.tvAuthor.setText("Tác giả: " + story.getAuthor());
        holder.tvViews.setText("Lượt xem: " + story.getViews());

        // Tải hình ảnh bìa truyện bằng Picasso
        if (story.getImage() != null && !story.getImage().isEmpty()) {
            Picasso.get()
                    .load(story.getImage())
                    .placeholder(R.drawable._a3b01d12f250412a001beeaf6a86a4c) // Hình ảnh placeholder khi đang tải
                    .error(R.drawable.c_ch_n_nh_n) // Hình ảnh hiển thị nếu lỗi
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.hoagh1);
        }

        // Thêm sự kiện click để chuyển sang StoryDetailActivity
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

    static class GenreStoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle, tvAuthor, tvViews;

        public GenreStoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivGenreStoryImage);
            tvTitle = itemView.findViewById(R.id.tvGenreStoryTitle);
            tvAuthor = itemView.findViewById(R.id.tvGenreStoryAuthor);
            tvViews = itemView.findViewById(R.id.tvGenreStoryViews);
        }
    }
}