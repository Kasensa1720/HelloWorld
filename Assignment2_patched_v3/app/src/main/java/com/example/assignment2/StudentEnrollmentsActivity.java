
package com.example.assignment2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

import java.util.ArrayList;

public class StudentEnrollmentsActivity extends AppCompatActivity {
    DatabaseHelper db;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<Long> courseIds = new ArrayList<>();
    long studentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_enrollments);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listEnrollments);
        studentId = getIntent().getLongExtra("student_id", -1);

        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            long courseId = courseIds.get(position);
            new AlertDialog.Builder(StudentEnrollmentsActivity.this)
                    .setTitle("Remove enrollment?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean ok = db.deleteEnrollment(studentId, courseId);
                        if (ok) {
                            Toast.makeText(StudentEnrollmentsActivity.this, "Enrollment removed", Toast.LENGTH_SHORT).show();
                            loadEnrollments();
                        } else {
                            Toast.makeText(StudentEnrollmentsActivity.this, "Failed to remove enrollment", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null).show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEnrollments();
    }

    private void loadEnrollments() {
        items.clear();
        courseIds.clear();
        Cursor c = db.getCoursesForStudent(studentId);
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("course_id"));
                String name = c.getString(c.getColumnIndexOrThrow("course_name"));
                courseIds.add(id);
                items.add(name);
            }
            c.close();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
    }
}
