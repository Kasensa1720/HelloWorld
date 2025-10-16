package com.example.assignment2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class CourseListActivity extends AppCompatActivity {

    private ListView listViewCourses;
    private Button btnAddCourse;
    private TextView textTitle;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private String facultyId, facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        getIntentData();
        setupListView();
        setupClickListeners();
        loadCourses();
    }

    private void initializeViews() {
        listViewCourses = findViewById(R.id.listViewCourses);
        btnAddCourse = findViewById(R.id.btnAddCourse);
        textTitle = findViewById(R.id.textTitle);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        facultyId = intent.getStringExtra("FACULTY_ID");
        facultyName = intent.getStringExtra("FACULTY_NAME");

        if (facultyName != null) {
            textTitle.setText("Courses - " + facultyName);
        } else {
            textTitle.setText("All Courses");
        }
    }

    private void setupListView() {
        String[] from = {DatabaseHelper.COLUMN_COURSE_NAME, DatabaseHelper.COLUMN_COURSE_CODE, DatabaseHelper.COLUMN_CREDITS, DatabaseHelper.COLUMN_FACULTY_NAME};
        int[] to = {R.id.textCourseName, R.id.textCourseCode, R.id.textCredits, R.id.textFacultyName};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_course, null, from, to, 0);
        listViewCourses.setAdapter(adapter);

        listViewCourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                String courseId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COURSE_ID));

                Intent intent = new Intent(CourseListActivity.this, CourseDetailActivity.class);
                intent.putExtra("COURSE_ID", courseId);
                startActivity(intent);
            }
        });
    }

    private void setupClickListeners() {
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this, AddCourseActivity.class);
                if (facultyId != null) {
                    intent.putExtra("FACULTY_ID", facultyId);
                }
                startActivity(intent);
            }
        });
    }

    private void loadCourses() {
        Cursor cursor;
        if (facultyId != null) {
            long facultyDbId = dbHelper.getFacultyIdByName(facultyName);
            cursor = dbHelper.getCoursesByFaculty(facultyDbId);
        } else {
            cursor = dbHelper.getAllCourses();
        }
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
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