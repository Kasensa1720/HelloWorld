// dao/DatabaseHelper.java
package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "university.db";
    private static final int DATABASE_VERSION = 1;

    // Students table
    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";

    private static final String CREATE_TABLE_STUDENTS =
            "CREATE TABLE " + TABLE_STUDENTS + "(" +
                    COLUMN_STUDENT_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAME + " TEXT NOT NULL," +
                    COLUMN_GENDER + " TEXT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_PHONE + " TEXT" +
                    ")";

    // Courses table
    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_COURSE_ID = "course_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_CREDITS = "credits";

    private static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + TABLE_COURSES + "(" +
                    COLUMN_COURSE_ID + " TEXT PRIMARY KEY," +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_CODE + " TEXT NOT NULL," +
                    COLUMN_CREDITS + " INTEGER" +
                    ")";

    // Enrollments table
    public static final String TABLE_ENROLLMENTS = "enrollments";
    public static final String COLUMN_ENROLLMENT_ID = "enrollment_id";
    public static final String COLUMN_STUDENT_ID_FK = "student_id";
    public static final String COLUMN_COURSE_ID_FK = "course_id";

    private static final String CREATE_TABLE_ENROLLMENTS =
            "CREATE TABLE " + TABLE_ENROLLMENTS + "(" +
                    COLUMN_ENROLLMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_STUDENT_ID_FK + " TEXT," +
                    COLUMN_COURSE_ID_FK + " TEXT," +
                    "FOREIGN KEY(" + COLUMN_STUDENT_ID_FK + ") REFERENCES " +
                    TABLE_STUDENTS + "(" + COLUMN_STUDENT_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_COURSE_ID_FK + ") REFERENCES " +
                    TABLE_COURSES + "(" + COLUMN_COURSE_ID + ")" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_ENROLLMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }
}