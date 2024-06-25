package com.example.contactapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.contactapp.databinding.ActivityMainBinding;

public class Activity_Main extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        // Ánh xạ đối tượng binding với layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Thay thế fragment mặc định bằng Fragment_Home
        replaceFragment(new Fragment_Home());

        // Xử lý sự kiện khi chọn item trên BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.d("ItemSelected", "Item selected: " + item.getItemId());
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new Fragment_Home());
                    break;
                case R.id.fav:
                    replaceFragment(new Fragment_Favorite());
                    break;
                case R.id.recents:
                    replaceFragment(new Fragment_Recent());
                    break;
                case R.id.pad:
                    replaceFragment(new Fragment_Keypad());
                    break;
            }
            return true;
        });
    }

    // Phương thức để thay thế fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.coordinatorLayout, fragment);
        fragmentTransaction.commit();
    }
}
