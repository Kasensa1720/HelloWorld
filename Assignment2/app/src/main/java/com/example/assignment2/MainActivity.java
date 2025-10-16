package com.example.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnManageFaculties, btnManageStudents, btnManageCourses, btnManageEnrollments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnManageFaculties = findViewById(R.id.btnManageFaculties);
        btnManageStudents = findViewById(R.id.btnManageStudents);
        btnManageCourses = findViewById(R.id.btnManageCourses);
        btnManageEnrollments = findViewById(R.id.btnManageEnrollments);
    }

    private void setupClickListeners() {
        btnManageFaculties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FacultyListActivity.class));
            }
        });

        btnManageStudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.assignment2.StudentListActivity.class));
            }
        });

        btnManageCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CourseListActivity.class));
            }
        });

        btnManageEnrollments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EnrollmentActivity.class));
            }
        });
    }
}