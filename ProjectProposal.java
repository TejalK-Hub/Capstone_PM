package model;

import java.sql.Timestamp;

/**
 * Project Proposal model class
 */
public class ProjectProposal {
    private int proposalId;
    private int studentId;
    private String title;
    private String description;
    private String objectives;
    private String technologies;
    private String status;
    private Timestamp submissionDate;
    private Timestamp lastUpdated;
    private Student student; // Reference to associated student
    
    // Default constructor
    public ProjectProposal() {
    }
    
    // Constructor with fields
    public ProjectProposal(int proposalId, int studentId, String title, String description, String objectives,
                          String technologies, String status, Timestamp submissionDate, Timestamp lastUpdated) {
        this.proposalId = proposalId;
        this.studentId = studentId;
        this.title = title;
        this.description = description;
        this.objectives = objectives;
        this.technologies = technologies;
        this.status = status;
        this.submissionDate = submissionDate;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters and Setters
    public int getProposalId() {
        return proposalId;
    }
    
    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
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
    
    public String getObjectives() {
        return objectives;
    }
    
    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }
    
    public String getTechnologies() {
        return technologies;
    }
    
    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getSubmissionDate() {
        return submissionDate;
    }
    
    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    public Timestamp getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    @Override
    public String toString() {
        return "ProjectProposal [proposalId=" + proposalId + ", studentId=" + studentId + ", title=" + title
                + ", status=" + status + "]";
    }
}
