package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class FacultyDetailActivity extends AppCompatActivity {

    private TextView textFacultyName, textFacultyId, textDeanName, textCreatedBy;
    private Button btnViewStudents, btnViewCourses, btnEditFaculty, btnDeleteFaculty;
    private DatabaseHelper dbHelper;
    private String facultyId, facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_detail);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getIntentData();
        loadFacultyDetails();
        setupClickListeners();
    }

    private void initializeViews() {
        textFacultyName = findViewById(R.id.textFacultyName);
        textFacultyId = findViewById(R.id.textFacultyId);
        textDeanName = findViewById(R.id.textDeanName);
        textCreatedBy = findViewById(R.id.textCreatedBy);
        btnViewStudents = findViewById(R.id.btnViewStudents);
        btnViewCourses = findViewById(R.id.btnViewCourses);
        btnEditFaculty = findViewById(R.id.btnEditFaculty);
        btnDeleteFaculty = findViewById(R.id.btnDeleteFaculty);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        facultyId = intent.getStringExtra("FACULTY_ID");
        facultyName = intent.getStringExtra("FACULTY_NAME");
    }

    private void loadFacultyDetails() {
        Cursor cursor = dbHelper.getFacultyById(facultyId);
        if (cursor != null && cursor.moveToFirst()) {
            String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
            String deanName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEAN_NAME));
            String creatorName = cursor.getString(cursor.getColumnIndexOrThrow("creator_name"));

            textFacultyName.setText(facultyName);
            textFacultyId.setText("ID: " + facultyId);
            textDeanName.setText("Dean: " + deanName);
            textCreatedBy.setText("Created by: " + creatorName);

            cursor.close();
        }
    }

    private void setupClickListeners() {
        btnViewStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDetailActivity.this, StudentListActivity.class);
                intent.putExtra("FACULTY_ID", facultyId);
                intent.putExtra("FACULTY_NAME", facultyName);
                startActivity(intent);
            }
        });

        btnViewCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacultyDetailActivity.this, CourseListActivity.class);
                intent.putExtra("FACULTY_ID", facultyId);
                intent.putExtra("FACULTY_NAME", facultyName);
                startActivity(intent);
            }
        });

        btnEditFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement edit functionality
                Toast.makeText(FacultyDetailActivity.this, "Edit functionality to be implemented", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = dbHelper.deleteFaculty(facultyId);
                if (success) {
                    Toast.makeText(FacultyDetailActivity.this, "Faculty deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FacultyDetailActivity.this, "Failed to delete faculty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}