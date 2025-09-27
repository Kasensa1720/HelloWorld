package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {
    EditText etNewUsername, etNewPassword;
    RadioGroup rgGender;
    CheckBox cbAgree;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNewUsername = findViewById(R.id.etNewUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        rgGender = findViewById(R.id.rgGender);
        cbAgree = findViewById(R.id.cbAgree);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUser = etNewUsername.getText().toString();
                String newPass = etNewPassword.getText().toString();
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                RadioButton rbGender = selectedGenderId != -1 ? findViewById(selectedGenderId) : null;

                if (newUser.isEmpty() || newPass.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                } else if (!cbAgree.isChecked()) {
                    Toast.makeText(SignupActivity.this, "You must agree to terms", Toast.LENGTH_SHORT).show();
                } else {
                    String gender = rbGender != null ? rbGender.getText().toString() : "Not specified";
                    Toast.makeText(SignupActivity.this, "Sign-Up successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignupActivity.this, UserDetailsActivity.class);
                    intent.putExtra("username", newUser);
                    intent.putExtra("gender", gender);
                    startActivity(intent);
                }
            }
        });
    }
}
