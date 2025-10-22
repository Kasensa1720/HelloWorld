package com.example.a25661_midterm.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.a25661_midterm.R;
import dao.StudentDAO;
import model.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementActivity extends AppCompatActivity {
    private StudentDAO studentDAO;
    private List<Student> studentList;
    private ListView listViewStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        studentDAO = new StudentDAO(this);
        studentDAO.open();

        listViewStudents = findViewById(R.id.listViewStudents);
        Button btnAddStudent = findViewById(R.id.btnAddStudent); // Converted to local variable

        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(StudentManagementActivity.this,
                    AddEditStudentActivity.class);
            startActivity(intent);
        });

        loadStudents();
    }

    private void loadStudents() {
        studentList = studentDAO.getAllStudents();

        // Create a simple array of student display strings
        List<String> studentDisplay = new ArrayList<>();
        for (Student student : studentList) {
            String display = student.getName() + " - " + student.getStudentId();
            studentDisplay.add(display);
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, studentDisplay);
        listViewStudents.setAdapter(adapter);

        // Set click listener for editing students
        listViewStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student student = studentList.get(position);
            Intent intent = new Intent(StudentManagementActivity.this,
                    AddEditStudentActivity.class);
            intent.putExtra("STUDENT_ID", student.getStudentId());
            startActivity(intent);
        });

        // Set long click listener for deleting students
        listViewStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            Student student = studentList.get(position);
            showDeleteDialog(student);
            return true;
        });
    }

    private void showDeleteDialog(Student student) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete " + student.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    studentDAO.deleteStudent(student.getStudentId());
                    loadStudents(); // Refresh the list
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    @Override
    protected void onDestroy() {
        studentDAO.close();
        super.onDestroy();
    }
}