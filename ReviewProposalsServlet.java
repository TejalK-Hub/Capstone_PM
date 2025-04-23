package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ProjectProposal;
import service.ProjectService;

/**
 * Servlet for handling proposal review by admin
 */
@WebServlet("/admin/review-proposals")
public class ReviewProposalsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
    }
    
    /**
     * Handle GET requests - display list of proposals
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get status filter from request
        String status = request.getParameter("status");
        
        List<ProjectProposal> proposals;
        
        if (status != null && !status.trim().isEmpty()) {
            // Filter by status
            proposals = projectService.getProposalsByStatus(status);
            request.setAttribute("currentStatus", status);
        } else {
            // Show all proposals
            proposals = projectService.getAllProposals();
        }
        
        // Set attributes for the view
        request.setAttribute("proposals", proposals);
        
        // Forward to proposals page
        request.getRequestDispatcher("/WEB-INF/views/admin/review-proposals.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process proposal approval/rejection
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get session
        HttpSession session = request.getSession();
        
        // Get form parameters
        String proposalIdStr = request.getParameter("proposalId");
        String action = request.getParameter("action");
        
        if (proposalIdStr == null || proposalIdStr.trim().isEmpty() || action == null || action.trim().isEmpty()) {
            // Missing parameters
            session.setAttribute("error", "Missing parameters");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
            return;
        }
        
        try {
            int proposalId = Integer.parseInt(proposalIdStr);
            
            boolean success = false;
            String message = "";
            
            if (action.equals("approve")) {
                // Approve proposal
                success = projectService.approveProposal(proposalId);
                message = "Proposal approved successfully";
            } else if (action.equals("reject")) {
                // Reject proposal
                success = projectService.rejectProposal(proposalId);
                message = "Proposal rejected successfully";
            }
            
            if (success) {
                session.setAttribute("message", message);
            } else {
                session.setAttribute("error", "Failed to process proposal");
            }
        } catch (NumberFormatException e) {
            // Invalid proposal ID
            session.setAttribute("error", "Invalid proposal ID");
        }
        
        // Redirect back to review page
        response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
    }
}
