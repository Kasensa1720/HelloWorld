package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import database.DatabaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword, etPhone;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Button btnUpdate, btnCancel;
    private DatabaseHelper databaseHelper;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        databaseHelper = new DatabaseHelper(this);
        initializeViews();
        loadUserData();
        setupClickListeners();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.et_edit_full_name);
        etEmail = findViewById(R.id.et_edit_email);
        etPassword = findViewById(R.id.et_edit_password);
        etConfirmPassword = findViewById(R.id.et_edit_confirm_password);
        etPhone = findViewById(R.id.et_edit_phone);
        rgGender = findViewById(R.id.rg_edit_gender);
        rbMale = findViewById(R.id.rb_edit_male);
        rbFemale = findViewById(R.id.rb_edit_female);
        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void loadUserData() {
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("userId");

        etFullName.setText(intent.getStringExtra("fullName"));
        etEmail.setText(intent.getStringExtra("email"));
        etPhone.setText(intent.getStringExtra("phone"));
        etPassword.setText(intent.getStringExtra("password"));
        etConfirmPassword.setText(intent.getStringExtra("password"));

        String gender = intent.getStringExtra("gender");
        if ("Male".equals(gender)) {
            rbMale.setChecked(true);
        } else if ("Female".equals(gender)) {
            rbFemale.setChecked(true);
        }
    }

    private void setupClickListeners() {
        btnUpdate.setOnClickListener(v -> performUpdate());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void performUpdate() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(fullName) || fullName.length() < 2) {
            etFullName.setError("Full name must be at least 2 characters");
            etFullName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Valid email required");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            etPhone.setError("Valid phone (10+ digits) required");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = rbMale.isChecked() ? "Male" : "Female";

        // Update user in database
        boolean isUpdated = databaseHelper.updateUser(currentUserId, fullName, password, gender, email, phone);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

            // Return to UserDetailsActivity with updated data
            Intent resultIntent = new Intent();
            resultIntent.putExtra("userId", currentUserId);
            resultIntent.putExtra("fullName", fullName);
            resultIntent.putExtra("email", email);
            resultIntent.putExtra("phone", phone);
            resultIntent.putExtra("gender", gender);
            resultIntent.putExtra("password", password);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}