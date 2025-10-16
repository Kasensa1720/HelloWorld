
package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_student_list);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listStudents);
        btnAdd = findViewById(R.id.btnAddStudent);
        facultyId = getIntent().getLongExtra("faculty_id", -1);

        btnAdd.setOnClickListener(v -> {
            Intent it = new Intent(StudentListActivity.this, StudentFormActivity.class);
            it.putExtra("faculty_id", facultyId);
            startActivity(it);
        });

        lv.setOnItemClickListener((parent, view, position, id) -> {
            long studentId = ids.get(position);
            Intent it = new Intent(StudentListActivity.this, StudentDetailActivity.class);
            it.putExtra("student_id", studentId);
            startActivity(it);
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
        Cursor c;
        if (facultyId != -1) {
            c = db.getStudentsByFaculty(facultyId);
        } else {
            c = db.getAllStudents();
        }
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("student_id"));
                String name = c.getString(c.getColumnIndexOrThrow("student_name"));
                ids.add(id);
                items.add(name);
            }
            c.close();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
    }
}
