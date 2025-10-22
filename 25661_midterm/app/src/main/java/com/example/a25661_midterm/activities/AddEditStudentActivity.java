// activities/AddEditStudentActivity.java
package com.example.a25661_midterm.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
//import com.auca.midterm.R;
import com.example.a25661_midterm.R;

import dao.StudentDAO;
import model.Student;

public class AddEditStudentActivity extends AppCompatActivity {
    private EditText etStudentId, etName, etEmail, etPhone;
    private RadioGroup rgGender;
    private Button btnSave;
    private StudentDAO studentDAO;
    private boolean isEditMode = false;
    private String existingStudentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_student);

        studentDAO = new StudentDAO(this);
        studentDAO.open();

        initializeViews();

        // Check if we're in edit mode
        if (getIntent().hasExtra("STUDENT_ID")) {
            isEditMode = true;
            existingStudentId = getIntent().getStringExtra("STUDENT_ID");
            loadStudentData();
        }

        btnSave.setOnClickListener(v -> saveStudent());
    }

    private void initializeViews() {
        etStudentId = findViewById(R.id.etStudentId);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        rgGender = findViewById(R.id.rgGender);
        btnSave = findViewById(R.id.btnSave);

        if (isEditMode) {
            setTitle("Edit Student");
            etStudentId.setEnabled(false); // Don't allow editing ID
        } else {
            setTitle("Add New Student");
        }
    }

    private void loadStudentData() {
        Student student = studentDAO.getStudentById(existingStudentId);
        if (student != null) {
            etStudentId.setText(student.getStudentId());
            etName.setText(student.getName());
            etEmail.setText(student.getEmail());
            etPhone.setText(student.getPhone());

            // Set gender radio button
            if (student.getGender() != null) {
                if (student.getGender().equals("Male")) {
                    rgGender.check(R.id.rbMale);
                } else if (student.getGender().equals("Female")) {
                    rgGender.check(R.id.rbFemale);
                }
            }
        }
    }

    private void saveStudent() {
        String studentId = etStudentId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Get selected gender
        String gender = "";
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId == R.id.rbMale) {
            gender = "Male";
        } else if (selectedId == R.id.rbFemale) {
            gender = "Female";
        }

        // Validation
        if (studentId.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Student ID and Name are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = new Student(studentId, name, gender, email, phone);

        if (isEditMode) {
            // Update existing student
            int result = studentDAO.updateStudent(student);
            if (result > 0) {
                Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new student
            // Check if student ID already exists
            if (studentDAO.getStudentById(studentId) != null) {
                Toast.makeText(this, "Student ID already exists", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = studentDAO.addStudent(student);
            if (result != -1) {
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        studentDAO.close();
        super.onDestroy();
    }
}