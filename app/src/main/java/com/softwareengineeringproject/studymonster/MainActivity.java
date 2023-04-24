package com.softwareengineeringproject.studymonster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private final String FRAGMENT_TAG = "main_activity_fragment_container";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChipNavigationBar navbar = findViewById(R.id.navbar);
        navbar.setItemSelected(R.id.navbar_home_icon, true);

        navbar.setOnItemSelectedListener(item -> {
            OnNavigationItemSelected(item);
        });
    }

    public void OnNavigationItemSelected(int item) {
        Fragment fragment = null;

        switch (item)
        {
            case R.id.navbar_home_icon:
                fragment = new DashboardFragment();
                break;

            case R.id.navbar_settings_icon:
                fragment = new SettingsFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_activity_fragment_container, fragment, FRAGMENT_TAG)
                .commit();
    }
}