package com.example.tangthucac;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UserFragment extends Fragment {
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private GoogleSignInClient ggS;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = view.findViewById(R.id.btn_Logout);

        // Khởi tạo GoogleSignInClient để đăng xuất Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Dùng Web Client ID từ Firebase
                .requestEmail()
                .build();
        ggS = GoogleSignIn.getClient(requireActivity(), gso);

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
}
