package com.example.assignment2;

public class Faculty {
    private String facultyId;
    private String facultyName;
    private String deanName;
    private String creatorName;

    public Faculty(String facultyId, String facultyName, String deanName, String creatorName) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.deanName = deanName;
        this.creatorName = creatorName;
    }

    // Getters and Setters
    public String getFacultyId() { return facultyId; }
    public void setFacultyId(String facultyId) { this.facultyId = facultyId; }
    public String getFacultyName() { return facultyName; }
    public void setFacultyName(String facultyName) { this.facultyName = facultyName; }
    public String getDeanName() { return deanName; }
    public void setDeanName(String deanName) { this.deanName = deanName; }
    public String getCreatorName() { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
}