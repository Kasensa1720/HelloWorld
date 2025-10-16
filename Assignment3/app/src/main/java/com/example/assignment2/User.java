package com.example.assignment2;
public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String password;
    private boolean newsletter;

    // Constructor
    public User(String userId, String name, String email, String phone, String gender, String password, boolean newsletter) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.newsletter = newsletter;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getGender() { return gender; }
    public String getPassword() { return password; }
    public boolean isNewsletter() { return newsletter; }
}

//commit made to git lab 1