package com.example.tangthucac.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tangthucac.R;
import com.example.tangthucac.adapter.SearchResultAdapter;
import com.example.tangthucac.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private AutoCompleteTextView actvSearch;
    private Button btnSearch;
    private RecyclerView rvResults;
    private SearchResultAdapter resultAdapter;
    private DatabaseReference storiesRef;
    private final List<String> allStoryTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setupRecyclerView();
        setupFirebase();
        loadAllStoryTitles();
        setupSearchAutoComplete();
        setupSearchButton();
    }

    private void initializeViews() {
        actvSearch = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        rvResults = findViewById(R.id.rvSearchResults);
    }

    private void setupRecyclerView() {
        rvResults.setLayoutManager(new LinearLayoutManager(this));
        resultAdapter = new SearchResultAdapter(this, new ArrayList<>());
        rvResults.setAdapter(resultAdapter);
    }

    private void setupFirebase() {
        storiesRef = FirebaseDatabase.getInstance().getReference("stories");
    }

    private void loadAllStoryTitles() {
        storiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                    String title = storySnapshot.child("title").getValue(String.class);
                    if (title != null) allStoryTitles.add(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Lỗi tải dữ liệu");
            }
        });
    }

    private void setupSearchAutoComplete() {
        ArrayAdapter<String> suggestionAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, allStoryTitles);
        actvSearch.setAdapter(suggestionAdapter);
        actvSearch.setThreshold(1);
    }

    private void setupSearchButton() {
        btnSearch.setOnClickListener(v -> performSearch(actvSearch.getText().toString()));
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            showToast("Vui lòng nhập từ khóa");
            return;
        }

        storiesRef.orderByChild("title")
                .startAt(query)
                .endAt(query + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Story> results = new ArrayList<>();
                        for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                            Story story = storySnapshot.getValue(Story.class);
                            if (story != null) {
                                story.setId(storySnapshot.getKey());
                                results.add(story);
                            }
                        }
                        resultAdapter.updateData(results);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("Lỗi tìm kiếm");
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}