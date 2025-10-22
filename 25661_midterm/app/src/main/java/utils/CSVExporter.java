// utils/CSVExporter.java
package utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import dao.EnrollmentDAO;
import dao.StudentDAO;
import model.Student;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CSVExporter {

    private static final String TAG = "CSVExporter";

    public static boolean exportStudentsToCSV(Context context) {
        StudentDAO studentDAO = new StudentDAO(context);
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(context);

        studentDAO.open();
        enrollmentDAO.open();

        List<Student> students = studentDAO.getAllStudents();

        try {
            // Get the app's external files directory (doesn't require permissions on newer Android)
            File downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (downloadsDir == null) {
                // Fallback to internal storage
                downloadsDir = new File(context.getFilesDir(), "exports");
            }

            if (!downloadsDir.exists()) {
                boolean created = downloadsDir.mkdirs();
                if (!created) {
                    Log.e(TAG, "Failed to create directory: " + downloadsDir.getAbsolutePath());
                    return false;
                }
            }

            // Create file with timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                    .format(new Date());
            String fileName = "students_export_" + timeStamp + ".csv";
            File file = new File(downloadsDir, fileName);

            FileWriter writer = new FileWriter(file);

            // Write header with enrollment information
            writer.append("Student ID,Name,Gender,Email,Phone,Enrolled Courses\n");

            // Write data
            for (Student student : students) {
                writer.append(escapeCsvField(student.getStudentId())).append(",");
                writer.append(escapeCsvField(student.getName())).append(",");
                writer.append(escapeCsvField(student.getGender())).append(",");
                writer.append(escapeCsvField(student.getEmail())).append(",");
                writer.append(escapeCsvField(student.getPhone())).append(",");

                // Get enrolled courses for this student
                List<String> enrolledCourses = enrollmentDAO.getEnrolledCoursesByStudent(student.getStudentId());
                StringBuilder coursesBuilder = new StringBuilder();
                for (String course : enrolledCourses) {
                    if (coursesBuilder.length() > 0) {
                        coursesBuilder.append("; ");
                    }
                    coursesBuilder.append(course);
                }

                writer.append(escapeCsvField(coursesBuilder.toString())).append("\n");
            }

            writer.flush();
            writer.close();

            Log.d(TAG, "CSV exported successfully to: " + file.getAbsolutePath());
            return true;

        } catch (IOException e) {
            Log.e(TAG, "Error exporting CSV: " + e.getMessage(), e);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error: " + e.getMessage(), e);
            return false;
        } finally {
            studentDAO.close();
            enrollmentDAO.close();
        }
    }

    private static String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n") || field.contains("\r")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    // Helper method to get the export directory path for display
    public static String getExportDirectoryPath(Context context) {
        File downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (downloadsDir != null) {
            return downloadsDir.getAbsolutePath();
        }
        return new File(context.getFilesDir(), "exports").getAbsolutePath();
    }
}