package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class StudentFormActivity extends AppCompatActivity {

    private TextInputEditText editStudentId, editStudentName, editEmail, editPhone;
    private AutoCompleteTextView autoCompleteGender, autoCompleteFaculty;
    private Button btnSaveStudent;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private String originalStudentId;
    private List<FacultySpinnerItem> facultyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);

        dbHelper = new DatabaseHelper(this);
        facultyList = new ArrayList<>();

        initializeViews();
        setupGenderSpinner();
        setupFacultySpinner();
        setupForm();

        btnSaveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudent();
            }
        });
    }

    private void initializeViews() {
        editStudentId = findViewById(R.id.editStudentId);
        editStudentName = findViewById(R.id.editStudentName);
        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        autoCompleteGender = findViewById(R.id.autoCompleteGender);
        autoCompleteFaculty = findViewById(R.id.autoCompleteFaculty);
        btnSaveStudent = findViewById(R.id.btnSaveStudent);
    }

    private void setupGenderSpinner() {
        String[] genderOptions = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genderOptions);
        autoCompleteGender.setAdapter(genderAdapter);
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
                String facultyId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_ID));
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));
                facultyList.add(new FacultySpinnerItem(facultyId, facultyName));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    private void setupForm() {
        Intent intent = getIntent();

        // Pre-select faculty if coming from faculty view
        if (intent.hasExtra("FACULTY_ID")) {
            String facultyId = intent.getStringExtra("FACULTY_ID");
            for (FacultySpinnerItem faculty : facultyList) {
                if (faculty.getId().equals(facultyId)) {
                    autoCompleteFaculty.setText(faculty.getName(), false);
                    break;
                }
            }
        }

        if (intent.hasExtra("STUDENT_ID")) {
            isEditMode = true;
            originalStudentId = intent.getStringExtra("STUDENT_ID");
            editStudentId.setText(originalStudentId);
            editStudentName.setText(intent.getStringExtra("STUDENT_NAME"));
            editEmail.setText(intent.getStringExtra("EMAIL"));
            editPhone.setText(intent.getStringExtra("PHONE"));
            autoCompleteGender.setText(intent.getStringExtra("GENDER"), false);

            String facultyId = intent.getStringExtra("FACULTY_ID");
            for (FacultySpinnerItem faculty : facultyList) {
                if (faculty.getId().equals(facultyId)) {
                    autoCompleteFaculty.setText(faculty.getName(), false);
                    break;
                }
            }

            editStudentId.setEnabled(false);
            btnSaveStudent.setText("Update Student");
        } else {
            isEditMode = false;
            btnSaveStudent.setText("Save Student");
        }
    }

    private void saveStudent() {
        String studentId = editStudentId.getText().toString().trim();
        String studentName = editStudentName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String gender = autoCompleteGender.getText().toString().trim();
        String facultyName = autoCompleteFaculty.getText().toString().trim();

        if (studentId.isEmpty() || studentName.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || gender.isEmpty() || facultyName.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Find faculty ID from faculty name
        String facultyId = null;
        for (FacultySpinnerItem faculty : facultyList) {
            if (faculty.getName().equals(facultyName)) {
                facultyId = faculty.getId();
                break;
            }
        }

        if (facultyId == null) {
            Toast.makeText(this, "Invalid faculty selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // For demo purposes, using user ID 1. In real app, get from session
        int currentUserId = 1;

        boolean success;
        if (isEditMode) {
            success = dbHelper.updateStudent(studentId, studentName, email, phone, gender, facultyId);
        } else {
            success = dbHelper.addStudent(studentId, studentName, email, phone, gender, facultyId, currentUserId);
        }

        if (success) {
            String message = isEditMode ? "Student updated successfully" : "Student added successfully";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            String message = isEditMode ? "Error updating student" : "Error adding student";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}

