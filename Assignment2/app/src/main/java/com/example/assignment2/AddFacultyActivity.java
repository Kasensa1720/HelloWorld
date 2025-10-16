package com.example.assignment2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class AddFacultyActivity extends AppCompatActivity {

    private EditText editFacultyId, editFacultyName, editDeanName;
    private Button btnSaveFaculty;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_faculty);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        editFacultyId = findViewById(R.id.editFacultyId);
        editFacultyName = findViewById(R.id.editFacultyName);
        editDeanName = findViewById(R.id.editDeanName);
        btnSaveFaculty = findViewById(R.id.btnSaveFaculty);
    }

    private void setupClickListeners() {
        btnSaveFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFaculty();
            }
        });
    }

    private void saveFaculty() {
        String facultyId = editFacultyId.getText().toString().trim();
        String facultyName = editFacultyName.getText().toString().trim();
        String deanName = editDeanName.getText().toString().trim();

        if (facultyId.isEmpty() || facultyName.isEmpty() || deanName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // For demo, using admin user ID (in real app, get from login session)
        long createdBy = dbHelper.getUserIdByEmail("admin@auca.ac.rw");

        boolean success = dbHelper.addFaculty(facultyId, facultyName, deanName, createdBy);

        if (success) {
            Toast.makeText(this, "Faculty added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add faculty", Toast.LENGTH_SHORT).show();
        }
    }
}