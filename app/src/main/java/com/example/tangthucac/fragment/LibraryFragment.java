package com.example.tangthucac.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tangthucac.R;
import com.example.tangthucac.SharedViewModel;
import com.example.tangthucac.adapter.LibraryAdapter;
import com.example.tangthucac.model.Library;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    private RecyclerView rcvLib;
    private LibraryAdapter adapter;
    private List<Library> libraryItems;
    private SharedViewModel sharedViewModel;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;

    public LibraryFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        // Ánh xạ RecyclerView
        rcvLib = view.findViewById(R.id.rcvLib);
        rcvLib.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo list và adapter
        libraryItems = new ArrayList<>();
        adapter = new LibraryAdapter(getContext(), libraryItems);
        rcvLib.setAdapter(adapter);
        // refresh ui
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Lắng nghe sự kiện refresh
        sharedViewModel.getShouldRefresh().observe(getViewLifecycleOwner(), shouldRefresh -> {
            if (shouldRefresh != null && shouldRefresh) {
                loadLibrary(); // Tải lại dữ liệu
                sharedViewModel.refreshComplete(); // Đánh dấu hoàn thành
            }
        });

        // Load dữ liệu từ Firebase
        loadLibrary();

        return view;
    }

    private void loadLibrary() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)
                .child("Library");

        // Hủy listener cũ nếu có
        if (valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                libraryItems.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Library item = itemSnapshot.getValue(Library.class);
                    if (item != null) {
                        libraryItems.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        };

        databaseReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseReference != null && valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }
}
