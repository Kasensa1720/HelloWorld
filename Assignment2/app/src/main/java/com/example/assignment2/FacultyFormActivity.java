package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import database.DatabaseHelper;

public class FacultyFormActivity extends AppCompatActivity {

    private TextInputEditText editFacultyId, editFacultyName, editDeanName;
    private Button btnSaveFaculty;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private String originalFacultyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupForm();

        btnSaveFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFaculty();
            }
        });
    }

    private void initializeViews() {
        editFacultyId = findViewById(R.id.editFacultyId);
        editFacultyName = findViewById(R.id.editFacultyName);
        editDeanName = findViewById(R.id.editDeanName);
        btnSaveFaculty = findViewById(R.id.btnSaveFaculty);
    }

    private void setupForm() {
        Intent intent = getIntent();
        if (intent.hasExtra("FACULTY_ID")) {
            isEditMode = true;
            originalFacultyId = intent.getStringExtra("FACULTY_ID");
            editFacultyId.setText(originalFacultyId);
            editFacultyName.setText(intent.getStringExtra("FACULTY_NAME"));
            editDeanName.setText(intent.getStringExtra("DEAN_NAME"));

            editFacultyId.setEnabled(false);
            btnSaveFaculty.setText("Update Faculty");
        } else {
            isEditMode = false;
            btnSaveFaculty.setText("Save Faculty");
        }
    }

    private void saveFaculty() {
        String facultyId = editFacultyId.getText().toString().trim();
        String facultyName = editFacultyName.getText().toString().trim();
        String deanName = editDeanName.getText().toString().trim();

        if (facultyId.isEmpty() || facultyName.isEmpty() || deanName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success;
        if (isEditMode) {
            success = dbHelper.updateFaculty(facultyId, facultyName, deanName);
        } else {
            // Add the createdBy parameter - using admin user for demo
            long createdBy = dbHelper.getUserIdByEmail("admin@auca.ac.rw");
            success = dbHelper.addFaculty(facultyId, facultyName, deanName, createdBy);
        }

        if (success) {
            String message = isEditMode ? "Faculty updated successfully" : "Faculty added successfully";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String message = isEditMode ? "Error updating faculty" : "Error adding faculty";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}