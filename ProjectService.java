package service;

import java.util.List;

import dao.ProjectProposalDAO;
import dao.MentorInterestDAO;
import dao.MentorAssignmentDAO;
import dao.StudentDAO;
import model.ProjectProposal;
import model.MentorInterest;
import model.MentorAssignment;
import model.Student;

/**
 * Service class for project proposal management
 */
public class ProjectService {
    private ProjectProposalDAO proposalDAO;
    private MentorInterestDAO interestDAO;
    private MentorAssignmentDAO assignmentDAO;
    private StudentDAO studentDAO;

    /**
     * Constructor
     */
    public ProjectService() {
        proposalDAO = new ProjectProposalDAO();
        interestDAO = new MentorInterestDAO();
        assignmentDAO = new MentorAssignmentDAO();
        studentDAO = new StudentDAO();
    }

    /**
     * Get a project proposal by ID
     * @param proposalId Proposal ID
     * @return ProjectProposal object
     */
    public ProjectProposal getProposalById(int proposalId) {
        return proposalDAO.getProposalById(proposalId);
    }

    /**
     * Get all project proposals
     * @return List of all project proposals
     */
    public List<ProjectProposal> getAllProposals() {
        return proposalDAO.getAllProposals();
    }

    /**
     * Get project proposals by student ID
     * @param studentId Student ID
     * @return List of project proposals for the student
     */
    public List<ProjectProposal> getProposalsByStudentId(int studentId) {
        return proposalDAO.getProposalsByStudentId(studentId);
    }

    /**
     * Get project proposals by user ID
     * @param userId User ID
     * @return List of project proposals for the user
     */
    public List<ProjectProposal> getProposalsByUserId(int userId) {
        Student student = studentDAO.getStudentByUserId(userId);
        if (student != null) {
            return proposalDAO.getProposalsByStudentId(student.getStudentId());
        }
        return null;
    }

    /**
     * Get project proposals by status
     * @param status Proposal status
     * @return List of project proposals with the specified status
     */
    public List<ProjectProposal> getProposalsByStatus(String status) {
        return proposalDAO.getProposalsByStatus(status);
    }

    /**
     * Create a new project proposal
     * @param proposal ProjectProposal object to create
     * @return Created proposal with ID set
     */
    public ProjectProposal createProposal(ProjectProposal proposal) {
        // Set initial status to pending
        proposal.setStatus("pending");
        return proposalDAO.createProposal(proposal);
    }

    /**
     * Update an existing project proposal
     * @param proposal ProjectProposal object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateProposal(ProjectProposal proposal) {
        return proposalDAO.updateProposal(proposal);
    }

    /**
     * Delete a project proposal
     * @param proposalId ID of the proposal to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProposal(int proposalId) {
        return proposalDAO.deleteProposal(proposalId);
    }

    /**
     * Approve a project proposal
     * @param proposalId ID of the proposal to approve
     * @return true if approval was successful, false otherwise
     */
    public boolean approveProposal(int proposalId) {
        ProjectProposal proposal = proposalDAO.getProposalById(proposalId);
        if (proposal != null) {
            proposal.setStatus("approved");
            return proposalDAO.updateProposal(proposal);
        }
        return false;
    }

    /**
     * Reject a project proposal
     * @param proposalId ID of the proposal to reject
     * @return true if rejection was successful, false otherwise
     */
    public boolean rejectProposal(int proposalId) {
        ProjectProposal proposal = proposalDAO.getProposalById(proposalId);
        if (proposal != null) {
            proposal.setStatus("rejected");
            return proposalDAO.updateProposal(proposal);
        }
        return false;
    }

    /**
     * Express interest in a project proposal
     * @param mentorId Mentor ID
     * @param proposalId Proposal ID
     * @param interestLevel Interest level (high, medium, low)
     * @param comments Comments
     * @return Created MentorInterest object
     */
    public MentorInterest expressInterest(int mentorId, int proposalId, String interestLevel, String comments) {
        // Check if interest already exists
        MentorInterest existingInterest = interestDAO.getInterestByMentorAndProposal(mentorId, proposalId);

        if (existingInterest != null) {
            // Update existing interest
            existingInterest.setInterestLevel(interestLevel);
            existingInterest.setComments(comments);
            interestDAO.updateInterest(existingInterest);
            return existingInterest;
        } else {
            // Create new interest
            MentorInterest interest = new MentorInterest();
            interest.setMentorId(mentorId);
            interest.setProposalId(proposalId);
            interest.setInterestLevel(interestLevel);
            interest.setComments(comments);
            return interestDAO.createInterest(interest);
        }
    }

