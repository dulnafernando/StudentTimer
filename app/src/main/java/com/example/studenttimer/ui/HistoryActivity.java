package com.example.studenttimer.ui;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studenttimer.R;
import com.example.studenttimer.auth.SessionManager;
import com.example.studenttimer.data.repo.SessionRepository;
import com.example.studenttimer.ui.adapter.SessionAdapter;

public class HistoryActivity extends AppCompatActivity {

    private SessionAdapter adapter;
    private SessionRepository repo;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        session = new SessionManager(this);
        repo = new SessionRepository(this);

        RecyclerView rv = findViewById(R.id.rvSessions);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Cursor c = repo.getSessionsForUser(session.getUserId());
        adapter = new SessionAdapter(this, c);
        rv.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // adapter closes cursor on swap, but not here. Safe to ignore for now.
    }
}