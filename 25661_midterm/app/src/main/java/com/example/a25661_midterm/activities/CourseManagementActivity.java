// activities/CourseManagementActivity.java
package com.example.a25661_midterm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
//import com.auca.midterm.R;
import dao.CourseDAO;
import com.example.a25661_midterm.R;

import model.Course;
import java.util.List;

public class CourseManagementActivity extends AppCompatActivity {
    private CourseDAO courseDAO;
    private List<Course> courseList;
    private ListView listViewCourses;
    private Button btnAddCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_management);

        courseDAO = new CourseDAO(this);
        courseDAO.open();

        listViewCourses = findViewById(R.id.listViewCourses);
        btnAddCourse = findViewById(R.id.btnAddCourse);

        btnAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(CourseManagementActivity.this,
                    AddEditCourseActivity.class);
            startActivity(intent);
        });

        loadCourses();
    }

    private void loadCourses() {
        courseList = courseDAO.getAllCourses();
        String[] courseTitles = new String[courseList.size()];
        for (int i = 0; i < courseList.size(); i++) {
            courseTitles[i] = courseList.get(i).getTitle() + " (" +
                    courseList.get(i).getCode() + ") - " +
                    courseList.get(i).getCredits() + " credits";
        }

        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, courseTitles);
        listViewCourses.setAdapter(adapter);

        // Set click listener for editing courses
        listViewCourses.setOnItemClickListener((parent, view, position, id) -> {
            Course course = courseList.get(position);
            Intent intent = new Intent(CourseManagementActivity.this,
                    AddEditCourseActivity.class);
            intent.putExtra("COURSE_ID", course.getCourseId());
            startActivity(intent);
        });

        // Set long click listener for deleting courses
        listViewCourses.setOnItemLongClickListener((parent, view, position, id) -> {
            Course course = courseList.get(position);
            showDeleteDialog(course);
            return true;
        });
    }

    private void showDeleteDialog(Course course) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete " + course.getTitle() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    courseDAO.deleteCourse(course.getCourseId());
                    loadCourses(); // Refresh the list
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }

    @Override
    protected void onDestroy() {
        courseDAO.close();
        super.onDestroy();
    }
}