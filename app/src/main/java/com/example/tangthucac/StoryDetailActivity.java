package com.example.tangthucac;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class StoryDetailActivity extends AppCompatActivity {
    private ImageView storyImage;
    private TextView storyTitle, storyAuthor, storyViews;
    private RecyclerView chapterRecyclerView;
    private Button readButton;
    private Story story;
    private List<Chapter> chapterList;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
            finish(); // Close activity if there's an error
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
}