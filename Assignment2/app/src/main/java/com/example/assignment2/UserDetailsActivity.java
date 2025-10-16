package com.example.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import database.DatabaseHelper;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserInfo, tvUserId, tvEmail, tvPhone, tvGender, tvNewsletter, tvPassword;
    private Button btnLogout, btnViewUserList, btnEditProfile, btnDeleteAccount;
    private String emailAddress, phoneNumber, currentUserId;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        databaseHelper = new DatabaseHelper(this);
        initializeViews();
        displayUserDetails();
        setupClickListeners();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tv_welcome);
        tvUserInfo = findViewById(R.id.tv_user_info);
        tvUserId = findViewById(R.id.tv_user_id);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvGender = findViewById(R.id.tv_gender);
        tvNewsletter = findViewById(R.id.tv_newsletter);
        tvPassword = findViewById(R.id.tv_password);
        btnLogout = findViewById(R.id.btn_logout);
        btnViewUserList = findViewById(R.id.btn_view_user_list);

        // Add new buttons - make sure to add these to your layout XML
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnDeleteAccount = findViewById(R.id.btn_delete_account);
    }

    private void displayUserDetails() {
        Intent intent = getIntent();

        currentUserId = intent.getStringExtra("userId");
        String fullName = intent.getStringExtra("fullName");
        emailAddress = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phone");
        String gender = intent.getStringExtra("gender");
        boolean newsletter = intent.getBooleanExtra("newsletter", false);
        String password = intent.getStringExtra("password");
        String loginType = intent.getStringExtra("loginType");

        tvWelcome.setText("Welcome, " + fullName + "!");
        tvUserInfo.setText("signup".equals(loginType) ? "Account created!" : "User Details");

        if (currentUserId != null && !currentUserId.isEmpty()) {
            tvUserId.setText("User ID: " + currentUserId);
            tvUserId.setVisibility(View.VISIBLE);
        } else {
            tvUserId.setVisibility(View.GONE);
        }

        tvEmail.setText("📧 Email: " + emailAddress);
        tvPhone.setText("📱 Phone: " + phoneNumber);
        tvGender.setText("Gender: " + gender);
        tvNewsletter.setText("Newsletter: " + (newsletter ? "Subscribed" : "Not subscribed"));
        tvPassword.setText("Password: " + password);

        // Make email and phone clickable
        tvEmail.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvPhone.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvEmail.setPaintFlags(tvEmail.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        tvPhone.setPaintFlags(tvPhone.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setupClickListeners() {
        // Email click
        tvEmail.setOnClickListener(v -> {
            if (emailAddress != null && !emailAddress.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddress));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello!");
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

        // Phone click
        tvPhone.setOnClickListener(v -> {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
            }
        });

        // View user list
        btnViewUserList.setOnClickListener(v ->
                startActivity(new Intent(this, UserListActivity.class)));

        // Edit profile
        btnEditProfile.setOnClickListener(v -> navigateToEditProfile());

        // Delete account
        btnDeleteAccount.setOnClickListener(v -> deleteCurrentUser());

        // Logout
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void navigateToEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("userId", currentUserId);
        intent.putExtra("fullName", getIntent().getStringExtra("fullName"));
        intent.putExtra("email", emailAddress);
        intent.putExtra("phone", phoneNumber);
        intent.putExtra("gender", getIntent().getStringExtra("gender"));
        intent.putExtra("password", getIntent().getStringExtra("password"));
        startActivity(intent);
    }

    private void deleteCurrentUser() {
        if (currentUserId != null) {
            boolean isDeleted = databaseHelper.deleteUser(currentUserId);
            if (isDeleted) {
                Toast.makeText(this, "Account deleted successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to delete account!", Toast.LENGTH_SHORT).show();
            }
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