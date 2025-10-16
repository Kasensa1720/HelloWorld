package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "student_manager.db";
    private static final int DATABASE_VERSION = 2;

    // ---------- TABLE NAMES ----------
    public static final String TABLE_USERS = "users";
    public static final String TABLE_FACULTIES = "faculties";
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_STUDENTS = "students";
    public static final String TABLE_ENROLLMENTS = "enrollments";

    // ---------- COMMON COLUMNS ----------
    public static final String COLUMN_CREATED_BY = "created_by";

    // ---------- USER COLUMNS ----------
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    // ---------- FACULTY COLUMNS ----------
    public static final String COLUMN_FACULTY_ID = "faculty_id";
    public static final String COLUMN_FACULTY_NAME = "faculty_name";
    public static final String COLUMN_DEAN_NAME = "dean_name";

    // ---------- COURSE COLUMNS ----------
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_FACULTY_ID = "faculty_id";

    // ---------- STUDENT COLUMNS ----------
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_STUDENT_EMAIL = "student_email";
    public static final String COLUMN_STUDENT_PHONE = "student_phone";
    public static final String COLUMN_STUDENT_FACULTY_ID = "faculty_id";

    // ---------- ENROLLMENT COLUMNS ----------
    public static final String COLUMN_ENROLLMENT_ID = "enrollment_id";
    public static final String COLUMN_ENROLL_STUDENT_ID = "student_id";
    public static final String COLUMN_ENROLL_COURSE_ID = "course_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ---------- TABLE CREATION ----------
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PHONE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_FACULTIES + " (" +
                COLUMN_FACULTY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FACULTY_NAME + " TEXT NOT NULL, " +
                COLUMN_DEAN_NAME + " TEXT, " +
                COLUMN_CREATED_BY + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_COURSES + " (" +
                COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                COLUMN_COURSE_FACULTY_ID + " INTEGER, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_COURSE_FACULTY_ID + ") REFERENCES " + TABLE_FACULTIES + "(" + COLUMN_FACULTY_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT NOT NULL, " +
                COLUMN_STUDENT_EMAIL + " TEXT UNIQUE, " +
                COLUMN_STUDENT_PHONE + " TEXT, " +
                COLUMN_STUDENT_FACULTY_ID + " INTEGER, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_STUDENT_FACULTY_ID + ") REFERENCES " + TABLE_FACULTIES + "(" + COLUMN_FACULTY_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_ENROLLMENTS + " (" +
                COLUMN_ENROLLMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ENROLL_STUDENT_ID + " INTEGER, " +
                COLUMN_ENROLL_COURSE_ID + " INTEGER, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ENROLL_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_STUDENT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_ENROLL_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from v" + oldVersion + " to v" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACULTIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // -------------------- USERS CRUD --------------------
    public long addUser(String username, String password, String gender, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?",
                new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // -------------------- FACULTIES CRUD --------------------
    public long createFaculty(String facultyName, String deanName, Integer createdByUserId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FACULTY_NAME, facultyName);
        values.put(COLUMN_DEAN_NAME, deanName);
        if (createdByUserId != null) values.put(COLUMN_CREATED_BY, createdByUserId);
        long id = db.insert(TABLE_FACULTIES, null, values);
        db.close();
        return id;
    }

    public Cursor getAllFaculties() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_FACULTIES + " ORDER BY " + COLUMN_FACULTY_NAME, null);
    }

    public boolean updateFaculty(long facultyId, String facultyName, String deanName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FACULTY_NAME, facultyName);
        values.put(COLUMN_DEAN_NAME, deanName);
        int res = db.update(TABLE_FACULTIES, values, COLUMN_FACULTY_ID + "=?",
                new String[]{String.valueOf(facultyId)});
        db.close();
        return res > 0;
    }

    public boolean deleteFaculty(long facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_FACULTIES, COLUMN_FACULTY_ID + "=?",
                new String[]{String.valueOf(facultyId)});
        db.close();
        return res > 0;
    }

    // -------------------- COURSES CRUD --------------------
    public long createCourse(String courseName, long facultyId, Integer createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, courseName);
        values.put(COLUMN_COURSE_FACULTY_ID, facultyId);
        if (createdBy != null) values.put(COLUMN_CREATED_BY, createdBy);
        long id = db.insert(TABLE_COURSES, null, values);
        db.close();
        return id;
    }

    public Cursor getCoursesByFaculty(long facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COURSES +
                " WHERE " + COLUMN_COURSE_FACULTY_ID + "=?", new String[]{String.valueOf(facultyId)});
    }

    public boolean updateCourse(long courseId, String courseName, long facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, courseName);
        values.put(COLUMN_COURSE_FACULTY_ID, facultyId);
        int res = db.update(TABLE_COURSES, values, COLUMN_COURSE_ID + "=?",
                new String[]{String.valueOf(courseId)});
        db.close();
        return res > 0;
    }

    public boolean deleteCourse(long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_COURSES, COLUMN_COURSE_ID + "=?",
                new String[]{String.valueOf(courseId)});
        db.close();
        return res > 0;
    }

    // -------------------- STUDENTS CRUD --------------------
    public long createStudent(String name, String email, String phone, Long facultyId, Integer createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, name);
        values.put(COLUMN_STUDENT_EMAIL, email);
        values.put(COLUMN_STUDENT_PHONE, phone);
        if (facultyId != null) values.put(COLUMN_STUDENT_FACULTY_ID, facultyId);
        if (createdBy != null) values.put(COLUMN_CREATED_BY, createdBy);
        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_STUDENT_NAME, null);
    }

    public boolean updateStudent(long studentId, String name, String email, String phone, Long facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, name);
        values.put(COLUMN_STUDENT_EMAIL, email);
        values.put(COLUMN_STUDENT_PHONE, phone);
        if (facultyId != null) values.put(COLUMN_STUDENT_FACULTY_ID, facultyId);
        int res = db.update(TABLE_STUDENTS, values, COLUMN_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)});
        db.close();
        return res > 0;
    }

    public boolean deleteStudent(long studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_STUDENTS, COLUMN_STUDENT_ID + "=?",
                new String[]{String.valueOf(studentId)});
        db.close();
        return res > 0;
    }

    // -------------------- ENROLLMENTS --------------------
    public long enrollStudentInCourse(long studentId, long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_ENROLLMENTS + " WHERE " +
                        COLUMN_ENROLL_STUDENT_ID + "=? AND " + COLUMN_ENROLL_COURSE_ID + "=?",
                new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        if (c.getCount() > 0) {
            c.close();
            db.close();
            return -1; // already enrolled
        }
        c.close();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENROLL_STUDENT_ID, studentId);
        values.put(COLUMN_ENROLL_COURSE_ID, courseId);
        if (createdBy != null) values.put(COLUMN_CREATED_BY, createdBy);
        long id = db.insert(TABLE_ENROLLMENTS, null, values);
        db.close();
        return id;
    }

    public boolean deleteEnrollment(long studentId, long courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_ENROLLMENTS,
                COLUMN_ENROLL_STUDENT_ID + "=? AND " + COLUMN_ENROLL_COURSE_ID + "=?",
                new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        db.close();
        return res > 0;
    }

    // -------------------- EXPORT --------------------
    public Cursor getAllForExport(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + tableName, null);
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}
