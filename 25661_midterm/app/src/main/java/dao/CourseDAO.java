// dao/CourseDAO.java
package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public CourseDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws android.database.SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COURSE_ID, course.getCourseId());
        values.put(DatabaseHelper.COLUMN_TITLE, course.getTitle());
        values.put(DatabaseHelper.COLUMN_CODE, course.getCode());
        values.put(DatabaseHelper.COLUMN_CREDITS, course.getCredits());

        return db.insert(DatabaseHelper.TABLE_COURSES, null, values);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_COURSES,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_COURSE_ID)));
                course.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_TITLE)));
                course.setCode(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_CODE)));
                course.setCredits(cursor.getInt(cursor.getColumnIndexOrThrow(
                        DatabaseHelper.COLUMN_CREDITS)));
                courses.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courses;
    }

    public int updateCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, course.getTitle());
        values.put(DatabaseHelper.COLUMN_CODE, course.getCode());
        values.put(DatabaseHelper.COLUMN_CREDITS, course.getCredits());

        return db.update(DatabaseHelper.TABLE_COURSES, values,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{course.getCourseId()});
    }

    public void deleteCourse(String courseId) {
        db.delete(DatabaseHelper.TABLE_COURSES,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId});
    }

    public Course getCourseById(String courseId) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_COURSES, null,
                DatabaseHelper.COLUMN_COURSE_ID + " = ?",
                new String[]{courseId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Course course = new Course();
            course.setCourseId(cursor.getString(cursor.getColumnIndexOrThrow(
                    DatabaseHelper.COLUMN_COURSE_ID)));
            course.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(
                    DatabaseHelper.COLUMN_TITLE)));
            course.setCode(cursor.getString(cursor.getColumnIndexOrThrow(
                    DatabaseHelper.COLUMN_CODE)));
            course.setCredits(cursor.getInt(cursor.getColumnIndexOrThrow(
                    DatabaseHelper.COLUMN_CREDITS)));
            cursor.close();
            return course;
        }
        return null;
    }
}