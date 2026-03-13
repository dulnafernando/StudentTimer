package com.example.studenttimer.ui;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeStore {
    private static final String PREF = "student_timer_prefs";
    private static final String KEY_THEME = "theme_mode";

    public static void saveMode(Context c, int mode) {
        SharedPreferences sp = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_THEME, mode).apply();
    }

    public static int loadMode(Context c) {
        SharedPreferences sp = c.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}