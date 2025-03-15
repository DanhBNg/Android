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

        // Ánh xạ các view từ layout activity_story_detail.xml
        storyImage = findViewById(R.id.storyImage);
        storyTitle = findViewById(R.id.storyTitle);
        storyAuthor = findViewById(R.id.storyAuthor);
        storyViews = findViewById(R.id.storyViews);
        chapterRecyclerView = findViewById(R.id.chapterRecyclerView);
        readButton = findViewById(R.id.readButton);

        // Nhận dữ liệu từ Intent
        story = (Story) getIntent().getSerializableExtra("story");

        if (story != null) {
            storyTitle.setText(story.getTitle());
            storyAuthor.setText("Tác giả: " + story.getAuthor());
            storyViews.setText("Lượt xem: " + story.getViews());
            Glide.with(this).load(story.getImage()).into(storyImage);

            // Chuyển đổi map chapters thành List
            Map<String, Chapter> chaptersMap = story.getChapters();
            chapterList = new ArrayList<>(chaptersMap.values());

            // Sự kiện khi nhấn nút "Đọc truyện" -> chuyển đến chương đầu tiên
            readButton.setOnClickListener(v -> {
                Intent intent = new Intent(StoryDetailActivity.this, ChapterReaderActivity.class);
                intent.putExtra("story", story);
                intent.putExtra("chapterIndex", 0); // Chuyển đến chương đầu tiên
                startActivity(intent);
            });

            // Thiết lập RecyclerView cho danh sách chương
            ChapterAdapter adapter = new ChapterAdapter(this, chapterList, position -> {
                // Khi bấm vào chương, chuyển sang ChapterReaderActivity với index của chương
                Intent intent = new Intent(StoryDetailActivity.this, ChapterReaderActivity.class);
                intent.putExtra("story", story);
                intent.putExtra("chapterIndex", position);
                startActivity(intent);
            });
            chapterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            chapterRecyclerView.setAdapter(adapter);
        }
    }
}
