package com.example.tangthucac.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tangthucac.R;
import com.example.tangthucac.activity.StoryDetailActivity;
import com.example.tangthucac.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private final List<Story> stories;
    private final Context context;

    public SearchResultAdapter(Context context, List<Story> stories) {
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story story = stories.get(position);
        holder.tvTitle.setText(story.getTitle());
        holder.tvAuthor.setText(story.getAuthor());
        Glide.with(context).load(story.getImage()).into(holder.ivCover);

        holder.itemView.setOnClickListener(v -> {
            // Truy ngược từ title để lấy full story data
            DatabaseReference storiesRef = FirebaseDatabase.getInstance().getReference("stories");
            storiesRef.orderByChild("title").equalTo(story.getTitle())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot storySnap : snapshot.getChildren()) {
                                    Story story = storySnap.getValue(Story.class);
                                    story.setId(storySnap.getKey()); // Lấy ID thực sự của story

                                    Intent intent = new Intent(context, StoryDetailActivity.class);
                                    intent.putExtra("story", story);
                                    context.startActivity(intent);
                                    return;
                                }
                            }
                            Toast.makeText(context, "Không tìm thấy truyện", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Lỗi truy vấn", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }



    @Override
    public int getItemCount() { return stories.size(); }

    public void updateData(List<Story> newStories) {
        stories.clear();
        stories.addAll(newStories);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor;
        ImageView ivCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitleSearch);
            tvAuthor = itemView.findViewById(R.id.tvAuthorSearch);
            ivCover = itemView.findViewById(R.id.imgSearch);
        }
    }
}
