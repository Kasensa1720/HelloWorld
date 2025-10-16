package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentManagerDB";
    private static final int DATABASE_VERSION = 2;

    // USERS table (existing)
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    // FACULTIES table
    public static final String TABLE_FACULTIES = "faculties";
    public static final String COLUMN_FACULTY_ID = "faculty_id";
    public static final String COLUMN_FACULTY_NAME = "faculty_name";
    public static final String COLUMN_DEAN_NAME = "dean_name";
    public static final String COLUMN_CREATED_BY = "created_by";

    // STUDENTS table
    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_STUDENT_EMAIL = "student_email";
    public static final String COLUMN_STUDENT_PHONE = "student_phone";
    public static final String COLUMN_STUDENT_GENDER = "student_gender";
    public static final String COLUMN_FACULTY_ID_FK = "faculty_id";

    // COURSES table
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_COURSE_NAME = "course_name";
    public static final String COLUMN_COURSE_CODE = "course_code";
    public static final String COLUMN_CREDITS = "credits";

    // ENROLLMENTS table (Bonus - many-to-many)
    public static final String TABLE_ENROLLMENTS = "enrollments";
    public static final String COLUMN_ENROLLMENT_ID = "enrollment_id";
    public static final String COLUMN_STUDENT_ID_FK = "student_id";
    public static final String COLUMN_COURSE_ID_FK = "course_id";

    // Table creation statements
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " TEXT UNIQUE, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_PHONE + " TEXT" +
                    ");";

    private static final String CREATE_TABLE_FACULTIES =
            "CREATE TABLE " + TABLE_FACULTIES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FACULTY_ID + " TEXT UNIQUE, " +
                    COLUMN_FACULTY_NAME + " TEXT NOT NULL, " +
                    COLUMN_DEAN_NAME + " TEXT NOT NULL, " +
                    COLUMN_CREATED_BY + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_CREATED_BY + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_STUDENTS =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_ID + " TEXT UNIQUE, " +
                    COLUMN_STUDENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_STUDENT_EMAIL + " TEXT, " +
                    COLUMN_STUDENT_PHONE + " TEXT, " +
                    COLUMN_STUDENT_GENDER + " TEXT, " +
                    COLUMN_FACULTY_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_CREATED_BY + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_FACULTY_ID_FK + ") REFERENCES " +
                    TABLE_FACULTIES + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_CREATED_BY + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + TABLE_COURSES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_COURSE_ID + " TEXT UNIQUE, " +
                    COLUMN_COURSE_NAME + " TEXT NOT NULL, " +
                    COLUMN_COURSE_CODE + " TEXT UNIQUE, " +
                    COLUMN_CREDITS + " INTEGER, " +
                    COLUMN_FACULTY_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_CREATED_BY + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_FACULTY_ID_FK + ") REFERENCES " +
                    TABLE_FACULTIES + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_CREATED_BY + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_ENROLLMENTS =
            "CREATE TABLE " + TABLE_ENROLLMENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ENROLLMENT_ID + " TEXT UNIQUE, " +
                    COLUMN_STUDENT_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_COURSE_ID_FK + " INTEGER NOT NULL, " +
                    COLUMN_CREATED_BY + " INTEGER NOT NULL, " +
                    "FOREIGN KEY (" + COLUMN_STUDENT_ID_FK + ") REFERENCES " +
                    TABLE_STUDENTS + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_COURSE_ID_FK + ") REFERENCES " +
                    TABLE_COURSES + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY (" + COLUMN_CREATED_BY + ") REFERENCES " +
                    TABLE_USERS + "(" + COLUMN_ID + "), " +
                    "UNIQUE(" + COLUMN_STUDENT_ID_FK + ", " + COLUMN_COURSE_ID_FK + ")" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_FACULTIES);
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_ENROLLMENTS);

        // Insert default admin user
        ContentValues adminUser = new ContentValues();
        adminUser.put(COLUMN_USER_ID, "admin001");
        adminUser.put(COLUMN_USERNAME, "Administrator");
        adminUser.put(COLUMN_EMAIL, "admin@auca.ac.rw");
        adminUser.put(COLUMN_PASSWORD, "admin123");
        adminUser.put(COLUMN_GENDER, "Other");
        adminUser.put(COLUMN_PHONE, "+250788123456");
        db.insert(TABLE_USERS, null, adminUser);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACULTIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ==================== USER OPERATIONS ====================
    public boolean addUser(String userId, String username, String password, String gender, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public Cursor getUserById(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?",
                new String[]{userId});
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " +
                        COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public Cursor getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email});
    }

    public boolean updateUser(String userId, String username, String password, String gender, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);

        int result = db.update(TABLE_USERS, values, COLUMN_USER_ID + " = ?", new String[]{userId});
        db.close();
        return result > 0;
    }

    public boolean deleteUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COLUMN_USER_ID + " = ?", new String[]{userId});
        db.close();
        return result > 0;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ==================== FACULTY OPERATIONS ====================
    public boolean addFaculty(String facultyId, String facultyName, String deanName, long createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FACULTY_ID, facultyId);
        values.put(COLUMN_FACULTY_NAME, facultyName);
        values.put(COLUMN_DEAN_NAME, deanName);
        values.put(COLUMN_CREATED_BY, createdBy);

        long result = db.insert(TABLE_FACULTIES, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllFaculties() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT f.*, u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_FACULTIES + " f " +
                "JOIN " + TABLE_USERS + " u ON f." + COLUMN_CREATED_BY + " = u." + COLUMN_ID;
        return db.rawQuery(query, null);
    }

    public Cursor getFacultyById(String facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT f.*, u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_FACULTIES + " f " +
                "JOIN " + TABLE_USERS + " u ON f." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE f." + COLUMN_FACULTY_ID + " = ?";
        return db.rawQuery(query, new String[]{facultyId});
    }

    public boolean updateFaculty(String facultyId, String facultyName, String deanName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FACULTY_NAME, facultyName);
        values.put(COLUMN_DEAN_NAME, deanName);

        int result = db.update(TABLE_FACULTIES, values, COLUMN_FACULTY_ID + " = ?", new String[]{facultyId});
        db.close();
        return result > 0;
    }

    public boolean deleteFaculty(String facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FACULTIES, COLUMN_FACULTY_ID + " = ?", new String[]{facultyId});
        db.close();
        return result > 0;
    }

    public Cursor getFacultiesForSpinner() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_FACULTY_NAME +
                " FROM " + TABLE_FACULTIES + " ORDER BY " + COLUMN_FACULTY_NAME, null);
    }

    // ==================== STUDENT OPERATIONS ====================
    public boolean addStudent(String studentId, String studentName, String email, String phone,
                              String gender, String facultyId, long createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, studentId);
        values.put(COLUMN_STUDENT_NAME, studentName);
        values.put(COLUMN_STUDENT_EMAIL, email);
        values.put(COLUMN_STUDENT_PHONE, phone);
        values.put(COLUMN_STUDENT_GENDER, gender);
        values.put(COLUMN_FACULTY_ID_FK, facultyId);
        values.put(COLUMN_CREATED_BY, createdBy);

        long result = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_STUDENTS + " s " +
                "JOIN " + TABLE_FACULTIES + " f ON s." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON s." + COLUMN_CREATED_BY + " = u." + COLUMN_ID;
        return db.rawQuery(query, null);
    }

    public Cursor getStudentsByFaculty(long facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_STUDENTS + " s " +
                "JOIN " + TABLE_FACULTIES + " f ON s." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON s." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE s." + COLUMN_FACULTY_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(facultyId)});
    }

    public Cursor getStudentById(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.*, f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_STUDENTS + " s " +
                "JOIN " + TABLE_FACULTIES + " f ON s." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON s." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE s." + COLUMN_STUDENT_ID + " = ?";
        return db.rawQuery(query, new String[]{studentId});
    }

    public boolean updateStudent(String studentId, String studentName, String email, String phone,
                                 String gender, String facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, studentName);
        values.put(COLUMN_STUDENT_EMAIL, email);
        values.put(COLUMN_STUDENT_PHONE, phone);
        values.put(COLUMN_STUDENT_GENDER, gender);
        values.put(COLUMN_FACULTY_ID_FK, facultyId);

        int result = db.update(TABLE_STUDENTS, values, COLUMN_STUDENT_ID + " = ?", new String[]{studentId});
        db.close();
        return result > 0;
    }

    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_STUDENTS, COLUMN_STUDENT_ID + " = ?", new String[]{studentId});
        db.close();
        return result > 0;
    }

    // ==================== COURSE OPERATIONS ====================
    public boolean addCourse(String courseId, String courseName, String courseCode,
                             int credits, long facultyId, long createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_ID, courseId);
        values.put(COLUMN_COURSE_NAME, courseName);
        values.put(COLUMN_COURSE_CODE, courseCode);
        values.put(COLUMN_CREDITS, credits);
        values.put(COLUMN_FACULTY_ID_FK, facultyId);
        values.put(COLUMN_CREATED_BY, createdBy);

        long result = db.insert(TABLE_COURSES, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllCourses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_COURSES + " c " +
                "JOIN " + TABLE_FACULTIES + " f ON c." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON c." + COLUMN_CREATED_BY + " = u." + COLUMN_ID;
        return db.rawQuery(query, null);
    }

    public Cursor getCoursesByFaculty(long facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_COURSES + " c " +
                "JOIN " + TABLE_FACULTIES + " f ON c." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON c." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE c." + COLUMN_FACULTY_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(facultyId)});
    }

    public Cursor getCourseById(String courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_COURSES + " c " +
                "JOIN " + TABLE_FACULTIES + " f ON c." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON c." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE c." + COLUMN_COURSE_ID + " = ?";
        return db.rawQuery(query, new String[]{courseId});
    }

    public boolean updateCourse(String courseId, String courseName, String courseCode, int credits, long facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSE_NAME, courseName);
        values.put(COLUMN_COURSE_CODE, courseCode);
        values.put(COLUMN_CREDITS, credits);
        values.put(COLUMN_FACULTY_ID_FK, facultyId);

        int result = db.update(TABLE_COURSES, values, COLUMN_COURSE_ID + " = ?", new String[]{courseId});
        db.close();
        return result > 0;
    }

    public boolean deleteCourse(String courseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_COURSES, COLUMN_COURSE_ID + " = ?", new String[]{courseId});
        db.close();
        return result > 0;
    }

    // ==================== ENROLLMENT OPERATIONS (BONUS) ====================
    public boolean enrollStudent(String enrollmentId, long studentId, long courseId, long createdBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENROLLMENT_ID, enrollmentId);
        values.put(COLUMN_STUDENT_ID_FK, studentId);
        values.put(COLUMN_COURSE_ID_FK, courseId);
        values.put(COLUMN_CREATED_BY, createdBy);

        long result = db.insert(TABLE_ENROLLMENTS, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getStudentEnrollments(long studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, c." + COLUMN_COURSE_NAME + ", c." + COLUMN_COURSE_CODE + ", " +
                "f." + COLUMN_FACULTY_NAME + ", u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_ENROLLMENTS + " e " +
                "JOIN " + TABLE_COURSES + " c ON e." + COLUMN_COURSE_ID_FK + " = c." + COLUMN_ID + " " +
                "JOIN " + TABLE_FACULTIES + " f ON c." + COLUMN_FACULTY_ID_FK + " = f." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON e." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE e." + COLUMN_STUDENT_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(studentId)});
    }

    public Cursor getCourseEnrollments(long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e.*, s." + COLUMN_STUDENT_NAME + ", s." + COLUMN_STUDENT_ID + ", " +
                "u." + COLUMN_USERNAME + " as creator_name " +
                "FROM " + TABLE_ENROLLMENTS + " e " +
                "JOIN " + TABLE_STUDENTS + " s ON e." + COLUMN_STUDENT_ID_FK + " = s." + COLUMN_ID + " " +
                "JOIN " + TABLE_USERS + " u ON e." + COLUMN_CREATED_BY + " = u." + COLUMN_ID + " " +
                "WHERE e." + COLUMN_COURSE_ID_FK + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(courseId)});
    }

    public boolean isStudentEnrolled(long studentId, long courseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ENROLLMENTS + " WHERE " +
                        COLUMN_STUDENT_ID_FK + " = ? AND " + COLUMN_COURSE_ID_FK + " = ?",
                new String[]{String.valueOf(studentId), String.valueOf(courseId)});
        boolean enrolled = cursor.getCount() > 0;
        cursor.close();
        return enrolled;
    }

    public boolean unenrollStudent(String enrollmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ENROLLMENTS, COLUMN_ENROLLMENT_ID + " = ?", new String[]{enrollmentId});
        db.close();
        return result > 0;
    }

    // Utility methods
    public long getUserIdByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email});
        long userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        return userId;
    }

    public long getFacultyIdByName(String facultyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_FACULTIES + " WHERE " + COLUMN_FACULTY_NAME + " = ?",
                new String[]{facultyName});
        long facultyId = -1;
        if (cursor.moveToFirst()) {
            facultyId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        return facultyId;
    }
}