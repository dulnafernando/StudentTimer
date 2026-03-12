package com.example.studenttimer.auth;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studenttimer.R;
import com.example.studenttimer.data.repo.UserRepository;
import com.example.studenttimer.utils.PasswordHasher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private UserRepository userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepo = new UserRepository(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        MaterialButton btnCreate = findViewById(R.id.btnCreate);
        TextView tvBack = findViewById(R.id.tvBack);

        tvBack.setOnClickListener(v -> finish());
        btnCreate.setOnClickListener(v -> doRegister());
    }

    private void doRegister() {
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

        // Check if user exists
        try (Cursor c = userRepo.getUserByUsername(username)) {
            if (c != null && c.moveToFirst()) {
                toast("User already exists");
                return;
            }
        }

        String hash = PasswordHasher.hash(password.toCharArray());
        long id = userRepo.createUser(username, hash);

        if (id == -1) {
            toast("Registration failed");
        } else {
            toast("Account created. Please login.");
            finish(); // back to login
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}