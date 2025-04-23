package service;

import java.util.List;

import dao.ProgressUpdateDAO;
import dao.MentorAssignmentDAO;
import model.ProgressUpdate;
import model.MentorAssignment;

/**
 * Service class for progress update management
 */
public class ProgressService {
    private ProgressUpdateDAO updateDAO;
    private MentorAssignmentDAO assignmentDAO;
    
    /**
     * Constructor
     */
    public ProgressService() {
        updateDAO = new ProgressUpdateDAO();
        assignmentDAO = new MentorAssignmentDAO();
    }
    
    /**
     * Get a progress update by ID
     * @param updateId Update ID
     * @return ProgressUpdate object
     */
    public ProgressUpdate getUpdateById(int updateId) {
        return updateDAO.getUpdateById(updateId);
    }
    
    /**
     * Get progress updates by assignment ID
     * @param assignmentId Assignment ID
     * @return List of progress updates for the assignment
     */
    public List<ProgressUpdate> getUpdatesByAssignmentId(int assignmentId) {
        return updateDAO.getUpdatesByAssignmentId(assignmentId);
    }
    
    /**
     * Get progress updates by creator ID
     * @param createdBy User ID of the creator
     * @return List of progress updates created by the user
     */
    public List<ProgressUpdate> getUpdatesByCreator(int createdBy) {
        return updateDAO.getUpdatesByCreator(createdBy);
    }
    
    /**
     * Get progress updates for a project proposal
     * @param proposalId Proposal ID
     * @return List of progress updates for the proposal
     */
    public List<ProgressUpdate> getUpdatesByProposalId(int proposalId) {
        MentorAssignment assignment = assignmentDAO.getAssignmentByProposalId(proposalId);
        if (assignment != null) {
            return updateDAO.getUpdatesByAssignmentId(assignment.getAssignmentId());
        }
        return null;
    }
    
    /**
     * Create a new progress update
     * @param update ProgressUpdate object to create
     * @return Created update with ID set
     */
    public ProgressUpdate createUpdate(ProgressUpdate update) {
        return updateDAO.createUpdate(update);
    }
    
    /**
     * Update an existing progress update
     * @param update ProgressUpdate object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateProgressUpdate(ProgressUpdate update) {
        return updateDAO.updateProgressUpdate(update);
    }
    
    /**
     * Delete a progress update
     * @param updateId ID of the update to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUpdate(int updateId) {
        return updateDAO.deleteUpdate(updateId);
    }
    
    /**
     * Log a milestone
     * @param assignmentId Assignment ID
     * @param title Milestone title
     * @param description Milestone description
     * @param createdBy User ID of the creator
     * @return Created ProgressUpdate object
     */
    public ProgressUpdate logMilestone(int assignmentId, String title, String description, int createdBy) {
        ProgressUpdate update = new ProgressUpdate();
        update.setAssignmentId(assignmentId);
        update.setTitle(title);
        update.setDescription(description);
        update.setUpdateType("milestone");
        update.setCreatedBy(createdBy);
        return updateDAO.createUpdate(update);
    }
    
    /**
     * Log a status update
     * @param assignmentId Assignment ID
     * @param title Status update title
     * @param description Status update description
     * @param createdBy User ID of the creator
     * @return Created ProgressUpdate object
     */
    public ProgressUpdate logStatusUpdate(int assignmentId, String title, String description, int createdBy) {
        ProgressUpdate update = new ProgressUpdate();
        update.setAssignmentId(assignmentId);
        update.setTitle(title);
        update.setDescription(description);
        update.setUpdateType("status_update");
        update.setCreatedBy(createdBy);
        return updateDAO.createUpdate(update);
    }
    
    /**
     * Log an issue
     * @param assignmentId Assignment ID
     * @param title Issue title
     * @param description Issue description
     * @param createdBy User ID of the creator
     * @return Created ProgressUpdate object
     */
    public ProgressUpdate logIssue(int assignmentId, String title, String description, int createdBy) {
        ProgressUpdate update = new ProgressUpdate();
        update.setAssignmentId(assignmentId);
        update.setTitle(title);
        update.setDescription(description);
        update.setUpdateType("issue");
        update.setCreatedBy(createdBy);
        return updateDAO.createUpdate(update);
    }
}
