
package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class StudentDetailActivity extends AppCompatActivity {
    DatabaseHelper db;
    TextView tvName, tvEmail, tvPhone, tvFaculty;
    Button btnEnroll, btnViewEnrollments;
    long studentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        db = new DatabaseHelper(this);
        tvName = findViewById(R.id.tvStudentName);
        tvEmail = findViewById(R.id.tvStudentEmail);
        tvPhone = findViewById(R.id.tvStudentPhone);
        tvFaculty = findViewById(R.id.tvStudentFaculty);
        btnEnroll = findViewById(R.id.btnEnrollCourse);
        btnViewEnrollments = findViewById(R.id.btnViewEnrollments);

        studentId = getIntent().getLongExtra("student_id", -1);

        loadDetails();

        btnEnroll.setOnClickListener(v -> {
            // open Course list for student to pick from (show all courses of student's faculty)
            Intent it = new Intent(StudentDetailActivity.this, CourseListActivity.class);
            // pass student id too
            it.putExtra("student_id", studentId);
            startActivity(it);
        });

        btnViewEnrollments.setOnClickListener(v -> {
            Intent it = new Intent(StudentDetailActivity.this, StudentEnrollmentsActivity.class);
            it.putExtra("student_id", studentId);
            startActivity(it);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDetails();
    }

    private void loadDetails() {
        if (studentId == -1) return;
        Cursor c = db.getStudentWithFaculty(studentId);
        if (c != null && c.moveToFirst()) {
            tvName.setText(c.getString(c.getColumnIndexOrThrow("student_name")));
            tvEmail.setText(c.getString(c.getColumnIndexOrThrow("student_email")));
            tvPhone.setText(c.getString(c.getColumnIndexOrThrow("student_phone")));
            String facultyName = c.getString(c.getColumnIndexOrThrow("faculty_name"));
            tvFaculty.setText(facultyName==null? "N/A":facultyName);
            c.close();
        }
    }
}
