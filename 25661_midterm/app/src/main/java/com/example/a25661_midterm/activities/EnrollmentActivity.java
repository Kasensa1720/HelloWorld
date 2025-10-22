// activities/EnrollmentActivity.java
package com.example.a25661_midterm.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
//import com.auca.midterm.R;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import com.example.a25661_midterm.R;

import dao.StudentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentActivity extends AppCompatActivity {
    private Spinner spinnerStudents, spinnerCourses;
    private Button btnEnroll;
    private ListView listViewEnrollments;

    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    private List<Student> studentList;
    private List<Course> courseList;
    private List<Enrollment> enrollmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        initializeDAOs();
        initializeViews();
        loadSpinners();
        loadEnrollments();

        btnEnroll.setOnClickListener(v -> enrollStudent());
    }

    private void initializeDAOs() {
        studentDAO = new StudentDAO(this);
        courseDAO = new CourseDAO(this);
        enrollmentDAO = new EnrollmentDAO(this);

        studentDAO.open();
        courseDAO.open();
        enrollmentDAO.open();
    }

    private void initializeViews() {
        spinnerStudents = findViewById(R.id.spinnerStudents);
        spinnerCourses = findViewById(R.id.spinnerCourses);
        btnEnroll = findViewById(R.id.btnEnroll);
        listViewEnrollments = findViewById(R.id.listViewEnrollments);
    }

    private void loadSpinners() {
        // Load students
        studentList = studentDAO.getAllStudents();
        List<String> studentNames = new ArrayList<>();
        for (Student student : studentList) {
            studentNames.add(student.getName() + " (" + student.getStudentId() + ")");
        }

        ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, studentNames);
        studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudents.setAdapter(studentAdapter);

        // Load courses
        courseList = courseDAO.getAllCourses();
        List<String> courseTitles = new ArrayList<>();
        for (Course course : courseList) {
            courseTitles.add(course.getTitle() + " (" + course.getCode() + ")");
        }

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, courseTitles);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourses.setAdapter(courseAdapter);
    }

    private void loadEnrollments() {
        enrollmentList = enrollmentDAO.getAllEnrollments();
        List<String> enrollmentStrings = new ArrayList<>();

        for (Enrollment enrollment : enrollmentList) {
            enrollmentStrings.add(enrollment.getStudentName() + " → " +
                    enrollment.getCourseTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, enrollmentStrings);
        listViewEnrollments.setAdapter(adapter);

        // Set long click to unenroll
        listViewEnrollments.setOnItemLongClickListener((parent, view, position, id) -> {
            Enrollment enrollment = enrollmentList.get(position);
            showUnenrollDialog(enrollment);
            return true;
        });
    }

    private void enrollStudent() {
        int studentPosition = spinnerStudents.getSelectedItemPosition();
        int coursePosition = spinnerCourses.getSelectedItemPosition();

        if (studentPosition == -1 || coursePosition == -1) {
            Toast.makeText(this, "Please select both student and course", Toast.LENGTH_SHORT).show();
            return;
        }

        Student selectedStudent = studentList.get(studentPosition);
        Course selectedCourse = courseList.get(coursePosition);

        long result = enrollmentDAO.enrollStudent(
                selectedStudent.getStudentId(),
                selectedCourse.getCourseId()
        );

        if (result != -1) {
            Toast.makeText(this, "Student enrolled successfully", Toast.LENGTH_SHORT).show();
            loadEnrollments();
        } else {
            Toast.makeText(this, "Student is already enrolled in this course", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUnenrollDialog(Enrollment enrollment) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Unenroll Student")
                .setMessage("Are you sure you want to unenroll " + enrollment.getStudentName() +
                        " from " + enrollment.getCourseTitle() + "?")
                .setPositiveButton("Unenroll", (dialog, which) -> {
                    enrollmentDAO.unenrollStudent(enrollment.getEnrollmentId());
                    loadEnrollments();
                    Toast.makeText(this, "Student unenrolled", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        studentDAO.close();
        courseDAO.close();
        enrollmentDAO.close();
        super.onDestroy();
    }
}