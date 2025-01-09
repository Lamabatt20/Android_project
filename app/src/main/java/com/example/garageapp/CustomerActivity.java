package com.example.garageapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.garageapp.CustomerFragments.CustomerHomeFragment;
import com.example.garageapp.CustomerFragments.CustomerNotificationFragment;
import com.example.garageapp.CustomerFragments.CustomerProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    CustomerHomeFragment customerHomeFragment = new CustomerHomeFragment();
    CustomerNotificationFragment customerNotificationFragment = new CustomerNotificationFragment();
    CustomerProfileFragment customerProfileFragment = new CustomerProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_nav3);
        getSupportFragmentManager().beginTransaction().replace(R.id.main,customerHomeFragment ).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId() == R.id.homes) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, customerHomeFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.notifications) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, customerNotificationFragment).commit();
                    return true;
                } else if (item.getItemId() == R.id.profiles) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, customerProfileFragment).commit();
                    return true;
                } else {
                    return false;
                }
            }
        });



    }
}