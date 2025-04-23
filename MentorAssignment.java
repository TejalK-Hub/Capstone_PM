package model;

import java.sql.Timestamp;

/**
 * Mentor Assignment model class
 */
public class MentorAssignment {
    private int assignmentId;
    private int mentorId;
    private int studentId;
    private int proposalId;
    private Timestamp assignedDate;
    private String status;
    private Mentor mentor; // Reference to associated mentor
    private Student student; // Reference to associated student
    private ProjectProposal proposal; // Reference to associated proposal
    
    // Default constructor
    public MentorAssignment() {
    }
    
    // Constructor with fields
    public MentorAssignment(int assignmentId, int mentorId, int studentId, int proposalId, 
                           Timestamp assignedDate, String status) {
        this.assignmentId = assignmentId;
        this.mentorId = mentorId;
        this.studentId = studentId;
        this.proposalId = proposalId;
        this.assignedDate = assignedDate;
        this.status = status;
    }
    
    // Getters and Setters
    public int getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public int getMentorId() {
        return mentorId;
    }
    
    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getProposalId() {
        return proposalId;
    }
    
    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }
    
    public Timestamp getAssignedDate() {
        return assignedDate;
    }
    
    public void setAssignedDate(Timestamp assignedDate) {
        this.assignedDate = assignedDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Mentor getMentor() {
        return mentor;
    }
    
    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    public ProjectProposal getProposal() {
        return proposal;
    }
    
    public void setProposal(ProjectProposal proposal) {
        this.proposal = proposal;
    }
    
    @Override
    public String toString() {
        return "MentorAssignment [assignmentId=" + assignmentId + ", mentorId=" + mentorId + ", studentId=" + studentId
                + ", proposalId=" + proposalId + ", status=" + status + "]";
    }
}
