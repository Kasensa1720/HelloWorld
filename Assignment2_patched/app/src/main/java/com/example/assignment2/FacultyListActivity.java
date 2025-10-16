
package com.example.assignment2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import database.DatabaseHelper;

import java.util.ArrayList;

public class FacultyListActivity extends AppCompatActivity {

    DatabaseHelper db;
    ListView lv;
    ArrayAdapter<String> adapter;
    ArrayList<Long> ids = new ArrayList<>();
    ArrayList<String> items = new ArrayList<>();
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_list);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listFaculties);
        btnAdd = findViewById(R.id.btnAddFaculty);

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(FacultyListActivity.this, FacultyFormActivity.class));
        });

        lv.setOnItemClickListener((parent, view, position, id) -> {
            long facultyId = ids.get(position);
            Intent i = new Intent(FacultyListActivity.this, StudentListActivity.class);
            i.putExtra("faculty_id", facultyId);
            startActivity(i);
        });

        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            long facultyId = ids.get(position);
            new AlertDialog.Builder(FacultyListActivity.this)
                    .setTitle("Faculty options")
                    .setItems(new String[]{"Edit", "Delete", "View Courses"}, (dialog, which) -> {
                        if (which == 0) {
                            Intent it = new Intent(FacultyListActivity.this, FacultyFormActivity.class);
                            it.putExtra("faculty_id", facultyId);
                            startActivity(it);
                        } else if (which == 1) {
                            db.deleteFaculty(facultyId);
                            loadData();
                        } else if (which == 2) {
                            Intent it = new Intent(FacultyListActivity.this, CourseListActivity.class);
                            it.putExtra("faculty_id", facultyId);
                            startActivity(it);
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
        Cursor c = db.getAllFaculties();
        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("faculty_id"));
                String name = c.getString(c.getColumnIndexOrThrow("faculty_name"));
                String dean = c.getString(c.getColumnIndexOrThrow("dean_name"));
                ids.add(id);
                items.add(name + " — Dean: " + (dean==null? "N/A":dean));
            }
            c.close();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);
    }
}
