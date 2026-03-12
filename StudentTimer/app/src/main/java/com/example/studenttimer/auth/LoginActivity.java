package com.example.studenttimer.auth;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studenttimer.R;
import com.example.studenttimer.data.db.DatabaseHelper;
import com.example.studenttimer.data.repo.UserRepository;
import com.example.studenttimer.ui.TimerActivity;
import com.example.studenttimer.utils.PasswordHasher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.studenttimer.ui.ThemeStore;
import com.example.studenttimer.ui.TimerActivity;
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private UserRepository userRepo;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Apply saved theme BEFORE activity is created
        AppCompatDelegate.setDefaultNightMode(ThemeStore.loadMode(this));

        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if (session.isLoggedIn()) {
            startActivity(new Intent(this, TimerActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        userRepo = new UserRepository(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvSignup = findViewById(R.id.tvSignup);

        btnLogin.setOnClickListener(v -> doLogin());
        tvSignup.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    private void doLogin() {
        String username = etUsername.getText() == null ? "" : etUsername.getText().toString().trim();
        String password = etPassword.getText() == null ? "" : etPassword.getText().toString();

        if (username.isEmpty()) {
            toast("Enter email or student ID");
            return;
        }
        if (password.length() < 8) {
            toast("Password must be at least 8 characters");
            return;
        }

        try (Cursor c = userRepo.getUserByUsername(username)) {
            if (c != null && c.moveToFirst()) {
                long userId = c.getLong(c.getColumnIndexOrThrow(DatabaseHelper.U_ID));
                String storedHash = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.U_PASS_HASH));

                if (PasswordHasher.verify(password.toCharArray(), storedHash)) {
                    session.saveUser(userId, username);
                    startActivity(new Intent(this, TimerActivity.class));
                    finish();
                } else {
                    toast("Incorrect password");
                }
            } else {
                toast("User not found");
            }
        } catch (Exception e) {
            toast("Login error");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}