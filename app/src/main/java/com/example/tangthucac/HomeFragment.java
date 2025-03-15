package com.example.tangthucac;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tangthucac.API.FirebaseAPI;
import com.example.tangthucac.BannerAdapter;
import com.example.tangthucac.StoryAdapter;
import com.example.tangthucac.Story;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final String BASE_URL = "https://apptruyenfu-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private ViewPager2 viewPagerBanner;
    private BannerAdapter bannerAdapter;
    private List<Integer> bannerImages;
    private Handler bannerHandler;
    private Runnable bannerRunnable;

    private RecyclerView recyclerViewStories;
    private StoryAdapter storyAdapter;
    private List<Story> storyList = new ArrayList<>();
    private FirebaseAPI api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Cấu hình ViewPager2 cho banner quảng cáo
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner);
        bannerImages = new ArrayList<>();
        bannerImages.add(R.drawable.banner1);
        bannerImages.add(R.drawable.banner2);
        bannerImages.add(R.drawable.banner3);

        bannerAdapter = new BannerAdapter(bannerImages);
        viewPagerBanner.setAdapter(bannerAdapter);

        // Tự động chạy banner
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

        // Khởi tạo Retrofit để gọi API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(FirebaseAPI.class);
        fetchStories();

        return view;
    }

    private void fetchStories() {
        api.getStories().enqueue(new Callback<Map<String, Story>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Story>> call, @NonNull Response<Map<String, Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    storyList.clear();
                    storyList.addAll(response.body().values());
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, Story>> call, @NonNull Throwable t) {
                Log.e("Firebase", "Lỗi: " + t.getMessage());
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
