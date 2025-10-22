// utils/CSVExporter.java (Enhanced)
package utils;

import android.content.Context;
import android.os.Environment;
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

    public static boolean exportStudentsToCSV(Context context) {
        StudentDAO studentDAO = new StudentDAO(context);
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO(context);

        studentDAO.open();
        enrollmentDAO.open();

        List<Student> students = studentDAO.getAllStudents();

        // Create downloads directory if it doesn't exist
        File downloadsDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        // Create file with timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(new Date());
        String fileName = "students_with_enrollments_" + timeStamp + ".csv";
        File file = new File(downloadsDir, fileName);

        try {
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
                List<String> enrolledCourseIds = enrollmentDAO.getEnrolledCourseIdsByStudent(
                        student.getStudentId());
                StringBuilder coursesBuilder = new StringBuilder();
                for (String courseId : enrolledCourseIds) {
                    if (coursesBuilder.length() > 0) {
                        coursesBuilder.append("; ");
                    }
                    coursesBuilder.append(courseId);
                }

                writer.append(escapeCsvField(coursesBuilder.toString())).append("\n");
            }

            writer.flush();
            writer.close();

            studentDAO.close();
            enrollmentDAO.close();

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            studentDAO.close();
            enrollmentDAO.close();
            return false;
        }
    }

    private static String escapeCsvField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}