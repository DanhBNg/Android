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
import com.example.tangthucac.model.Library;
import com.example.tangthucac.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder> {

    private Context context;
    private List<Library> libraryList;

    public LibraryAdapter(Context context, List<Library> libraryList) {
        this.context = context;
        this.libraryList = libraryList;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        Library savedStory = libraryList.get(position);

        holder.tvTitle.setText(savedStory.getTitle());
        holder.tvAuthor.setText(savedStory.getAuthor());
        Glide.with(context).load(savedStory.getImage()).into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> {
            // Truy ngược từ title để lấy full story data
            DatabaseReference storiesRef = FirebaseDatabase.getInstance().getReference("stories");
            storiesRef.orderByChild("title").equalTo(savedStory.getTitle())
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
    public int getItemCount() {
        return libraryList.size();
    }

    public static class LibraryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor;
        ImageView ivImage;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitleLib);
            tvAuthor = itemView.findViewById(R.id.tvAuthorLib);
            ivImage = itemView.findViewById(R.id.imgLib);
        }
    }
}

