package com.example.studenttimer.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.studenttimer.R;

public class ThemeSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_settings);

        findViewById(R.id.btnSystem).setOnClickListener(v -> apply(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        findViewById(R.id.btnLight).setOnClickListener(v -> apply(AppCompatDelegate.MODE_NIGHT_NO));
        findViewById(R.id.btnDark).setOnClickListener(v -> apply(AppCompatDelegate.MODE_NIGHT_YES));
    }

    private void apply(int mode) {
        ThemeStore.saveMode(this, mode);
        AppCompatDelegate.setDefaultNightMode(mode);
        recreate();
    }
}