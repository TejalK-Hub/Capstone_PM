package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.Student;
import model.ProjectProposal;
import service.ProjectService;
import dao.StudentDAO;

/**
 * Servlet for handling project proposal submission
 */
@WebServlet("/student/proposal")
public class ProjectProposalServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private StudentDAO studentDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        studentDAO = new StudentDAO();
    }
    
    /**
     * Handle GET requests - display proposal form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Check if editing an existing proposal
        String proposalIdStr = request.getParameter("id");
        
        if (proposalIdStr != null && !proposalIdStr.trim().isEmpty()) {
            try {
                int proposalId = Integer.parseInt(proposalIdStr);
                ProjectProposal proposal = projectService.getProposalById(proposalId);
                
                if (proposal != null) {
                    // Set proposal for editing
                    request.setAttribute("proposal", proposal);
                    request.setAttribute("editing", true);
                }
            } catch (NumberFormatException e) {
                // Invalid proposal ID, ignore
            }
        }
        
        // Forward to proposal form
        request.getRequestDispatcher("/WEB-INF/views/student/proposal-form.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process proposal form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get student profile
        Student student = studentDAO.getStudentByUserId(user.getUserId());
        
        if (student == null) {
            // Student profile not found
            session.setAttribute("error", "Student profile not found");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
            return;
        }
        
        // Get form parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String objectives = request.getParameter("objectives");
        String technologies = request.getParameter("technologies");
        String proposalIdStr = request.getParameter("proposalId");
        
        // Validate input
        boolean hasError = false;
        
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("titleError", "Title is required");
            hasError = true;
        }
        
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("descriptionError", "Description is required");
            hasError = true;
        }
        
        if (hasError) {
            // Preserve entered values
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("objectives", objectives);
            request.setAttribute("technologies", technologies);
            
            // Forward back to proposal form with errors
            request.getRequestDispatcher("/WEB-INF/views/student/proposal-form.jsp").forward(request, response);
            return;
        }
        
        // Check if editing an existing proposal
        if (proposalIdStr != null && !proposalIdStr.trim().isEmpty()) {
            try {
                int proposalId = Integer.parseInt(proposalIdStr);
                ProjectProposal proposal = projectService.getProposalById(proposalId);
                
                if (proposal != null && proposal.getStudentId() == student.getStudentId()) {
                    // Update existing proposal
                    proposal.setTitle(title);
                    proposal.setDescription(description);
                    proposal.setObjectives(objectives);
                    proposal.setTechnologies(technologies);
                    
                    boolean updated = projectService.updateProposal(proposal);
                    
                    if (updated) {
                        session.setAttribute("message", "Proposal updated successfully");
                    } else {
                        session.setAttribute("error", "Failed to update proposal");
                    }
                } else {
                    session.setAttribute("error", "Invalid proposal");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid proposal ID");
            }
        } else {
            // Create new proposal
            ProjectProposal proposal = new ProjectProposal();
            proposal.setStudentId(student.getStudentId());
            proposal.setTitle(title);
            proposal.setDescription(description);
            proposal.setObjectives(objectives);
            proposal.setTechnologies(technologies);
            
            ProjectProposal createdProposal = projectService.createProposal(proposal);
            
            if (createdProposal != null) {
                session.setAttribute("message", "Proposal submitted successfully");
            } else {
                session.setAttribute("error", "Failed to submit proposal");
            }
        }
        
        // Redirect to student dashboard
        response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
    }
}
