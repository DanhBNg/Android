package com.example.tangthucac;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;
    private boolean isUserAction = true;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Xử lý vuốt ViewPager2
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (isUserAction) {
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                }
            }
        });

        // Xử lý BottomNavigationView
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            isUserAction = false;
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    viewPager.setCurrentItem(0);
//                    break;
//                case R.id.nav_library:
//                    viewPager.setCurrentItem(1);
//                    break;
//                case R.id.nav_genres:
//                    viewPager.setCurrentItem(2);
//                    break;
//                case R.id.nav_user:
//                    viewPager.setCurrentItem(3);
//                    break;
//            }
//            isUserAction = true;
//            return true;
//        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            isUserAction = false;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                viewPager.setCurrentItem(0);
            } else if (itemId == R.id.nav_library) {
                viewPager.setCurrentItem(1);
            } else if (itemId == R.id.nav_genres) {
                viewPager.setCurrentItem(2);
            } else if (itemId == R.id.nav_user) {
                viewPager.setCurrentItem(3);
            }
            isUserAction = true;
            return true;
        });
    }
}
