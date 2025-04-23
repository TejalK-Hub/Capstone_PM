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
import model.Mentor;
import model.Student;
import model.MentorInterest;
import service.ProjectService;
import dao.MentorDAO;
import dao.StudentDAO;

/**
 * Servlet for handling manual mentor assignment by admin
 */
@WebServlet("/admin/assign-mentor")
public class AssignMentorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private MentorDAO mentorDAO;
    private StudentDAO studentDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        mentorDAO = new MentorDAO();
        studentDAO = new StudentDAO();
    }
    
    /**
     * Handle GET requests - display assignment form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get proposal ID from request
        String proposalIdStr = request.getParameter("proposalId");
        
        if (proposalIdStr == null || proposalIdStr.trim().isEmpty()) {
            // No proposal ID provided
            HttpSession session = request.getSession();
            session.setAttribute("error", "No proposal selected");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
            return;
        }
        
        try {
            int proposalId = Integer.parseInt(proposalIdStr);
            ProjectProposal proposal = projectService.getProposalById(proposalId);
            
            if (proposal == null) {
                // Invalid proposal
                HttpSession session = request.getSession();
                session.setAttribute("error", "Invalid proposal");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
                return;
            }
            
            // Get student for the proposal
            Student student = studentDAO.getStudentById(proposal.getStudentId());
            
            // Get all mentors
            List<Mentor> mentors = mentorDAO.getAllMentors();
            
            // Get mentor interests for the proposal
            List<MentorInterest> interests = projectService.getInterestsForProposal(proposalId);
            
            // Set attributes for the view
            request.setAttribute("proposal", proposal);
            request.setAttribute("student", student);
            request.setAttribute("mentors", mentors);
            request.setAttribute("interests", interests);
            
            // Forward to assignment form
            request.getRequestDispatcher("/WEB-INF/views/admin/assign-mentor.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Invalid proposal ID
            HttpSession session = request.getSession();
            session.setAttribute("error", "Invalid proposal ID");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
        }
    }
    
    /**
     * Handle POST requests - process assignment form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get session
        HttpSession session = request.getSession();
        
        // Get form parameters
        String proposalIdStr = request.getParameter("proposalId");
        String mentorIdStr = request.getParameter("mentorId");
        
        // Validate input
        if (proposalIdStr == null || proposalIdStr.trim().isEmpty() || 
            mentorIdStr == null || mentorIdStr.trim().isEmpty()) {
            // Missing parameters
            session.setAttribute("error", "Proposal and mentor must be selected");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
            return;
        }
        
        try {
            int proposalId = Integer.parseInt(proposalIdStr);
            int mentorId = Integer.parseInt(mentorIdStr);
            
            ProjectProposal proposal = projectService.getProposalById(proposalId);
            
            if (proposal == null) {
                // Invalid proposal
                session.setAttribute("error", "Invalid proposal");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
                return;
            }
            
            // Assign mentor
            projectService.assignMentor(mentorId, proposal.getStudentId(), proposalId);
            
            session.setAttribute("message", "Mentor assigned successfully");
            
            // Redirect to review page
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
        } catch (NumberFormatException e) {
            // Invalid IDs
            session.setAttribute("error", "Invalid proposal or mentor ID");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/review-proposals"));
        }
    }
}
