package com.example.assignment2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import database.DatabaseHelper;

public class ExportActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        dbHelper = new DatabaseHelper(this);
        Button exportBtn = findViewById(R.id.btnExportCsv);

        exportBtn.setOnClickListener(v -> exportAllTables());
    }

    private void exportAllTables() {
        String[] tables = {"users", "faculties", "courses", "students", "enrollments"};
        for (String table : tables) {
            exportTableToCSV(this, table);
        }
        Toast.makeText(this, "Export complete! Files in app folder.", Toast.LENGTH_LONG).show();
    }

    private void exportTableToCSV(Context context, String tableName) {
        Cursor cursor = dbHelper.getAllForExport(tableName);
        if (cursor == null) return;

        File dir = context.getExternalFilesDir(null);
        if (dir == null) return;

        File outFile = new File(dir, tableName + ".csv");

        try (FileWriter fw = new FileWriter(outFile)) {
            // Write header
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                fw.append(cursor.getColumnName(i));
                if (i < cursor.getColumnCount() - 1) fw.append(",");
            }
            fw.append("\n");

            // Write rows
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String val = cursor.getString(i);
                    if (val == null) val = "";
                    val = val.replace("\"", "\"\""); // escape quotes
                    fw.append("\"").append(val).append("\"");
                    if (i < cursor.getColumnCount() - 1) fw.append(",");
                }
                fw.append("\n");
            }

            fw.flush();
            Toast.makeText(context, "Exported " + tableName + " to " + outFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error exporting " + tableName, Toast.LENGTH_SHORT).show();
        } finally {
            cursor.close();
        }
    }
}
