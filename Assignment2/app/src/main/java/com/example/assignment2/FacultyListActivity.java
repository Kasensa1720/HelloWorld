package com.example.assignment2;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

public class FacultyListActivity extends AppCompatActivity {

    private ListView listViewFaculties;
    private Button btnAddFaculty;
    private DatabaseHelper dbHelper;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);

        dbHelper = new DatabaseHelper(this);
        initializeViews();
        setupListView();
        setupClickListeners();
        loadFaculties();
    }

    private void initializeViews() {
        listViewFaculties = findViewById(R.id.listViewFaculties);
        btnAddFaculty = findViewById(R.id.btnAddFaculty);
    }

    private void setupListView() {
        String[] from = {DatabaseHelper.COLUMN_FACULTY_NAME, DatabaseHelper.COLUMN_DEAN_NAME, "creator_name"};
        int[] to = {R.id.textFacultyName, R.id.textDeanName, R.id.textCreatedBy};

        adapter = new SimpleCursorAdapter(this, R.layout.list_item_faculty, null, from, to, 0);
        listViewFaculties.setAdapter(adapter);

        listViewFaculties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                String facultyId = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_ID));
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FACULTY_NAME));

                Intent intent = new Intent(FacultyListActivity.this, FacultyDetailActivity.class);
                intent.putExtra("FACULTY_ID", facultyId);
                intent.putExtra("FACULTY_NAME", facultyName);
                startActivity(intent);
            }
        });
    }

    private void setupClickListeners() {
        btnAddFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacultyListActivity.this, AddFacultyActivity.class));
            }
        });
    }

    private void loadFaculties() {
        Cursor cursor = dbHelper.getAllFaculties();
        adapter.changeCursor(cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFaculties();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.getCursor().close();
        dbHelper.close();
    }
}