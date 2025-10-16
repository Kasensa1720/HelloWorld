package com.example.assignment2;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {

    private EditText editCourseId, editCourseName, editCourseCode, editCredits;
    private Spinner spinnerFaculty;
    private Button btnSaveCourse;
    private DatabaseHelper dbHelper;
    private List<Long> facultyIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupFacultySpinner();
        setupClickListeners();
    }

    private void initializeViews() {
        editCourseId = findViewById(R.id.editCourseId);
        editCourseName = findViewById(R.id.editCourseName);
        editCourseCode = findViewById(R.id.editCourseCode);
        editCredits = findViewById(R.id.editCredits);
        spinnerFaculty = findViewById(R.id.spinnerFaculty);
        btnSaveCourse = findViewById(R.id.btnSaveCourse);
    }

    private void setupFacultySpinner() {
        Cursor cursor = dbHelper.getFacultiesForSpinner();
        List<String> facultyNames = new ArrayList<>();
        facultyIds = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                facultyIds.add(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                facultyNames.add(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME)));
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facultyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaculty.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSaveCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCourse();
            }
        });
    }

    private void saveCourse() {
        String courseId = editCourseId.getText().toString().trim();
        String courseName = editCourseName.getText().toString().trim();
        String courseCode = editCourseCode.getText().toString().trim();
        String creditsStr = editCredits.getText().toString().trim();

        if (courseId.isEmpty() || courseName.isEmpty() || courseCode.isEmpty() || creditsStr.isEmpty() || facultyIds.isEmpty()) {
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

        long facultyId = facultyIds.get(spinnerFaculty.getSelectedItemPosition());
        long createdBy = dbHelper.getUserIdByEmail("admin@auca.ac.rw");

        boolean success = dbHelper.addCourse(courseId, courseName, courseCode, credits, facultyId, createdBy);

        if (success) {
            Toast.makeText(this, "Course added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT).show();
        }
    }
}