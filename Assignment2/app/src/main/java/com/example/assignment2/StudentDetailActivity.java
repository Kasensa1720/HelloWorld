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

public class StudentDetailActivity extends AppCompatActivity {

    private TextView textStudentName, textStudentId, textStudentEmail, textStudentPhone, textStudentGender, textFacultyName, textCreatedBy;
    private Button btnViewFaculty, btnEditStudent, btnDeleteStudent, btnViewEnrollments;
    private DatabaseHelper dbHelper;
    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getIntentData();
        loadStudentDetails();
        setupClickListeners();
    }

    private void initializeViews() {
        textStudentName = findViewById(R.id.textStudentName);
        textStudentId = findViewById(R.id.textStudentId);
        textStudentEmail = findViewById(R.id.textStudentEmail);
        textStudentPhone = findViewById(R.id.textStudentPhone);
        textStudentGender = findViewById(R.id.textStudentGender);
        textFacultyName = findViewById(R.id.textFacultyName);
        textCreatedBy = findViewById(R.id.textCreatedBy);
        btnViewFaculty = findViewById(R.id.btnViewFaculty);
        btnEditStudent = findViewById(R.id.btnEditStudent);
        btnDeleteStudent = findViewById(R.id.btnDeleteStudent);
        btnViewEnrollments = findViewById(R.id.btnViewEnrollments);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        studentId = intent.getStringExtra("STUDENT_ID");
    }

    private void loadStudentDetails() {
        Cursor cursor = dbHelper.getStudentById(studentId);
        if (cursor != null && cursor.moveToFirst()) {
            String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_EMAIL));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_PHONE));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_GENDER));
            String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
            String creatorName = cursor.getString(cursor.getColumnIndexOrThrow("creator_name"));

            textStudentName.setText(studentName);
            textStudentId.setText("ID: " + studentId);
            textStudentEmail.setText("Email: " + email);
            textStudentPhone.setText("Phone: " + phone);
            textStudentGender.setText("Gender: " + gender);
            textFacultyName.setText("Faculty: " + facultyName);
            textCreatedBy.setText("Created by: " + creatorName);

            cursor.close();
        }
    }

    private void setupClickListeners() {
        btnViewFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to faculty details
                Cursor cursor = dbHelper.getStudentById(studentId);
                if (cursor != null && cursor.moveToFirst()) {
                    String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
                    cursor.close();

                    // Get faculty ID from name (in real app, store faculty ID in student details)
                    long facultyDbId = dbHelper.getFacultyIdByName(facultyName);
                    Cursor facultyCursor = dbHelper.getAllFaculties();
                    String facultyId = "";
                    if (facultyCursor != null) {
                        while (facultyCursor.moveToNext()) {
                            if (facultyCursor.getLong(facultyCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)) == facultyDbId) {
                                facultyId = facultyCursor.getString(facultyCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_ID));
                                break;
                            }
                        }
                        facultyCursor.close();
                    }

                    Intent intent = new Intent(StudentDetailActivity.this, FacultyDetailActivity.class);
                    intent.putExtra("FACULTY_ID", facultyId);
                    intent.putExtra("FACULTY_NAME", facultyName);
                    startActivity(intent);
                }
            }
        });

        btnEditStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement edit functionality
                Toast.makeText(StudentDetailActivity.this, "Edit functionality to be implemented", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = dbHelper.deleteStudent(studentId);
                if (success) {
                    Toast.makeText(StudentDetailActivity.this, "Student deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(StudentDetailActivity.this, "Failed to delete student", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewEnrollments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getStudentById(studentId);
                if (cursor != null && cursor.moveToFirst()) {
                    long studentDbId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_NAME));
                    cursor.close();

                    Intent intent = new Intent(StudentDetailActivity.this, EnrollmentActivity.class);
                    intent.putExtra("STUDENT_ID", String.valueOf(studentDbId));
                    intent.putExtra("STUDENT_NAME", studentName);
                    startActivity(intent);
                }
            }
        });
    }
}