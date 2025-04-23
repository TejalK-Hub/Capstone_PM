package model;

/**
 * Mentor model class
 */
public class Mentor {
    private int mentorId;
    private int userId;
    private String department;
    private String specialization;
    private int maxMentees;
    private User user; // Reference to associated user
    
    // Default constructor
    public Mentor() {
    }
    
    // Constructor with fields
    public Mentor(int mentorId, int userId, String department, String specialization, int maxMentees) {
        this.mentorId = mentorId;
        this.userId = userId;
        this.department = department;
        this.specialization = specialization;
        this.maxMentees = maxMentees;
    }
    
    // Getters and Setters
    public int getMentorId() {
        return mentorId;
    }
    
    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public int getMaxMentees() {
        return maxMentees;
    }
    
    public void setMaxMentees(int maxMentees) {
        this.maxMentees = maxMentees;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "Mentor [mentorId=" + mentorId + ", userId=" + userId + ", department=" + department
                + ", specialization=" + specialization + ", maxMentees=" + maxMentees + "]";
    }
}
