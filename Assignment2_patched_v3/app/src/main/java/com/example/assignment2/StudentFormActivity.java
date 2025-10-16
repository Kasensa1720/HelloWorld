
package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

import java.util.ArrayList;

public class StudentFormActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etName, etEmail, etPhone;
    Spinner spinnerFaculty;
    Button btnSave;
    long studentId = -1;
    long facultyIdFromIntent = -1;

    ArrayList<Long> facultyIds = new ArrayList<>();
    ArrayList<String> facultyNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);
        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etStudentName);
        etEmail = findViewById(R.id.etStudentEmail);
        etPhone = findViewById(R.id.etStudentPhone);
        spinnerFaculty = findViewById(R.id.spinnerFaculty);
        btnSave = findViewById(R.id.btnSaveStudent);

        facultyIdFromIntent = getIntent().getLongExtra("faculty_id", -1);

        loadFaculties();

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            int pos = spinnerFaculty.getSelectedItemPosition();
            Long facId = null;
            if (pos >= 0 && pos < facultyIds.size()) facId = facultyIds.get(pos);
            db.createStudent(name, email, phone, facId);
            finish();
        });
    }

    private void loadFaculties() {
        facultyIds.clear();
        facultyNames.clear();
        Cursor c = db.getAllFaculties();
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("faculty_id"));
                String name = c.getString(c.getColumnIndexOrThrow("faculty_name"));
                facultyIds.add(id);
                facultyNames.add(name);
            }
            c.close();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, facultyNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaculty.setAdapter(adapter);

        // if opened from a faculty page, pre-select it
        if (facultyIdFromIntent != -1) {
            int idx = facultyIds.indexOf(facultyIdFromIntent);
            if (idx >= 0) spinnerFaculty.setSelection(idx);
        }
    }
}
