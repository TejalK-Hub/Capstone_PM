package model;

import java.sql.Timestamp;

/**
 * Mentor Interest model class
 */
public class MentorInterest {
    private int interestId;
    private int mentorId;
    private int proposalId;
    private String interestLevel;
    private String comments;
    private Timestamp createdAt;
    private Mentor mentor; // Reference to associated mentor
    private ProjectProposal proposal; // Reference to associated proposal
    
    // Default constructor
    public MentorInterest() {
    }
    
    // Constructor with fields
    public MentorInterest(int interestId, int mentorId, int proposalId, String interestLevel, 
                         String comments, Timestamp createdAt) {
        this.interestId = interestId;
        this.mentorId = mentorId;
        this.proposalId = proposalId;
        this.interestLevel = interestLevel;
        this.comments = comments;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getInterestId() {
        return interestId;
    }
    
    public void setInterestId(int interestId) {
        this.interestId = interestId;
    }
    
    public int getMentorId() {
        return mentorId;
    }
    
    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
    
    public int getProposalId() {
        return proposalId;
    }
    
    public void setProposalId(int proposalId) {
        this.proposalId = proposalId;
    }
    
    public String getInterestLevel() {
        return interestLevel;
    }
    
    public void setInterestLevel(String interestLevel) {
        this.interestLevel = interestLevel;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Mentor getMentor() {
        return mentor;
    }
    
    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
    
    public ProjectProposal getProposal() {
        return proposal;
    }
    
    public void setProposal(ProjectProposal proposal) {
        this.proposal = proposal;
    }
    
    @Override
    public String toString() {
        return "MentorInterest [interestId=" + interestId + ", mentorId=" + mentorId + ", proposalId=" + proposalId
                + ", interestLevel=" + interestLevel + "]";
    }
}
