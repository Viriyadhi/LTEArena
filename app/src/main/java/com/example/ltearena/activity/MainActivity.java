package com.example.ltearena.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.ltearena.R;
import com.example.ltearena.fragments.HomeFragment;
import com.example.ltearena.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the fragment exists, show it.
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("home")).commit();
                        } else {
                            //if the fragment does not exist, add it to fragment manager.
                            HomeFragment homeFragment = new HomeFragment();
                            fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "home").commit();
                        }
                        if (fragmentManager.findFragmentByTag("profile") != null) {
                            //if the other fragment is visible, profile it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("profile")).commit();
                        }
                        break;
                    case R.id.profile_nav:
                        if (fragmentManager.findFragmentByTag("profile") != null) {
                            //if the fragment exists, show it.
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("profile")).commit();
                        } else {
                            //if the fragment does not exist, add it to fragment manager.
                            ProfileFragment profileFragment = new ProfileFragment();
                            fragmentManager.beginTransaction().add(R.id.fragment_container, profileFragment, "profile").commit();
                        }
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                        }
                        break;
                }
                return true;
            }
        });
    }
}