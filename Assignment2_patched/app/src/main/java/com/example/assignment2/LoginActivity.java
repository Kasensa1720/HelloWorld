package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvSignUpLink;
    private DatabaseHelper databaseHelper;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            databaseHelper = new DatabaseHelper(this);
            initializeViews();
            setupClickListeners();
        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: " + e.getMessage());
            Toast.makeText(this, "App initialization failed", Toast.LENGTH_SHORT).show();
        }
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
            etUsername.setError("Username or email is required");
            etUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // Admin login
        if (username.equals("admin") && password.equals("password")) {
            Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, UserListActivity.class));
            finish();
            return;
        }

        // Database user authentication
        authenticateUser(username, password);
    }

    private void authenticateUser(String username, String password) {
        Cursor cursor = null;
        try {
            cursor = databaseHelper.getReadableDatabase().rawQuery(
                    "SELECT * FROM " + DatabaseHelper.TABLE_USERS +
                            " WHERE (" + DatabaseHelper.COLUMN_USERNAME + " = ? OR " +
                            DatabaseHelper.COLUMN_EMAIL + " = ?) AND " +
                            DatabaseHelper.COLUMN_PASSWORD + " = ?",
                    new String[]{username, username, password}
            );

            if (cursor != null && cursor.moveToFirst()) {
                handleSuccessfulLogin(cursor);
            } else {
                Toast.makeText(this, "Invalid username/email or password!", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Authentication error: " + e.getMessage(), e);
            Toast.makeText(this, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void handleSuccessfulLogin(Cursor cursor) {
        try {
            // Use safe column retrieval
            String userId = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_USER_ID);
            String fullName = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_USERNAME);
            String email = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_EMAIL);
            String phone = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_PHONE);
            String gender = getSafeColumnValue(cursor, DatabaseHelper.COLUMN_GENDER);

            Toast.makeText(this, "Welcome back, " + fullName + "!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, UserDetailsActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("fullName", fullName);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("gender", gender);
            intent.putExtra("newsletter", false);
            intent.putExtra("loginType", "registered");

            startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Error handling login: " + e.getMessage(), e);
            Toast.makeText(this, "Error processing user data", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSafeColumnValue(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            } else {
                Log.w(TAG, "Column not found: " + columnName);
                return "";
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting column " + columnName + ": " + e.getMessage());
            return "";
        }
    }

    private void clearErrors() {
        etUsername.setError(null);
        etPassword.setError(null);
    }

    private void navigateToSignUp() {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}