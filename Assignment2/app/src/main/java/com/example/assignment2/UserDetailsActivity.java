package com.example.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserInfo, tvUserId, tvEmail, tvPhone, tvGender, tvNewsletter, tvPassword;
    private Button btnLogout, btnViewUserList;
    private String emailAddress, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

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
    }

    private void displayUserDetails() {
        Intent intent = getIntent();

        String userId = intent.getStringExtra("userId");
        String fullName = intent.getStringExtra("fullName");
        emailAddress = intent.getStringExtra("email");
        phoneNumber = intent.getStringExtra("phone");
        String gender = intent.getStringExtra("gender");
        boolean newsletter = intent.getBooleanExtra("newsletter", false);
        String password = intent.getStringExtra("password");
        String loginType = intent.getStringExtra("loginType");

        tvWelcome.setText("Welcome, " + fullName + "!");
        tvUserInfo.setText("signup".equals(loginType) ? "Account created!" : "User Details");

        if (userId != null && !userId.isEmpty()) {
            tvUserId.setText("User ID: " + userId);
            tvUserId.setVisibility(View.VISIBLE);
        } else {
            tvUserId.setVisibility(View.GONE);
        }

        tvEmail.setText("📧 Email: " + emailAddress);
        tvPhone.setText("📱 Phone: " + phoneNumber);
        tvGender.setText("Gender: " + gender);
        tvNewsletter.setText("Newsletter: " + (newsletter ? "Subscribed" : "Not subscribed"));
        tvPassword.setText("Password: " + password);

        tvEmail.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvPhone.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvEmail.setPaintFlags(tvEmail.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        tvPhone.setPaintFlags(tvPhone.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setupClickListeners() {
        tvEmail.setOnClickListener(v -> {
            if (emailAddress != null && !emailAddress.isEmpty()) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + emailAddress));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello!");
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

        tvPhone.setOnClickListener(v -> {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
            }
        });

        btnViewUserList.setOnClickListener(v ->
                startActivity(new Intent(this, UserListActivity.class)));

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
