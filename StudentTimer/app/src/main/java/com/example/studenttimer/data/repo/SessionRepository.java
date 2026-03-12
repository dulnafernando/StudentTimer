package com.example.studenttimer.data.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.studenttimer.data.db.DatabaseHelper;

public class SessionRepository {

    private final DatabaseHelper dbHelper;

    public SessionRepository(Context ctx) {
        dbHelper = new DatabaseHelper(ctx);
    }

    // CREATE
    public long insertSession(long userId, String title, String subject, int focusMin, int breakMin, int totalSec, Integer mood) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.S_USER_ID, userId);
        cv.put(DatabaseHelper.S_TITLE, title);
        cv.put(DatabaseHelper.S_SUBJECT, subject);
        cv.put(DatabaseHelper.S_FOCUS_MIN, focusMin);
        cv.put(DatabaseHelper.S_BREAK_MIN, breakMin);
        cv.put(DatabaseHelper.S_TOTAL_SEC, totalSec);
        if (mood != null) cv.put(DatabaseHelper.S_MOOD, mood);
        cv.put(DatabaseHelper.S_COMPLETED_AT, System.currentTimeMillis());
        return db.insert(DatabaseHelper.T_SESSIONS, null, cv);
    }

    // READ (user-only)
    public Cursor getSessionsForUser(long userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                DatabaseHelper.T_SESSIONS,
                null,
                DatabaseHelper.S_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null,
                DatabaseHelper.S_COMPLETED_AT + " DESC"
        );
    }

    // UPDATE (edit)
    public int updateSession(long sessionId, long userId, String title, String subject, Integer mood) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.S_TITLE, title);
        cv.put(DatabaseHelper.S_SUBJECT, subject);
        if (mood != null) cv.put(DatabaseHelper.S_MOOD, mood);

        return db.update(
                DatabaseHelper.T_SESSIONS,
                cv,
                DatabaseHelper.S_ID + "=? AND " + DatabaseHelper.S_USER_ID + "=?",
                new String[]{String.valueOf(sessionId), String.valueOf(userId)}
        );
    }

    // DELETE
    public int deleteSession(long sessionId, long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(
                DatabaseHelper.T_SESSIONS,
                DatabaseHelper.S_ID + "=? AND " + DatabaseHelper.S_USER_ID + "=?",
                new String[]{String.valueOf(sessionId), String.valueOf(userId)}
        );
    }
}