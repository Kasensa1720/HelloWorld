// dao/EnrollmentDAO.java
package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.Enrollment;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public EnrollmentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws android.database.SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long enrollStudent(String studentId, String courseId) {
        // Check if enrollment already exists
        if (isEnrolled(studentId, courseId)) {
            return -1; // Already enrolled
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_ID_FK, studentId);
        values.put(DatabaseHelper.COLUMN_COURSE_ID_FK, courseId);

        return db.insert(DatabaseHelper.TABLE_ENROLLMENTS, null, values);
    }

    public boolean isEnrolled(String studentId, String courseId) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_ENROLLMENTS, null,
                DatabaseHelper.COLUMN_STUDENT_ID_FK + " = ? AND " +
                        DatabaseHelper.COLUMN_COURSE_ID_FK + " = ?",
                new String[]{studentId, courseId}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void unenrollStudent(int enrollmentId) {
        db.delete(DatabaseHelper.TABLE_ENROLLMENTS,
                DatabaseHelper.COLUMN_ENROLLMENT_ID + " = ?",
                new String[]{String.valueOf(enrollmentId)});
    }

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT e." + DatabaseHelper.COLUMN_ENROLLMENT_ID + ", " +
                "e." + DatabaseHelper.COLUMN_STUDENT_ID_FK + ", " +
                "e." + DatabaseHelper.COLUMN_COURSE_ID_FK + ", " +
                "s." + DatabaseHelper.COLUMN_NAME + " as student_name, " +
                "c." + DatabaseHelper.COLUMN_TITLE + " as course_title " +
                "FROM " + DatabaseHelper.TABLE_ENROLLMENTS + " e " +
                "INNER JOIN " + DatabaseHelper.TABLE_STUDENTS + " s ON " +
                "e." + DatabaseHelper.COLUMN_STUDENT_ID_FK + " = s." + DatabaseHelper.COLUMN_STUDENT_ID + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_COURSES + " c ON " +
                "e." + DatabaseHelper.COLUMN_COURSE_ID_FK + " = c." + DatabaseHelper.COLUMN_COURSE_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(cursor.getInt(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_ENROLLMENT_ID)));
                enrollment.setStudentId(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_STUDENT_ID_FK)));
                enrollment.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_COURSE_ID_FK)));
                enrollment.setStudentName(cursor.getString(cursor.getColumnIndexOrThrow("student_name")));
                enrollment.setCourseTitle(cursor.getString(cursor.getColumnIndexOrThrow("course_title")));
                enrollments.add(enrollment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return enrollments;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT e." + DatabaseHelper.COLUMN_ENROLLMENT_ID + ", " +
                "e." + DatabaseHelper.COLUMN_STUDENT_ID_FK + ", " +
                "e." + DatabaseHelper.COLUMN_COURSE_ID_FK + ", " +
                "s." + DatabaseHelper.COLUMN_NAME + " as student_name, " +
                "c." + DatabaseHelper.COLUMN_TITLE + " as course_title " +
                "FROM " + DatabaseHelper.TABLE_ENROLLMENTS + " e " +
                "INNER JOIN " + DatabaseHelper.TABLE_STUDENTS + " s ON " +
                "e." + DatabaseHelper.COLUMN_STUDENT_ID_FK + " = s." + DatabaseHelper.COLUMN_STUDENT_ID + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_COURSES + " c ON " +
                "e." + DatabaseHelper.COLUMN_COURSE_ID_FK + " = c." + DatabaseHelper.COLUMN_COURSE_ID + " " +
                "WHERE e." + DatabaseHelper.COLUMN_STUDENT_ID_FK + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{studentId});

        if (cursor.moveToFirst()) {
            do {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(cursor.getInt(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_ENROLLMENT_ID)));
                enrollment.setStudentId(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_STUDENT_ID_FK)));
                enrollment.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_COURSE_ID_FK)));
                enrollment.setStudentName(cursor.getString(cursor.getColumnIndexOrThrow("student_name")));
                enrollment.setCourseTitle(cursor.getString(cursor.getColumnIndexOrThrow("course_title")));
                enrollments.add(enrollment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return enrollments;
    }

    public List<String> getEnrolledCourseIdsByStudent(String studentId) {
        List<String> courseIds = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ENROLLMENTS,
                new String[]{DatabaseHelper.COLUMN_COURSE_ID_FK},
                DatabaseHelper.COLUMN_STUDENT_ID_FK + " = ?",
                new String[]{studentId}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                courseIds.add(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_COURSE_ID_FK)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courseIds;
    }

    // Add this method to EnrollmentDAO.java
    public List<String> getEnrolledCoursesByStudent(String studentId) {
        List<String> courses = new ArrayList<>();
        String query = "SELECT c." + DatabaseHelper.COLUMN_TITLE +
                " FROM " + DatabaseHelper.TABLE_ENROLLMENTS + " e " +
                "INNER JOIN " + DatabaseHelper.TABLE_COURSES + " c ON " +
                "e." + DatabaseHelper.COLUMN_COURSE_ID_FK + " = c." + DatabaseHelper.COLUMN_COURSE_ID + " " +
                "WHERE e." + DatabaseHelper.COLUMN_STUDENT_ID_FK + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{studentId});

        if (cursor.moveToFirst()) {
            do {
                int courseTitleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
                if (courseTitleIndex != -1) {
                    String courseTitle = cursor.getString(courseTitleIndex);
                    courses.add(courseTitle);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }
}