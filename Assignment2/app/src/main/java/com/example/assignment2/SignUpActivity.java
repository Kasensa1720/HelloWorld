package com.example.assignment2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword, etPhone;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private CheckBox cbTerms, cbNewsletter;
    private Button btnSignUp, btnBackToLogin;
    private SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userPrefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etPhone = findViewById(R.id.et_phone);
        rgGender = findViewById(R.id.rg_gender);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        cbTerms = findViewById(R.id.cb_terms);
        cbNewsletter = findViewById(R.id.cb_newsletter);
        btnSignUp = findViewById(R.id.btn_signup);
        btnBackToLogin = findViewById(R.id.btn_back_to_login);
    }

    private void setupClickListeners() {
        btnSignUp.setOnClickListener(v -> performSignUp());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void performSignUp() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        clearErrors();

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
        if (TextUtils.isEmpty(password) || password.length() < 6 || !isPasswordStrong(password)) {
            etPassword.setError("Strong password (6+ chars, upper/lower/digit) required");
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
        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Accept terms", Toast.LENGTH_SHORT).show();
            return;
        }

        String gender = rbMale.isChecked() ? "Male" : "Female";
        boolean newsletter = cbNewsletter.isChecked();

        // Multi-user save
        int nextId = userPrefs.getInt("next_user_id", 1);
        SharedPreferences.Editor editor = userPrefs.edit();
        String idKey = "user_" + nextId + "_";
        editor.putString(idKey + "name", fullName);
        editor.putString(idKey + "email", email);
        editor.putString(idKey + "password", password);
        editor.putString(idKey + "phone", phone);
        editor.putString(idKey + "gender", gender);
        editor.putBoolean(idKey + "newsletter", newsletter);
        editor.putInt("next_user_id", nextId + 1);
        editor.apply();

        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();

        // Explicit Intent: All details
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra("userId", "USER" + String.format("%03d", nextId));
        intent.putExtra("fullName", fullName);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("gender", gender);
        intent.putExtra("newsletter", newsletter);
        intent.putExtra("password", password);
        intent.putExtra("loginType", "signup");
        startActivity(intent);
        finish();
    }

    private void clearErrors() {
        etFullName.setError(null);
        etEmail.setError(null);
        etPhone.setError(null);
        etPassword.setError(null);
        etConfirmPassword.setError(null);
    }

    private boolean isPasswordStrong(String password) {
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }
        return hasUpper && hasLower && hasDigit;
    }
}

//commit made to sign up 1