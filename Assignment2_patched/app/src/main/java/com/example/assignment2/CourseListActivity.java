
package com.example.assignment2;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

import java.util.ArrayList;

public class CourseListActivity extends AppCompatActivity {
    DatabaseHelper db;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<Long> ids = new ArrayList<>();
    ArrayList<String> items = new ArrayList<>();
    Button btnAdd;
    long facultyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listCourses);
        btnAdd = findViewById(R.id.btnAddCourse);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            long courseId = ids.get(position);
            long studentIdFromIntent = getIntent().getLongExtra("student_id", -1);
            if (studentIdFromIntent != -1) {
                // enroll student in this course
                long res = db.enrollStudentInCourse(studentIdFromIntent, courseId);
                if (res == -1) {
                    Toast.makeText(CourseListActivity.this, "Student already enrolled in this course", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CourseListActivity.this, "Enrolled successfully", Toast.LENGTH_SHORT).show();
                }
            } else {
                // no student context - do nothing or future view
                Toast.makeText(CourseListActivity.this, "Long-press to edit/delete course", Toast.LENGTH_SHORT).show();
            }
        });

        facultyId = getIntent().getLongExtra("faculty_id", -1);

        btnAdd.setOnClickListener(v -> {
            Intent it = new Intent(CourseListActivity.this, CourseFormActivity.class);
            it.putExtra("faculty_id", facultyId);
            startActivity(it);
        });

        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            long courseId = ids.get(position);
            new AlertDialog.Builder(CourseListActivity.this)
                    .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                        if (which == 0) {
                            Intent it = new Intent(CourseListActivity.this, CourseFormActivity.class);
                            it.putExtra("course_id", courseId);
                            it.putExtra("faculty_id", facultyId);
                            startActivity(it);
                        } else if (which == 1) {
                            db.deleteCourse(courseId);
                            loadData();
                        }
                    }).show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        ids.clear();
        items.clear();
        if (facultyId != -1) {
            Cursor c = db.getCoursesByFaculty(facultyId);
            if (c != null) {
                while (c.moveToNext()) {
                    long id = c.getLong(c.getColumnIndexOrThrow("course_id"));
                    String name = c.getString(c.getColumnIndexOrThrow("course_name"));
                    ids.add(id);
                    items.add(name);
                }
                c.close();
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
    }
}
