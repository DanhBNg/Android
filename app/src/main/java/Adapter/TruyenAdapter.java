package Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.tangthucac.R;

import java.util.List;
import model.Truyen;

public class TruyenAdapter extends RecyclerView.Adapter<TruyenAdapter.TruyenViewHolder> {
        private Context context;
        private List<Truyen> truyenList;

        public TruyenAdapter(Context context, List<Truyen> truyenList) {
            this.context = context;
            this.truyenList = truyenList;
        }

        @NonNull
        @Override
        public TruyenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.truyen_decu, parent, false);
            return new TruyenViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TruyenViewHolder holder, int position) {
            Truyen truyen = truyenList.get(position);
            holder.txtTenTruyen.setText(truyen.getTenTruyen());
            holder.txtTheLoai.setText(truyen.getTheLoai());

            // Load ảnh từ URL bằng Glide
            Glide.with(context).load(truyen.getHinhAnhURL()).into(holder.imgTruyen);
        }

        @Override
        public int getItemCount() {
            return truyenList.size();
        }

        public static class TruyenViewHolder extends RecyclerView.ViewHolder {
            TextView txtTenTruyen, txtTheLoai;
            ImageView imgTruyen;

            public TruyenViewHolder(@NonNull View itemView) {
                super(itemView);
                txtTenTruyen = itemView.findViewById(R.id.txtTenTruyen);
                txtTheLoai = itemView.findViewById(R.id.txtTheLoai);
                imgTruyen = itemView.findViewById(R.id.imgTruyen);
            }
        }
    }


