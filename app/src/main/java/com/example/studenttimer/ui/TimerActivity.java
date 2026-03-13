package com.example.studenttimer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studenttimer.R;
import com.example.studenttimer.auth.LoginActivity;
import com.example.studenttimer.auth.SessionManager;
import com.example.studenttimer.data.repo.SessionRepository;
import com.google.android.material.button.MaterialButton;
import com.example.studenttimer.ui.HistoryActivity;
import java.util.Locale;
import com.example.studenttimer.ui.ThemeSettingsActivity;
public class TimerActivity extends AppCompatActivity {

    private static final int DEFAULT_FOCUS_MIN = 25;
    private static final int DEFAULT_BREAK_MIN = 5;

    private TextView tvTime;
    private ProgressBar progressRing;
    private MaterialButton btnStartPause, btnStop, btnLogout;

    private CountDownTimer timer;
    private boolean isRunning = false;

    private long totalMillis;
    private long remainingMillis;

    private SessionManager session;
    private SessionRepository sessionRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        MaterialButton btnTheme = findViewById(R.id.btnTheme);
        btnTheme.setOnClickListener(v ->
                startActivity(new Intent(this, ThemeSettingsActivity.class))
        );
        session = new SessionManager(this);
        sessionRepo = new SessionRepository(this);

        tvTime = findViewById(R.id.tvTime);
        progressRing = findViewById(R.id.progressRing);

        btnStartPause = findViewById(R.id.btnStartPause);
        btnStop = findViewById(R.id.btnStop);
        btnLogout = findViewById(R.id.btnLogout);

        totalMillis = DEFAULT_FOCUS_MIN * 60_000L;
        remainingMillis = totalMillis;

        progressRing.setMax((int) (totalMillis / 1000));
        progressRing.setProgress((int) (remainingMillis / 1000));
        tvTime.setText(formatTime(remainingMillis));

        btnStartPause.setOnClickListener(v -> {
            if (!isRunning) startTimer();
            else pauseTimer();
        });

        btnStop.setOnClickListener(v -> stopAndSave());

        btnLogout.setOnClickListener(v -> {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        MaterialButton btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );
    }

    private void startTimer() {
        isRunning = true;
        btnStartPause.setText("PAUSE");

        timer = new CountDownTimer(remainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMillis = millisUntilFinished;
                tvTime.setText(formatTime(remainingMillis));
                progressRing.setProgress((int) (remainingMillis / 1000));
            }

            @Override
            public void onFinish() {
                isRunning = false;
                remainingMillis = 0;
                tvTime.setText("00:00");
                progressRing.setProgress(0);
                btnStartPause.setText("START");
                toast("Session completed!");
                saveSession((int) (totalMillis / 1000)); // full focus time
                resetTimer();
            }
        }.start();
    }

    private void pauseTimer() {
        if (timer != null) timer.cancel();
        isRunning = false;
        btnStartPause.setText("START");
    }

    private void stopAndSave() {
        int studiedSec = (int) ((totalMillis - remainingMillis) / 1000);
        if (studiedSec <= 0) {
            toast("Nothing to save yet");
            return;
        }

        if (timer != null) timer.cancel();
        isRunning = false;
        btnStartPause.setText("START");

        saveSession(studiedSec);
        resetTimer();
        toast("Saved to history");
    }

    private void saveSession(int studiedSec) {
        long userId = session.getUserId();

        // For now we save a basic title/subject.
        // Later we will add subject picker + title input.
        String title = "Study Session";
        String subject = "General";

        long id = sessionRepo.insertSession(
                userId,
                title,
                subject,
                DEFAULT_FOCUS_MIN,
                DEFAULT_BREAK_MIN,
                studiedSec,
                null
        );

        if (id == -1) toast("Save failed");
    }

    private void resetTimer() {
        remainingMillis = totalMillis;
        tvTime.setText(formatTime(remainingMillis));
        progressRing.setMax((int) (totalMillis / 1000));
        progressRing.setProgress((int) (remainingMillis / 1000));
    }

    private String formatTime(long millis) {
        long totalSec = millis / 1000;
        long min = totalSec / 60;
        long sec = totalSec % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", min, sec);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}