package com.example.tangthucac;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    private Context context;
    private List<String> genreList;

    public GenreAdapter(Context context, List<String> genreList) {
        this.context = context;
        this.genreList = genreList;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        String genre = genreList.get(position);
        holder.genreButton.setText(genre);
        holder.genreButton.setOnClickListener(v -> {
            // Khi nhấn vào nút, mở Activity mới và truyền thể loại
            Intent intent = new Intent(context, GenreStoriesActivity.class);
            intent.putExtra("GENRE", genre);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    static class GenreViewHolder extends RecyclerView.ViewHolder {
        Button genreButton;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            genreButton = itemView.findViewById(R.id.genreButton);
        }
    }
}