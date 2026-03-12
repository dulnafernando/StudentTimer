package com.example.studenttimer.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studenttimer.data.db.DatabaseHelper;

public class UserRepository {

    private final DatabaseHelper dbHelper;

    public UserRepository(Context ctx) {
        dbHelper = new DatabaseHelper(ctx);
    }

    public long createUser(String username, String passwordHash) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.U_USERNAME, username.trim());
        cv.put(DatabaseHelper.U_PASS_HASH, passwordHash);
        cv.put(DatabaseHelper.U_CREATED_AT, System.currentTimeMillis());
        return db.insert(DatabaseHelper.T_USERS, null, cv);
    }

    public Cursor getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                DatabaseHelper.T_USERS,
                new String[]{DatabaseHelper.U_ID, DatabaseHelper.U_USERNAME, DatabaseHelper.U_PASS_HASH},
                DatabaseHelper.U_USERNAME + "=?",
                new String[]{username.trim()},
                null, null, null
        );
    }
}