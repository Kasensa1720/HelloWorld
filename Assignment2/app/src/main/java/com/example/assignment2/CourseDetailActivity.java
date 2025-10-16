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

public class CourseDetailActivity extends AppCompatActivity {

    private TextView textCourseName, textCourseId, textCourseCode, textCredits, textFacultyName, textCreatedBy;
    private Button btnViewFaculty, btnEditCourse, btnDeleteCourse, btnViewEnrollments;
    private DatabaseHelper dbHelper;
    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getIntentData();
        loadCourseDetails();
        setupClickListeners();
    }

    private void initializeViews() {
        textCourseName = findViewById(R.id.textCourseName);
        textCourseId = findViewById(R.id.textCourseId);
        textCourseCode = findViewById(R.id.textCourseCode);
        textCredits = findViewById(R.id.textCredits);
        textFacultyName = findViewById(R.id.textFacultyName);
        textCreatedBy = findViewById(R.id.textCreatedBy);
        btnViewFaculty = findViewById(R.id.btnViewFaculty);
        btnEditCourse = findViewById(R.id.btnEditCourse);
        btnDeleteCourse = findViewById(R.id.btnDeleteCourse);
        btnViewEnrollments = findViewById(R.id.btnViewEnrollments);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        courseId = intent.getStringExtra("COURSE_ID");
    }

    private void loadCourseDetails() {
        Cursor cursor = dbHelper.getCourseById(courseId);
        if (cursor != null && cursor.moveToFirst()) {
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_NAME));
            String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_CODE));
            int credits = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREDITS));
            String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
            String creatorName = cursor.getString(cursor.getColumnIndexOrThrow("creator_name"));

            textCourseName.setText(courseName);
            textCourseId.setText("ID: " + courseId);
            textCourseCode.setText("Code: " + courseCode);
            textCredits.setText("Credits: " + credits);
            textFacultyName.setText("Faculty: " + facultyName);
            textCreatedBy.setText("Created by: " + creatorName);

            cursor.close();
        }
    }

    private void setupClickListeners() {
        btnViewFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getCourseById(courseId);
                if (cursor != null && cursor.moveToFirst()) {
                    String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
                    cursor.close();

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

                    Intent intent = new Intent(CourseDetailActivity.this, FacultyDetailActivity.class);
                    intent.putExtra("FACULTY_ID", facultyId);
                    intent.putExtra("FACULTY_NAME", facultyName);
                    startActivity(intent);
                }
            }
        });

        btnEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CourseDetailActivity.this, "Edit functionality to be implemented", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = dbHelper.deleteCourse(courseId);
                if (success) {
                    Toast.makeText(CourseDetailActivity.this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CourseDetailActivity.this, "Failed to delete course", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnViewEnrollments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = dbHelper.getCourseById(courseId);
                if (cursor != null && cursor.moveToFirst()) {
                    long courseDbId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                    String courseName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_NAME));
                    cursor.close();

                    Intent intent = new Intent(CourseDetailActivity.this, EnrollmentActivity.class);
                    intent.putExtra("COURSE_ID", String.valueOf(courseDbId));
                    intent.putExtra("COURSE_NAME", courseName);
                    startActivity(intent);
                }
            }
        });
    }
}