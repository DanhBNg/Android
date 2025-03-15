package com.example.tangthucac;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChapterReaderActivity extends AppCompatActivity {
    private TextView chapterTitle, chapterContent;
    private Button btnPrev, btnNext;
    private Story story;
    private List<Chapter> chapterList;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_reader);

        chapterTitle = findViewById(R.id.chapterTitle);
        chapterContent = findViewById(R.id.chapterContent);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        // Nhận dữ liệu từ Intent
        story = (Story) getIntent().getSerializableExtra("story");
        currentIndex = getIntent().getIntExtra("chapterIndex", 0);

        if (story != null) {
            // Chuyển đổi map chapters thành list
            Map<String, Chapter> chaptersMap = story.getChapters();
            chapterList = new ArrayList<>(chaptersMap.values());
            displayChapter(currentIndex);

            btnPrev.setOnClickListener(v -> {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayChapter(currentIndex);
                } else {
                    Toast.makeText(this, "Đây là chương đầu tiên", Toast.LENGTH_SHORT).show();
                }
            });

            btnNext.setOnClickListener(v -> {
                if (currentIndex < chapterList.size() - 1) {
                    currentIndex++;
                    displayChapter(currentIndex);
                } else {
                    Toast.makeText(this, "Đây là chương cuối cùng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void displayChapter(int index) {
        Chapter chapter = chapterList.get(index);
        chapterTitle.setText(chapter.getTitle());
        chapterContent.setText(chapter.getContent());
    }
}
