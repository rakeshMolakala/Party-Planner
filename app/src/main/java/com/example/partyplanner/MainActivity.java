package com.example.partyplanner;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.chat:
                    fragment = new ChatFragment();
                    break;
                case R.id.events:
                    fragment = new EventsFragment();
                    break;
                case R.id.ToDo:
                    fragment = new ToDoFragment();
                    break;
                case R.id.Account:
                    fragment = new AccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments, fragment).commit();
            return true;
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragments, new HomeFragment()).commit();
    }
}
