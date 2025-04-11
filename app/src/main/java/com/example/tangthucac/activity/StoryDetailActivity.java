package com.example.tangthucac.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tangthucac.R;
import com.example.tangthucac.SharedViewModel;
import com.example.tangthucac.adapter.ChapterAdapter;
import com.example.tangthucac.model.Chapter;
import com.example.tangthucac.model.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryDetailActivity extends AppCompatActivity {
    private ImageView storyImage;
    private TextView storyTitle, storyAuthor, storyViews;
    private RecyclerView chapterRecyclerView;
    private Button readButton, saveButton;
    private Story story;
    private List<Chapter> chapterList;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        // Initialize views
        storyImage = findViewById(R.id.storyImage);
        storyTitle = findViewById(R.id.storyTitle);
        storyAuthor = findViewById(R.id.storyAuthor);
        storyViews = findViewById(R.id.storyViews);
        chapterRecyclerView = findViewById(R.id.chapterRecyclerView);
        readButton = findViewById(R.id.readButton);
        saveButton = findViewById(R.id.btn_save_story);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Setup RecyclerView with empty adapter first to avoid "No adapter attached" warning
        chapterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chapterRecyclerView.setAdapter(new ChapterAdapter(this, new ArrayList<>(), null));

        try {
            story = (Story) getIntent().getSerializableExtra("story");

            if (story != null) {
                storyTitle.setText(story.getTitle());
                storyAuthor.setText("Tác giả: " + story.getAuthor());
                storyViews.setText("Lượt xem: " + story.getViews());
                Glide.with(this).load(story.getImage()).into(storyImage);

                // Get and sort chapters
                chapterList = getSortedChapters(story.getChapters());

                // Setup adapter with sorted chapters
                ChapterAdapter adapter = new ChapterAdapter(this, chapterList, position -> {
                    Intent intent = new Intent(StoryDetailActivity.this, ChapterReaderActivity.class);
                    intent.putExtra("story", story);
                    intent.putExtra("chapterIndex", position);
                    startActivity(intent);
                });
                chapterRecyclerView.setAdapter(adapter);

                // Read button click
                readButton.setOnClickListener(v -> {
                    Intent intent = new Intent(StoryDetailActivity.this, ChapterReaderActivity.class);
                    intent.putExtra("story", story);
                    intent.putExtra("chapterIndex", 0);
                    startActivity(intent);
                });
                saveButton.setOnClickListener(v -> saveStoryToLibrary());
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    private List<Chapter> getSortedChapters(Map<String, Chapter> chaptersMap) {
        List<Chapter> chapters = new ArrayList<>(chaptersMap.values());

        Collections.sort(chapters, new Comparator<Chapter>() {
            @Override
            public int compare(Chapter c1, Chapter c2) {
                try {
                    // Extract number from keys like "chapter1", "chapter2", etc.
                    String key1 = getKeyForChapter(chaptersMap, c1).replace("chapter", "");
                    String key2 = getKeyForChapter(chaptersMap, c2).replace("chapter", "");
                    return Integer.compare(Integer.parseInt(key1), Integer.parseInt(key2));
                } catch (NumberFormatException e) {
                    return 0; // If parsing fails, maintain original order
                }
            }
        });

        return chapters;
    }

    private String getKeyForChapter(Map<String, Chapter> map, Chapter chapter) {
        for (Map.Entry<String, Chapter> entry : map.entrySet()) {
            if (entry.getValue().equals(chapter)) {
                return entry.getKey();
            }
        }
        return "0";
    }
    private void saveStoryToLibrary() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Bạn cần đăng nhập để lưu truyện", Toast.LENGTH_SHORT).show();
            return;
        }

        if (story == null) {
            Toast.makeText(this, "Không thể lưu truyện", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)
                .child("Library")
                .child(story.getTitle());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(StoryDetailActivity.this, "Truyện đã được lưu trước đó!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> savedStory = new HashMap<>();
                    savedStory.put("title", story.getTitle());
                    savedStory.put("author", story.getAuthor());
                    savedStory.put("image", story.getImage());

                    ref.setValue(savedStory)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(StoryDetailActivity.this, "Đã lưu vào thư viện", Toast.LENGTH_SHORT).show();
                                // Chỉ gọi requestRefresh sau khi lưu thành công
                                sharedViewModel.requestRefresh();
                            })
                            .addOnFailureListener(e -> Toast.makeText(StoryDetailActivity.this, "Lỗi khi lưu: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StoryDetailActivity.this, "Lỗi kết nối: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}