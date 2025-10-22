// model/Course.java
package model;

public class Course {
    private String courseId;
    private String title;
    private String code;
    private int credits;

    public Course() {}

    public Course(String courseId, String title, String code, int credits) {
        this.courseId = courseId;
        this.title = title;
        this.code = code;
        this.credits = credits;
    }

    // Getters and setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
}