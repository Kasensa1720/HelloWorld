package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class CourseFormActivity extends AppCompatActivity {

    private TextInputEditText editCourseId, editCourseName, editCourseCode, editCredits;
    private MaterialAutoCompleteTextView autoCompleteFaculty;
    private Button btnSaveCourse;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private String originalCourseId;
    private List<FacultySpinnerItem> facultyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);

        dbHelper = new DatabaseHelper(this);
        facultyList = new ArrayList<>();

        initializeViews();
        setupFacultySpinner();
        setupForm();

        btnSaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
            }
        });
    }

    private void initializeViews() {
        editCourseId = findViewById(R.id.editCourseId);
        editCourseName = findViewById(R.id.editCourseName);
        editCourseCode = findViewById(R.id.editCourseCode);
        editCredits = findViewById(R.id.editCredits);
        autoCompleteFaculty = findViewById(R.id.autoCompleteFaculty);
        btnSaveCourse = findViewById(R.id.btnSaveCourse);
    }

    private void setupFacultySpinner() {
        loadFacultiesForSpinner();

        ArrayAdapter<FacultySpinnerItem> facultyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, facultyList);
        autoCompleteFaculty.setAdapter(facultyAdapter);
    }

    private void loadFacultiesForSpinner() {
        facultyList.clear();
        Cursor cursor = dbHelper.getFacultiesForSpinner();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long facultyDbId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
                facultyList.add(new FacultySpinnerItem(facultyDbId, facultyName));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void setupForm() {
        Intent intent = getIntent();

        // Pre-select faculty if coming from faculty view
        if (intent.hasExtra("FACULTY_ID")) {
            String facultyId = intent.getStringExtra("FACULTY_ID");
            Cursor facultyCursor = dbHelper.getFacultyById(facultyId);
            if (facultyCursor != null && facultyCursor.moveToFirst()) {
                String facultyName = facultyCursor.getString(facultyCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
                autoCompleteFaculty.setText(facultyName, false);
                facultyCursor.close();
            }
        }

        if (intent.hasExtra("COURSE_ID")) {
            isEditMode = true;
            originalCourseId = intent.getStringExtra("COURSE_ID");
            editCourseId.setText(originalCourseId);
            editCourseName.setText(intent.getStringExtra("COURSE_NAME"));
            editCourseCode.setText(intent.getStringExtra("COURSE_CODE"));
            editCredits.setText(String.valueOf(intent.getIntExtra("CREDITS", 0)));

            String facultyId = intent.getStringExtra("FACULTY_ID");
            Cursor facultyCursor = dbHelper.getFacultyById(facultyId);
            if (facultyCursor != null && facultyCursor.moveToFirst()) {
                String facultyName = facultyCursor.getString(facultyCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
                autoCompleteFaculty.setText(facultyName, false);
                facultyCursor.close();
            }

            editCourseId.setEnabled(false);
            btnSaveCourse.setText("Update Course");
        } else {
            isEditMode = false;
            btnSaveCourse.setText("Save Course");
        }
    }

    private void saveCourse() {
        String courseId = editCourseId.getText().toString().trim();
        String courseName = editCourseName.getText().toString().trim();
        String courseCode = editCourseCode.getText().toString().trim();
        String creditsStr = editCredits.getText().toString().trim();
        String facultyName = autoCompleteFaculty.getText().toString().trim();

        if (courseId.isEmpty() || courseName.isEmpty() || courseCode.isEmpty() ||
                creditsStr.isEmpty() || facultyName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid credits", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find faculty database ID from faculty name
        long facultyDbId = -1;
        for (FacultySpinnerItem faculty : facultyList) {
            if (faculty.getName().equals(facultyName)) {
                facultyDbId = faculty.getId();
                break;
            }
        }

        if (facultyDbId == -1) {
            Toast.makeText(this, "Invalid faculty selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // For demo purposes, using admin user. In real app, get from session
        long currentUserId = dbHelper.getUserIdByEmail("admin@auca.ac.rw");

        boolean success;
        if (isEditMode) {
            success = dbHelper.updateCourse(courseId, courseName, courseCode, credits, facultyDbId);
        } else {
            success = dbHelper.addCourse(courseId, courseName, courseCode, credits, facultyDbId, currentUserId);
        }

        if (success) {
            String message = isEditMode ? "Course updated successfully" : "Course added successfully";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String message = isEditMode ? "Error updating course" : "Error adding course";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    // Inner class for faculty spinner items
    private static class FacultySpinnerItem {
        private long id;
        private String name;

        public FacultySpinnerItem(long id, String name) {
            this.id = id;
            this.name = name;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}