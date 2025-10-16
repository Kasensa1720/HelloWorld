package com.example.assignment2;

// Helper class for faculty spinner
class FacultySpinnerItem {
    private String id;
    private String name;

    public FacultySpinnerItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
