package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;l
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.AddStudentActivity;
import com.example.assignment2.StudentDetailActivity;

import database.DatabaseHelper;

public class StudentListActivity extends AppCompatActivity {

    private ListView listViewStudents;
    private Button btnAddStudent;
    private TextView textTitle;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private String facultyId, facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getIntentData();
        setupListView();
        setupClickListeners();
        loadStudents();
    }

    private void initializeViews() {
        listViewStudents = findViewById(R.id.listViewStudents);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        textTitle = findViewById(R.id.textTitle);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        facultyId = intent.getStringExtra("FACULTY_ID");
        facultyName = intent.getStringExtra("FACULTY_NAME");

        if (facultyName != null) {
            textTitle.setText("Students - " + facultyName);
        } else {
            textTitle.setText("All Students");
        }
    }

    private void setupListView() {
        String[] from = {DatabaseHelper.COLUMN_STUDENT_NAME, DatabaseHelper.COLUMN_STUDENT_EMAIL, DatabaseHelper.COLUMN_FACULTY_NAME};
        int[] to = {R.id.textStudentName, R.id.textStudentEmail, R.id.textFacultyName};

        // CHANGE THIS LINE - use list_item_student instead of activity_student_list
        adapter = new SimpleCursorAdapter(this, R.layout.student_list_item, null, from, to, 0);
        listViewStudents.setAdapter(adapter);

        listViewStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                String studentId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STUDENT_ID));

                Intent intent = new Intent(StudentListActivity.this, StudentDetailActivity.class);
                intent.putExtra("STUDENT_ID", studentId);
                startActivity(intent);
            }
        });
    }

    private void setupClickListeners() {
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                if (facultyId != null) {
                    intent.putExtra("FACULTY_ID", facultyId);
                }
                startActivity(intent);
            }
        });
    }

    private void loadStudents() {
        Cursor cursor;
        if (facultyId != null) {
            long facultyDbId = dbHelper.getFacultyIdByName(facultyName);
            cursor = dbHelper.getStudentsByFaculty(facultyDbId);
        } else {
            cursor = dbHelper.getAllStudents();
        }
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
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