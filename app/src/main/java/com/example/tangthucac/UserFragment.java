package com.example.tangthucac;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class UserFragment extends Fragment {
    private Button btnLogout, btn_log, btn_sign;
    private TextView txtUserId, txtUserEmail;
    private FirebaseAuth mAuth;
    private GoogleSignInClient ggS;
    private DatabaseReference databaseReference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btn_Logout);
        btn_log = view.findViewById(R.id.btn_log);
        btn_sign = view.findViewById(R.id.btn_sign);
        txtUserId = view.findViewById(R.id.txtUserId);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập
            txtUserEmail.setText("Email: " + currentUser.getEmail());

            // Lấy tham chiếu Database
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

            // Kiểm tra nếu ID đã lưu trong database chưa
            databaseReference.child("id").get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    // Nếu đã có ID -> Hiển thị
                    txtUserId.setText("ID: " + task.getResult().getValue().toString());
                } else {
                    // Nếu chưa có ID -> Tạo ID mới và lưu
                    String newId = generateRandomId();
                    databaseReference.child("id").setValue(newId);
                    txtUserId.setText("ID: " + newId);
                }
            });

            // Ẩn nút Login & Sign Up
            btn_log.setVisibility(View.GONE);
            btn_sign.setVisibility(View.GONE);
        } else {
            // Người dùng chưa đăng nhập, ẩn Logout
            btnLogout.setVisibility(View.GONE);
            txtUserId.setText("ID: ");
            txtUserEmail.setText("Email: ");
        }

        // Khởi tạo GoogleSignInClient để đăng xuất Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Dùng Web Client ID từ Firebase
                .requestEmail()
                .build();
        ggS = GoogleSignIn.getClient(requireActivity(), gso);

        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(getActivity(), SignUpActivity.class)));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        return view;
    }
    private void signOut() {
        // Đăng xuất Firebase
        mAuth.signOut();

        // Đăng xuất Google
        ggS.signOut().addOnCompleteListener(requireActivity(), task -> {
            // Sau khi đăng xuất, chuyển về màn hình đăng nhập
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish(); // Đóng Activity hiện tại
        });
    }
    private String generateRandomId() {
        int length = 10; // Độ dài chuỗi
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomId = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomId.append(chars.charAt(random.nextInt(chars.length())));
        }
        return randomId.toString();
    }
}
