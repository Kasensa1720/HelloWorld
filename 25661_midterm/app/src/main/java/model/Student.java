// model/Student.java
package model;

public class Student {
    private String studentId;
    private String name;
    private String gender;
    private String email;
    private String phone;

    public Student() {}

    public Student(String studentId, String name, String gender, String email, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    // Getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}