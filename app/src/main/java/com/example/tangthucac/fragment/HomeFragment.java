package com.example.tangthucac.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tangthucac.R;
import com.example.tangthucac.activity.MainActivity;
import com.example.tangthucac.activity.SearchActivity;
import com.example.tangthucac.adapter.BannerAdapter;
import com.example.tangthucac.adapter.HotStoryAdapter;
import com.example.tangthucac.adapter.StoryAdapter;
import com.example.tangthucac.model.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerBanner;
    private BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;
    private Handler bannerHandler;
    private Runnable bannerRunnable;
    private EditText ptSearch;

    private RecyclerView recyclerHotStories;
    private HotStoryAdapter hotStoryAdapter;
    private List<Story> hotStoryList = new ArrayList<>();

    private RecyclerView recyclerViewStories;
    private StoryAdapter storyAdapter;
    private List<Story> storyList = new ArrayList<>();

    // Thêm DatabaseReference
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo Firebase DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        ptSearch = view.findViewById(R.id.ptSearch);

        // Cấu hình ViewPager2 cho banner quảng cáo (giữ nguyên)
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.banner1);
        bannerImages.add(R.drawable.banner2);
        bannerImages.add(R.drawable.banner3);

        bannerAdapter = new BannerAdapter(bannerImages);
        viewPagerBanner.setAdapter(bannerAdapter);

        // Tự động chạy banner (giữ nguyên)
        bannerHandler = new Handler();
        bannerRunnable = () -> {
            int nextItem = viewPagerBanner.getCurrentItem() + 1;
            if (nextItem >= bannerImages.size()) {
                nextItem = 0;
            }
            viewPagerBanner.setCurrentItem(nextItem, true);
            bannerHandler.postDelayed(bannerRunnable, 5000);
        };
        bannerHandler.postDelayed(bannerRunnable, 5000);

        // Cấu hình RecyclerView hiển thị danh sách truyện
        recyclerViewStories = view.findViewById(R.id.recyclerViewStories);
        recyclerViewStories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        storyAdapter = new StoryAdapter(getContext(), storyList);
        recyclerViewStories.setAdapter(storyAdapter);


        fetchStoriesFromFirebase();

        recyclerHotStories = view.findViewById(R.id.recyclerHotStories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerHotStories.setLayoutManager(gridLayoutManager);

        hotStoryAdapter = new HotStoryAdapter(getContext(), hotStoryList);
        recyclerHotStories.setAdapter(hotStoryAdapter);

        ptSearch.setOnClickListener(v -> {
            // Tạo Intent để chuyển sang SearchActivity
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchStoriesFromFirebase() {
        databaseReference.child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storyList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Story story = dataSnapshot.getValue(Story.class);
                    if (story != null) {
                        storyList.add(story);
                       //
                    if (story.isHot() && hotStoryList.size()<=8 ) {
                            hotStoryList.add(story);
                        }
                    }
                }
                storyAdapter.notifyDataSetChanged();
                hotStoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi khi đọc dữ liệu: " + error.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerHandler != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
        }
    }
}