package com.example.assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUpLink;
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUpLink = findViewById(R.id.tv_signup_link);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(v -> performLogin());
        tvSignUpLink.setOnClickListener(v -> navigateToSignUp());
    }

    private void performLogin() {
        clearErrors();

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Admin login → go to user list
        if (username.equals("admin") && password.equals("password")) {
            Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, UserListActivity.class));
            finish();
            return;
        }

        // Multi-user check
        int nextId = userPrefs.getInt("next_user_id", 1);
        boolean found = false;
        for (int i = 1; i < nextId; i++) {
            String savedName = userPrefs.getString("user_" + i + "_name", "");
            String savedEmail = userPrefs.getString("user_" + i + "_email", "");
            String savedPass = userPrefs.getString("user_" + i + "_password", "");
            if ((username.equalsIgnoreCase(savedName) || username.equalsIgnoreCase(savedEmail)) && password.equals(savedPass)) {
                found = true;
                Intent intent = new Intent(this, UserDetailsActivity.class);
                intent.putExtra("userId", "USER" + String.format("%03d", i));
                intent.putExtra("fullName", savedName);
                intent.putExtra("email", savedEmail);
                intent.putExtra("phone", userPrefs.getString("user_" + i + "_phone", ""));
                intent.putExtra("gender", userPrefs.getString("user_" + i + "_gender", ""));
                intent.putExtra("newsletter", userPrefs.getBoolean("user_" + i + "_newsletter", false));
                intent.putExtra("password", savedPass);
                intent.putExtra("loginType", "registered");
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
                break;
            }
        }

        if (!found) {
            Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_LONG).show();
        }
    }

    private void clearErrors() {
        etUsername.setError(null);
        etPassword.setError(null);
    }

    private void navigateToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
