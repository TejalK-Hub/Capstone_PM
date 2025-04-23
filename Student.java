package model;

/**
 * Student model class
 */
public class Student {
    private int studentId;
    private int userId;
    private String studentNumber;
    private String major;
    private int graduationYear;
    private User user; // Reference to associated user
    
    // Default constructor
    public Student() {
    }
    
    // Constructor with fields
    public Student(int studentId, int userId, String studentNumber, String major, int graduationYear) {
        this.studentId = studentId;
        this.userId = userId;
        this.studentNumber = studentNumber;
        this.major = major;
        this.graduationYear = graduationYear;
    }
    
    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public int getGraduationYear() {
        return graduationYear;
    }
    
    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", userId=" + userId + ", studentNumber=" + studentNumber
                + ", major=" + major + ", graduationYear=" + graduationYear + "]";
    }
}
