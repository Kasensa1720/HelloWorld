
package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class FacultyFormActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText etName, etDean;
    Button btnSave;
    long facultyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_form);
        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etFacultyName);
        etDean = findViewById(R.id.etDeanName);
        btnSave = findViewById(R.id.btnSaveFaculty);

        Intent i = getIntent();
        if (i.hasExtra("faculty_id")) {
            facultyId = i.getLongExtra("faculty_id", -1);
            Cursor c = db.getAllFaculties();
            // find faculty record
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("faculty_id"));
                if (id == facultyId) {
                    etName.setText(c.getString(c.getColumnIndexOrThrow("faculty_name")));
                    etDean.setText(c.getString(c.getColumnIndexOrThrow("dean_name")));
                    break;
                }
            }
            c.close();
        }

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String dean = etDean.getText().toString().trim();
            if (facultyId == -1) {
                db.createFaculty(name, dean, null);
            } else {
                db.updateFaculty(facultyId, name, dean);
            }
            finish();
        });
    }
}
