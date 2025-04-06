package com.example.tangthucac;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class GenreStoriesActivity extends AppCompatActivity {

    private TextView tvGenreTitle;
    private RecyclerView recyclerViewGenreStories;
    private GenreStoryAdapter genreStoryAdapter; // Sử dụng GenreStoryAdapter
    private List<Story> storyList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_stories);

        // Lấy thể loại từ Intent
        String genre = getIntent().getStringExtra("GENRE");

        // Khởi tạo giao diện
        tvGenreTitle = findViewById(R.id.tvGenreTitle);
        tvGenreTitle.setText("Thể loại: " + genre);

        recyclerViewGenreStories = findViewById(R.id.recyclerViewGenreStories);
        recyclerViewGenreStories.setLayoutManager(new LinearLayoutManager(this));

        storyList = new ArrayList<>();
        genreStoryAdapter = new GenreStoryAdapter(this, storyList); // Khởi tạo GenreStoryAdapter
        recyclerViewGenreStories.setAdapter(genreStoryAdapter);

        // Khởi tạo Firebase DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("stories");

        // Lấy dữ liệu truyện theo thể loại
        fetchStoriesByGenre(genre);
    }

    private void fetchStoriesByGenre(String genre) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Story story = dataSnapshot.getValue(Story.class);
                    if (story != null && genre.equals(story.getGenre())) {
                        storyList.add(story);
                    }
                }
                genreStoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi khi đọc dữ liệu: " + error.getMessage());
            }
        });
    }
}