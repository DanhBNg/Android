package com.example.tangthucac.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tangthucac.R;
import com.example.tangthucac.adapter.GenreAdapter;

import java.util.Arrays;
import java.util.List;

public class GenresFragment extends Fragment {

    private RecyclerView recyclerViewGenres;
    private GenreAdapter genreAdapter;
    private List<String> genreList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        // Khởi tạo RecyclerView
        recyclerViewGenres = view.findViewById(R.id.recyclerViewGenres);
        recyclerViewGenres.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 cột

        // Danh sách các thể loại
        genreList = Arrays.asList(
                "Tiên Hiệp", "Kiếm Hiệp", "Ngôn Tình", "Đô Thị", "Quan Trường", "Võng Du",
                "Khoa Huyễn", "Hệ Thống", "Huyền Huyễn", "Dị Giới", "Dị Năng", "Quân Sự",
                "Lịch Sử", "Xuyên Không", "Xuyên Nhanh", "Trọng Sinh", "Trinh Thám", "Thám Hiểm",
                "Linh Dị", "Sắc", "Ngược", "Sủng", "Cung Đấu", "Nữ Cường", "Gia Đấu", "Đông Phương"
        );

        // Khởi tạo Adapter và gán cho RecyclerView
        genreAdapter = new GenreAdapter(getContext(), genreList);
        recyclerViewGenres.setAdapter(genreAdapter);

        return view;
    }
}