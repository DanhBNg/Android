package com.example.tangthucac.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;

import com.example.tangthucac.activity.ChatActivity;
import com.example.tangthucac.activity.LoginActivity;
import com.example.tangthucac.R;
import com.example.tangthucac.activity.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class UserFragment extends Fragment {
    private TextView txtUserEmail, txtUserId, fakebtnLogin, fakebtnSignup, fakebtnLogout, fakebtnAi;
    private FirebaseAuth mAuth;
    private GoogleSignInClient ggS;
    private DatabaseReference databaseReference;
    private SwitchCompat darkModeSwitch;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String DARK_MODE_KEY = "darkMode";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_user, container, false);



        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Áp dụng dark mode trước khi inflate layout
        boolean isDarkMode = sharedPreferences.getBoolean(DARK_MODE_KEY, false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        mAuth = FirebaseAuth.getInstance();
        fakebtnLogin = view.findViewById(R.id.fakebtnLogin);
        fakebtnSignup = view.findViewById(R.id.fakebtnSignup);
        fakebtnLogout = view.findViewById(R.id.fakebtnLogout);
        txtUserEmail = view.findViewById(R.id.txtUseremail);
        txtUserId = view.findViewById(R.id.txtUserId);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);
        fakebtnAi = view.findViewById(R.id.fakebtnAi);
        darkModeSwitch.setChecked(isDarkMode);

        fakebtnAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        // Set up dark mode switch listener
        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save preference
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DARK_MODE_KEY, isChecked);
                editor.apply();

                // Apply dark mode
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                // Restart activity to apply theme changes
                requireActivity().recreate();
            }
        });

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
            fakebtnLogin.setVisibility(View.GONE);
            fakebtnSignup.setVisibility(View.GONE);
        } else {
            // Người dùng chưa đăng nhập, ẩn Logout
            fakebtnLogout.setVisibility(View.GONE);
            txtUserId.setText("ID: ");
            txtUserEmail.setText("Email: ");
        }

        // Khởi tạo GoogleSignInClient để đăng xuất Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Dùng Web Client ID từ Firebase
                .requestEmail()
                .build();
        ggS = GoogleSignIn.getClient(requireActivity(), gso);

        fakebtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        fakebtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity((new Intent(getActivity(), SignUpActivity.class)));
            }
        });

        fakebtnLogout.setOnClickListener(new View.OnClickListener() {
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