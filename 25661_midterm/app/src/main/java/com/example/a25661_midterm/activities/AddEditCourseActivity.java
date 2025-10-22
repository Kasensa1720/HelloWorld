// activities/AddEditCourseActivity.java
package com.example.a25661_midterm.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
//import com.auca.midterm.R;
import dao.CourseDAO;
import com.example.a25661_midterm.R;

import model.Course;

public class AddEditCourseActivity extends AppCompatActivity {
    private EditText etCourseId, etTitle, etCode, etCredits;
    private Button btnSave;
    private CourseDAO courseDAO;
    private boolean isEditMode = false;
    private String existingCourseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        courseDAO = new CourseDAO(this);
        courseDAO.open();

        initializeViews();

        // Check if we're in edit mode
        if (getIntent().hasExtra("COURSE_ID")) {
            isEditMode = true;
            existingCourseId = getIntent().getStringExtra("COURSE_ID");
            loadCourseData();
        }

        btnSave.setOnClickListener(v -> saveCourse());
    }

    private void initializeViews() {
        etCourseId = findViewById(R.id.etCourseId);
        etTitle = findViewById(R.id.etTitle);
        etCode = findViewById(R.id.etCode);
        etCredits = findViewById(R.id.etCredits);
        btnSave = findViewById(R.id.btnSave);

        if (isEditMode) {
            setTitle("Edit Course");
            etCourseId.setEnabled(false); // Don't allow editing ID
        } else {
            setTitle("Add New Course");
        }
    }

    private void loadCourseData() {
        Course course = courseDAO.getCourseById(existingCourseId);
        if (course != null) {
            etCourseId.setText(course.getCourseId());
            etTitle.setText(course.getTitle());
            etCode.setText(course.getCode());
            etCredits.setText(String.valueOf(course.getCredits()));
        }
    }

    private void saveCourse() {
        String courseId = etCourseId.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        String creditsStr = etCredits.getText().toString().trim();

        // Validation
        if (courseId.isEmpty() || title.isEmpty() || code.isEmpty() || creditsStr.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Credits must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        Course course = new Course(courseId, title, code, credits);

        if (isEditMode) {
            // Update existing course
            int result = courseDAO.updateCourse(course);
            if (result > 0) {
                Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update course", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new course
            // Check if course ID already exists
            if (courseDAO.getCourseById(courseId) != null) {
                Toast.makeText(this, "Course ID already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = courseDAO.addCourse(course);
            if (result != -1) {
                Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        courseDAO.close();
        super.onDestroy();
    }
}