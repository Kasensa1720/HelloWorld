// activities/MainActivity.java
package com.example.a25661_midterm.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a25661_midterm.R;
//import com.auca.midterm.R;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_LAUNCH_COUNT = "launch_count";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Update launch count
        int launchCount = sharedPreferences.getInt(KEY_LAUNCH_COUNT, 0) + 1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_LAUNCH_COUNT, launchCount);
        editor.apply();

        // Display launch count
        TextView tvLaunchCount = findViewById(R.id.tvLaunchCount);
        tvLaunchCount.setText("App launched: " + launchCount + " times");

        // Initialize buttons
        Button btnStudentManagement = findViewById(R.id.btnStudentManagement);
        Button btnCourseManagement = findViewById(R.id.btnCourseManagement);
        Button btnEnrollments = findViewById(R.id.btnEnrollments);
        Button btnExportCSV = findViewById(R.id.btnExportCSV);

        btnStudentManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StudentManagementActivity.class);
            startActivity(intent);
        });

        btnCourseManagement.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CourseManagementActivity.class);
            startActivity(intent);
        });

        btnEnrollments.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EnrollmentActivity.class);
            startActivity(intent);
        });

        // Update the CSV export button in MainActivity.java
        btnExportCSV.setOnClickListener(v -> {
            boolean success = utils.CSVExporter.exportStudentsToCSV(MainActivity.this);
            if (success) {
                android.widget.Toast.makeText(MainActivity.this,
                        "CSV exported successfully to Downloads folder",
                        android.widget.Toast.LENGTH_LONG).show();
            } else {
                android.widget.Toast.makeText(MainActivity.this,
                        "CSV export failed",
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}