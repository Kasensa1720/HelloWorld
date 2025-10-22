package utils;

public class Constants {
    // SharedPreferences
    public static final String PREFS_NAME = "AppPrefs";
    public static final String KEY_LAUNCH_COUNT = "launch_count";
    public static final String KEY_STUDENT_COUNT = "student_count";

    // Request codes
    public static final int REQUEST_ADD_STUDENT = 1;
    public static final int REQUEST_EDIT_STUDENT = 2;
    public static final int REQUEST_ADD_COURSE = 3;
    public static final int REQUEST_EDIT_COURSE = 4;

    // Intent extras
    public static final String EXTRA_STUDENT_ID = "STUDENT_ID";
    public static final String EXTRA_COURSE_ID = "COURSE_ID";
    public static final String EXTRA_STUDENT = "student";
    public static final String EXTRA_COURSE = "course";

    // Database constants
    public static final String DATABASE_NAME = "university.db";
    public static final int DATABASE_VERSION = 1;
}