    /**
     * Get mentor interests for a proposal
     * @param proposalId Proposal ID
     * @return List of mentor interests for the proposal
     */
    public List<MentorInterest> getInterestsForProposal(int proposalId) {
        return interestDAO.getInterestsByProposalId(proposalId);
    }

    /**
     * Get interests expressed by a mentor
     * @param mentorId Mentor ID
     * @return List of interests expressed by the mentor
     */
    public List<MentorInterest> getInterestsForMentor(int mentorId) {
        return interestDAO.getInterestsByMentorId(mentorId);
    }

    /**
     * Get interest by mentor ID and proposal ID
     * @param mentorId Mentor ID
     * @param proposalId Proposal ID
     * @return MentorInterest object or null if not found
     */
    public MentorInterest getInterestByMentorAndProposal(int mentorId, int proposalId) {
        return interestDAO.getInterestByMentorAndProposal(mentorId, proposalId);
    }

    /**
     * Assign a mentor to a project
     * @param mentorId Mentor ID
     * @param studentId Student ID
     * @param proposalId Proposal ID
     * @return Created MentorAssignment object
     */
    public MentorAssignment assignMentor(int mentorId, int studentId, int proposalId) {
        // Check if assignment already exists
        MentorAssignment existingAssignment = assignmentDAO.getAssignmentByProposalId(proposalId);

        if (existingAssignment != null) {
            // Update existing assignment
            existingAssignment.setMentorId(mentorId);
            existingAssignment.setStatus("pending");
            assignmentDAO.updateAssignment(existingAssignment);
            return existingAssignment;
        } else {
            // Create new assignment
            MentorAssignment assignment = new MentorAssignment();
            assignment.setMentorId(mentorId);
            assignment.setStudentId(studentId);
            assignment.setProposalId(proposalId);
            assignment.setStatus("pending");

            // Update proposal status
            ProjectProposal proposal = proposalDAO.getProposalById(proposalId);
            if (proposal != null) {
                proposal.setStatus("in_progress");
                proposalDAO.updateProposal(proposal);
            }

            return assignmentDAO.createAssignment(assignment);
        }
    }

    /**
     * Accept a mentor assignment
     * @param assignmentId Assignment ID
     * @return true if acceptance was successful, false otherwise
     */
    public boolean acceptAssignment(int assignmentId) {
        MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        if (assignment != null) {
            assignment.setStatus("accepted");
            return assignmentDAO.updateAssignment(assignment);
        }
        return false;
    }

    /**
     * Complete a project
     * @param assignmentId Assignment ID
     * @return true if completion was successful, false otherwise
     */
    public boolean completeProject(int assignmentId) {
        MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        if (assignment != null) {
            assignment.setStatus("completed");
            assignmentDAO.updateAssignment(assignment);

            // Update proposal status
            ProjectProposal proposal = proposalDAO.getProposalById(assignment.getProposalId());
            if (proposal != null) {
                proposal.setStatus("completed");
                return proposalDAO.updateProposal(proposal);
            }
        }
        return false;
    }

    /**
     * Get assignments by mentor ID
     * @param mentorId Mentor ID
     * @return List of assignments for the mentor
     */
    public List<MentorAssignment> getAssignmentsByMentorId(int mentorId) {
        return assignmentDAO.getAssignmentsByMentorId(mentorId);
    }

    /**
     * Get assignments by student ID
     * @param studentId Student ID
     * @return List of assignments for the student
     */
    public List<MentorAssignment> getAssignmentsByStudentId(int studentId) {
        return assignmentDAO.getAssignmentsByStudentId(studentId);
    }

    /**
     * Get assignments by status
     * @param status Assignment status
     * @return List of assignments with the specified status
     */
    public List<MentorAssignment> getAssignmentsByStatus(String status) {
        return assignmentDAO.getAssignmentsByStatus(status);
    }
}
