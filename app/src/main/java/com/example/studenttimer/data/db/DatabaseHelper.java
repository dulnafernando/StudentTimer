package com.example.studenttimer.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "student_timer.db";
    public static final int DB_VERSION = 1;

    // Users table
    public static final String T_USERS = "users";
    public static final String U_ID = "id";
    public static final String U_USERNAME = "username";
    public static final String U_PASS_HASH = "password_hash";
    public static final String U_CREATED_AT = "created_at";

    // Sessions table
    public static final String T_SESSIONS = "sessions";
    public static final String S_ID = "id";
    public static final String S_USER_ID = "user_id";
    public static final String S_TITLE = "title";
    public static final String S_SUBJECT = "subject";
    public static final String S_FOCUS_MIN = "focus_min";
    public static final String S_BREAK_MIN = "break_min";
    public static final String S_TOTAL_SEC = "total_sec";
    public static final String S_MOOD = "mood";
    public static final String S_COMPLETED_AT = "completed_at";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers =
                "CREATE TABLE " + T_USERS + " (" +
                        U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        U_USERNAME + " TEXT NOT NULL UNIQUE, " +
                        U_PASS_HASH + " TEXT NOT NULL, " +
                        U_CREATED_AT + " INTEGER NOT NULL" +
                        ");";

        String createSessions =
                "CREATE TABLE " + T_SESSIONS + " (" +
                        S_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        S_USER_ID + " INTEGER NOT NULL, " +
                        S_TITLE + " TEXT, " +
                        S_SUBJECT + " TEXT, " +
                        S_FOCUS_MIN + " INTEGER NOT NULL, " +
                        S_BREAK_MIN + " INTEGER NOT NULL, " +
                        S_TOTAL_SEC + " INTEGER NOT NULL, " +
                        S_MOOD + " INTEGER, " +
                        S_COMPLETED_AT + " INTEGER NOT NULL, " +
                        "FOREIGN KEY(" + S_USER_ID + ") REFERENCES " + T_USERS + "(" + U_ID + ") ON DELETE CASCADE" +
                        ");";

        db.execSQL(createUsers);
        db.execSQL(createSessions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        onCreate(db);
    }
}