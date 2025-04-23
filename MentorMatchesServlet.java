package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.Student;
import model.ProjectProposal;
import model.MentorInterest;
import service.ProjectService;
import dao.StudentDAO;

/**
 * Servlet for handling mentor matches view
 */
@WebServlet("/student/mentor-matches")
public class MentorMatchesServlet extends HttpServlet {
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
     * Handle GET requests - display mentor matches
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
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
        
        // Get proposal ID from request
        String proposalIdStr = request.getParameter("proposalId");
        
        if (proposalIdStr == null || proposalIdStr.trim().isEmpty()) {
            // No proposal ID provided
            session.setAttribute("error", "No proposal selected");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
            return;
        }
        
        try {
            int proposalId = Integer.parseInt(proposalIdStr);
            ProjectProposal proposal = projectService.getProposalById(proposalId);
            
            if (proposal == null || proposal.getStudentId() != student.getStudentId()) {
                // Invalid proposal
                session.setAttribute("error", "Invalid proposal");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
                return;
            }
            
            // Get mentor interests for the proposal
            List<MentorInterest> interests = projectService.getInterestsForProposal(proposalId);
            
            // Set attributes for the view
            request.setAttribute("proposal", proposal);
            request.setAttribute("interests", interests);
            
            // Forward to mentor matches page
            request.getRequestDispatcher("/WEB-INF/views/student/mentor-matches.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            // Invalid proposal ID
            session.setAttribute("error", "Invalid proposal ID");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
        }
    }
}
