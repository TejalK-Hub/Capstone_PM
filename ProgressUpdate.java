package model;

import java.sql.Timestamp;

/**
 * Progress Update model class
 */
public class ProgressUpdate {
    private int updateId;
    private int assignmentId;
    private String title;
    private String description;
    private String updateType;
    private int createdBy;
    private Timestamp createdAt;
    private MentorAssignment assignment; // Reference to associated assignment
    private User creator; // Reference to user who created the update
    
    // Default constructor
    public ProgressUpdate() {
    }
    
    // Constructor with fields
    public ProgressUpdate(int updateId, int assignmentId, String title, String description, 
                         String updateType, int createdBy, Timestamp createdAt) {
        this.updateId = updateId;
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
        this.updateType = updateType;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getUpdateId() {
        return updateId;
    }
    
    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }
    
    public int getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUpdateType() {
        return updateType;
    }
    
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
    
    public int getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public MentorAssignment getAssignment() {
        return assignment;
    }
    
    public void setAssignment(MentorAssignment assignment) {
        this.assignment = assignment;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    @Override
    public String toString() {
        return "ProgressUpdate [updateId=" + updateId + ", assignmentId=" + assignmentId + ", title=" + title
                + ", updateType=" + updateType + ", createdAt=" + createdAt + "]";
    }
}
