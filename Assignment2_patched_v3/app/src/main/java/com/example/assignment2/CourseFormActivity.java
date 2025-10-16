
package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class CourseFormActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etName;
    Button btnSave;
    long courseId = -1;
    long facultyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_form);
        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etCourseName);
        btnSave = findViewById(R.id.btnSaveCourse);
        Intent i = getIntent();
        facultyId = i.getLongExtra("faculty_id", -1);
        courseId = i.getLongExtra("course_id", -1);

        if (courseId != -1) {
            Cursor c = db.getCoursesByFaculty(facultyId);
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("course_id"));
                if (id == courseId) {
                    etName.setText(c.getString(c.getColumnIndexOrThrow("course_name")));
                    break;
                }
            }
            c.close();
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (courseId == -1) {
                db.createCourse(name, facultyId);
            } else {
                db.updateCourse(courseId, name, facultyId);
            }
            finish();
        });
    }
}
