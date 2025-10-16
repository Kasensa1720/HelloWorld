package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnrollmentActivity extends AppCompatActivity {

    private TextView textTitle;
    private Spinner spinnerStudents, spinnerCourses;
    private Button btnEnroll, btnViewEnrollments;
    private ListView listViewEnrollments;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private String studentId, courseId, studentName, courseName;
    private List<Long> studentIds, courseIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getIntentData();
        setupSpinners();
        setupListView();
        setupClickListeners();
        loadEnrollments();
    }

    private void initializeViews() {
        textTitle = findViewById(R.id.textTitle);
        spinnerStudents = findViewById(R.id.spinnerStudents);
        spinnerCourses = findViewById(R.id.spinnerCourses);
        btnEnroll = findViewById(R.id.btnEnroll);
        btnViewEnrollments = findViewById(R.id.btnViewEnrollments);
        listViewEnrollments = findViewById(R.id.listViewEnrollments);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        studentId = intent.getStringExtra("STUDENT_ID");
        courseId = intent.getStringExtra("COURSE_ID");
        studentName = intent.getStringExtra("STUDENT_NAME");
        courseName = intent.getStringExtra("COURSE_NAME");

        if (studentName != null) {
            textTitle.setText("Enrollments - " + studentName);
        } else if (courseName != null) {
            textTitle.setText("Enrollments - " + courseName);
        } else {
            textTitle.setText("Manage Enrollments");
        }
    }

    private void setupSpinners() {
        setupStudentSpinner();
        setupCourseSpinner();
    }

    private void setupStudentSpinner() {
        Cursor cursor = dbHelper.getAllStudents();
        List<String> studentNames = new ArrayList<>();
        studentIds = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                studentIds.add(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_NAME));
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_ID));
                studentNames.add(studentName + " (" + studentId + ")");
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudents.setAdapter(adapter);

        // Pre-select student if provided in intent
        if (studentId != null) {
            for (int i = 0; i < studentIds.size(); i++) {
                if (studentIds.get(i) == Long.parseLong(studentId)) {
                    spinnerStudents.setSelection(i);
                    spinnerStudents.setEnabled(false); // Disable spinner when specific student is selected
                    break;
                }
            }
        }
    }

    private void setupCourseSpinner() {
        Cursor cursor = dbHelper.getAllCourses();
        List<String> courseNames = new ArrayList<>();
        courseIds = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                courseIds.add(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                String courseName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_NAME));
                String courseCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_CODE));
                courseNames.add(courseName + " (" + courseCode + ")");
            }
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(adapter);

        // Pre-select course if provided in intent
        if (courseId != null) {
            for (int i = 0; i < courseIds.size(); i++) {
                if (courseIds.get(i) == Long.parseLong(courseId)) {
                    spinnerCourses.setSelection(i);
                    spinnerCourses.setEnabled(false); // Disable spinner when specific course is selected
                    break;
                }
            }
        }
    }

    private void setupListView() {
        String[] from = {"course_name", "course_code", "faculty_name"};
        int[] to = {R.id.textCourseName, R.id.textCourseCode, R.id.textFacultyName};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_enrollment, null, from, to, 0);
        listViewEnrollments.setAdapter(adapter);

        listViewEnrollments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                String enrollmentId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ENROLLMENT_ID));

                // Show option to unenroll
                boolean success = dbHelper.unenrollStudent(enrollmentId);
                if (success) {
                    Toast.makeText(EnrollmentActivity.this, "Student unenrolled successfully", Toast.LENGTH_SHORT).show();
                    loadEnrollments();
                } else {
                    Toast.makeText(EnrollmentActivity.this, "Failed to unenroll student", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupClickListeners() {
        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollStudent();
            }
        });

        btnViewEnrollments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEnrollments();
            }
        });
    }

    private void enrollStudent() {
        int studentPosition = spinnerStudents.getSelectedItemPosition();
        int coursePosition = spinnerCourses.getSelectedItemPosition();

        if (studentPosition == -1 || coursePosition == -1) {
            Toast.makeText(this, "Please select both student and course", Toast.LENGTH_SHORT).show();
            return;
        }

        long selectedStudentId = studentIds.get(studentPosition);
        long selectedCourseId = courseIds.get(coursePosition);

        // Check if already enrolled
        if (dbHelper.isStudentEnrolled(selectedStudentId, selectedCourseId)) {
            Toast.makeText(this, "Student is already enrolled in this course", Toast.LENGTH_SHORT).show();
            return;
        }

        String enrollmentId = "ENR_" + UUID.randomUUID().toString().substring(0, 8);
        long createdBy = dbHelper.getUserIdByEmail("admin@auca.ac.rw");

        boolean success = dbHelper.enrollStudent(enrollmentId, selectedStudentId, selectedCourseId, createdBy);

        if (success) {
            Toast.makeText(this, "Student enrolled successfully", Toast.LENGTH_SHORT).show();
            loadEnrollments();
        } else {
            Toast.makeText(this, "Failed to enroll student", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadEnrollments() {
        Cursor cursor;
        if (studentId != null) {
            // Show enrollments for specific student
            cursor = dbHelper.getStudentEnrollments(Long.parseLong(studentId));
        } else if (courseId != null) {
            // Show enrollments for specific course
            cursor = dbHelper.getCourseEnrollments(Long.parseLong(courseId));
        } else {
            // Show all enrollments (you might want to implement this method)
            cursor = dbHelper.getStudentEnrollments(0); // Show none for now
            Toast.makeText(this, "Please select a student or course to view enrollments", Toast.LENGTH_SHORT).show();
        }

        adapter.changeCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEnrollments();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter.getCursor() != null) {
            adapter.getCursor().close();
        }
        dbHelper.close();
    }
}