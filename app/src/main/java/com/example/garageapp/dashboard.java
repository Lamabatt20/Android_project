package com.example.garageapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.garageapp.Fragments.HomeEmployee;
import com.example.garageapp.Fragments.HomeFragment;
import com.example.garageapp.Fragments.NotficationFragment;
import com.example.garageapp.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    NotficationFragment notficationFragment= new NotficationFragment();
    ProfileFragment profileFragment= new ProfileFragment();
    HomeEmployee homeEmployee = new HomeEmployee();
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        int userId=getIntent().getIntExtra("user_id", -1);
        String role=getIntent().getStringExtra("role");

        Bundle bundle = new Bundle();
        bundle.putInt("user_id",userId);
        bundle.putInt("user_id",userId);
        bundle.putString("role",role);
        profileFragment.setArguments(bundle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_nav1);

        getSupportFragmentManager().beginTransaction().replace(R.id.main, homeEmployee).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, homeEmployee).commit();
                    return true;
                } else if (item.getItemId() == R.id.notification) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, notficationFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, profileFragment).commit();
                    return true;
                } else {
                    return false;
                }

            }
        });
    }
}