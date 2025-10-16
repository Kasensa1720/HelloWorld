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

public class AddStudentActivity extends AppCompatActivity {

    private EditText editStudentId, editStudentName, editStudentEmail, editStudentPhone;
    private Spinner spinnerFaculty, spinnerGender;
    private Button btnSaveStudent;
    private DatabaseHelper dbHelper;
    private List<Long> facultyIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupFacultySpinner();
        setupGenderSpinner();
        setupClickListeners();
    }

    private void initializeViews() {
        editStudentId = findViewById(R.id.editStudentId);
        editStudentName = findViewById(R.id.editStudentName);
        editStudentEmail = findViewById(R.id.editStudentEmail);
        editStudentPhone = findViewById(R.id.editStudentPhone);
        spinnerFaculty = findViewById(R.id.spinnerFaculty);
        spinnerGender = findViewById(R.id.spinnerGender);
        btnSaveStudent = findViewById(R.id.btnSaveStudent);
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

    private void setupGenderSpinner() {
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnSaveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudent();
            }
        });
    }

    private void saveStudent() {
        String studentId = editStudentId.getText().toString().trim();
        String studentName = editStudentName.getText().toString().trim();
        String email = editStudentEmail.getText().toString().trim();
        String phone = editStudentPhone.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        if (studentId.isEmpty() || studentName.isEmpty() || facultyIds.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String facultyId = String.valueOf(facultyIds.get(spinnerFaculty.getSelectedItemPosition()));
        long createdBy = dbHelper.getUserIdByEmail("admin@auca.ac.rw");

        boolean success = dbHelper.addStudent(studentId, studentName, email, phone, gender, facultyId, createdBy);

        if (success) {
            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
        }
    }
}