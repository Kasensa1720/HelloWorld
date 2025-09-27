package com.example.assignment2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserDetailsActivity extends AppCompatActivity {
    TextView tvUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        tvUserInfo = findViewById(R.id.tvUserInfo);

        String username = getIntent().getStringExtra("username");
        String gender = getIntent().getStringExtra("gender");

        if (username != null) {
            String details = "Welcome, " + username;
            if (gender != null) {
                details += "\nGender: " + gender;
            }
            tvUserInfo.setText(details);
        }
    }
}
