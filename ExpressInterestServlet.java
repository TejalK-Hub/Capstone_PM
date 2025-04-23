package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.Mentor;
import model.ProjectProposal;
import model.MentorInterest;
import service.ProjectService;
import dao.MentorDAO;

/**
 * Servlet for handling expression of interest in a proposal
 */
@WebServlet("/mentor/express-interest")
public class ExpressInterestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private MentorDAO mentorDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        mentorDAO = new MentorDAO();
    }
    
    /**
     * Handle GET requests - display interest form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get mentor profile
        Mentor mentor = mentorDAO.getMentorByUserId(user.getUserId());
        
        if (mentor == null) {
            // Mentor profile not found
            session.setAttribute("error", "Mentor profile not found");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/dashboard"));
            return;
        }
        
        // Get proposal ID from request
        String proposalIdStr = request.getParameter("proposalId");
        
        if (proposalIdStr == null || proposalIdStr.trim().isEmpty()) {
            // No proposal ID provided
            session.setAttribute("error", "No proposal selected");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/browse-proposals"));
            return;
        }
        
        try {
            int proposalId = Integer.parseInt(proposalIdStr);
            ProjectProposal proposal = projectService.getProposalById(proposalId);
            
            if (proposal == null) {
                // Invalid proposal
                session.setAttribute("error", "Invalid proposal");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/browse-proposals"));
                return;
            }
            
            // Check if mentor has already expressed interest
            MentorInterest existingInterest = projectService.getInterestByMentorAndProposal(mentor.getMentorId(), proposalId);
            
            // Set attributes for the view
            request.setAttribute("mentor", mentor);
            request.setAttribute("proposal", proposal);
            request.setAttribute("interest", existingInterest);
            
            // Forward to interest form
            request.getRequestDispatcher("/WEB-INF/views/mentor/express-interest.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Invalid proposal ID
            session.setAttribute("error", "Invalid proposal ID");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/browse-proposals"));
        }
    }
    
    /**
     * Handle POST requests - process interest form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get mentor profile
        Mentor mentor = mentorDAO.getMentorByUserId(user.getUserId());
        
        if (mentor == null) {
            // Mentor profile not found
            session.setAttribute("error", "Mentor profile not found");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/dashboard"));
            return;
        }
        
        // Get form parameters
        String proposalIdStr = request.getParameter("proposalId");
        String interestLevel = request.getParameter("interestLevel");
        String comments = request.getParameter("comments");
        
        // Validate input
        boolean hasError = false;
        
        if (proposalIdStr == null || proposalIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Proposal ID is required");
            hasError = true;
        }
        
        if (interestLevel == null || interestLevel.trim().isEmpty() || 
            (!interestLevel.equals("high") && !interestLevel.equals("medium") && !interestLevel.equals("low"))) {
            request.setAttribute("interestLevelError", "Valid interest level is required");
            hasError = true;
        }
        
        if (hasError) {
            // Preserve entered values
            request.setAttribute("comments", comments);
            request.setAttribute("interestLevel", interestLevel);
            
            // Get proposal for the form
            try {
                int proposalId = Integer.parseInt(proposalIdStr);
                ProjectProposal proposal = projectService.getProposalById(proposalId);
                request.setAttribute("proposal", proposal);
            } catch (NumberFormatException e) {
                // Invalid proposal ID
            }
            
            // Forward back to form with errors
            request.getRequestDispatcher("/WEB-INF/views/mentor/express-interest.jsp").forward(request, response);
            return;
        }
        
        try {
            int proposalId = Integer.parseInt(proposalIdStr);
            ProjectProposal proposal = projectService.getProposalById(proposalId);
            
            if (proposal == null) {
                // Invalid proposal
                session.setAttribute("error", "Invalid proposal");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/browse-proposals"));
                return;
            }
            
            // Express interest
            MentorInterest interest = projectService.expressInterest(mentor.getMentorId(), proposalId, interestLevel, comments);
            
            if (interest != null) {
                session.setAttribute("message", "Interest expressed successfully");
            } else {
                session.setAttribute("error", "Failed to express interest");
            }
            
            // Redirect to mentor dashboard
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/dashboard"));
        } catch (NumberFormatException e) {
            // Invalid proposal ID
            session.setAttribute("error", "Invalid proposal ID");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/browse-proposals"));
        }
    }
}
