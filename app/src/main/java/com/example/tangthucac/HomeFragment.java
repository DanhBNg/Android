package com.example.tangthucac;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tangthucac.API.FirebaseAPI;
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
    private RecyclerView recyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList = new ArrayList<>();
    private FirebaseAPI api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewStories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        storyAdapter = new StoryAdapter(getContext(), storyList);
        recyclerView.setAdapter(storyAdapter);

        // Khởi tạo Retrofit và gọi API
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
            public void onResponse(Call<Map<String, Story>> call, Response<Map<String, Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    storyList.clear();
                    storyList.addAll(response.body().values());
                    storyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Story>> call, Throwable t) {
                Log.e("Firebase", "Lỗi: " + t.getMessage());
            }
        });
    }
}