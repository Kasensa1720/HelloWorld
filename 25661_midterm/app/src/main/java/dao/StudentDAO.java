// dao/StudentDAO.java
package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import model.Student;
import java.util.ArrayList;
import java.util.List;


public class StudentDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public StudentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws android.database.SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STUDENT_ID, student.getStudentId());
        values.put(DatabaseHelper.COLUMN_NAME, student.getName());
        values.put(DatabaseHelper.COLUMN_GENDER, student.getGender());
        values.put(DatabaseHelper.COLUMN_EMAIL, student.getEmail());
        values.put(DatabaseHelper.COLUMN_PHONE, student.getPhone());

        return db.insert(DatabaseHelper.TABLE_STUDENTS, null, values);
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENTS,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                // Use getColumnIndex with safety checks instead of getColumnIndexOrThrow
                int studentIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                int genderIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GENDER);
                int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
                int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);

                if (studentIdIndex != -1) {
                    student.setStudentId(cursor.getString(studentIdIndex));
                }
                if (nameIndex != -1) {
                    student.setName(cursor.getString(nameIndex));
                }
                if (genderIndex != -1) {
                    student.setGender(cursor.getString(genderIndex));
                }
                if (emailIndex != -1) {
                    student.setEmail(cursor.getString(emailIndex));
                }
                if (phoneIndex != -1) {
                    student.setPhone(cursor.getString(phoneIndex));
                }
                students.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return students;
    }

    public int updateStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, student.getName());
        values.put(DatabaseHelper.COLUMN_GENDER, student.getGender());
        values.put(DatabaseHelper.COLUMN_EMAIL, student.getEmail());
        values.put(DatabaseHelper.COLUMN_PHONE, student.getPhone());

        return db.update(DatabaseHelper.TABLE_STUDENTS, values,
                DatabaseHelper.COLUMN_STUDENT_ID + " = ?",
                new String[]{student.getStudentId()});
    }

    public void deleteStudent(String studentId) {
        db.delete(DatabaseHelper.TABLE_STUDENTS,
                DatabaseHelper.COLUMN_STUDENT_ID + " = ?",
                new String[]{studentId});
    }

    public Student getStudentById(String studentId) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_STUDENTS, null,
                DatabaseHelper.COLUMN_STUDENT_ID + " = ?",
                new String[]{studentId}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Student student = new Student();

            // Use getColumnIndex with safety checks
            int studentIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_STUDENT_ID);
            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            int genderIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_GENDER);
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL);
            int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);

            if (studentIdIndex != -1) {
                student.setStudentId(cursor.getString(studentIdIndex));
            }
            if (nameIndex != -1) {
                student.setName(cursor.getString(nameIndex));
            }
            if (genderIndex != -1) {
                student.setGender(cursor.getString(genderIndex));
            }
            if (emailIndex != -1) {
                student.setEmail(cursor.getString(emailIndex));
            }
            if (phoneIndex != -1) {
                student.setPhone(cursor.getString(phoneIndex));
            }

            cursor.close();
            return student;
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
}