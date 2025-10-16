package com.example.assignment2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnSignUp;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Your existing login layout

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        initializeViews();
        setupListeners();

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            navigateToFacultyList();
        }
    }

    private void initializeViews() {
        editEmail = findViewById(R.id.editEmail); // Your existing email field
        editPassword = findViewById(R.id.editPassword); // Your existing password field
        btnLogin = findViewById(R.id.btnLogin); // Your existing login button
        btnSignUp = findViewById(R.id.btnSignUp); // Your existing signup button
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValidUser = dbHelper.checkUser(email, password);
        if (isValidUser) {
            // Get user details and save to shared preferences
            Cursor cursor = dbHelper.getUserByEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USERNAME));

                // Save user session
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_ID", userId);
                editor.putString("USERNAME", username);
                editor.putString("EMAIL", email);
                editor.putBoolean("IS_LOGGED_IN", true);
                editor.apply();

                cursor.close();

                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                navigateToFacultyList();
            }
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false);
    }

    private void navigateToFacultyList() {
        Intent intent = new Intent(LoginActivity.this, FacultyListActivity.class);
        startActivity(intent);
        finish(); // Close login activity so user can't go back
    }
}