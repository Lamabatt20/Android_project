package com.example.garageapp;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garageapp.Fragments.HomeFragment;
import com.example.garageapp.Fragments.NotficationFragment;
import com.example.garageapp.Fragments.ProfileFragment;
import com.example.garageapp.Fragments.ReportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Admin extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    NotficationFragment notficationFragment= new NotficationFragment();
    ProfileFragment profileFragment= new ProfileFragment();
    ReportFragment reportFragment= new ReportFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        int userId=getIntent().getIntExtra("user_id", -1);
        String role=getIntent().getStringExtra("role");
        Bundle bundle = new Bundle();
        bundle.putInt("user_id",userId);
        bundle.putString("role",role);
        profileFragment.setArguments(bundle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);

        getSupportFragmentManager().beginTransaction().replace(R.id.main, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.homes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, homeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.notifications) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, notficationFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profiles) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, profileFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.reports) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, reportFragment).commit();
                    return true;
                }
                else {
                    return false;
                }

            }
        });
    }
}