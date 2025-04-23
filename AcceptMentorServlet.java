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
import model.MentorAssignment;
import service.ProjectService;
import dao.StudentDAO;
import dao.MentorAssignmentDAO;

/**
 * Servlet for handling mentor assignment acceptance
 */
@WebServlet("/student/accept-mentor")
public class AcceptMentorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private StudentDAO studentDAO;
    private MentorAssignmentDAO assignmentDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        studentDAO = new StudentDAO();
        assignmentDAO = new MentorAssignmentDAO();
    }
    
    /**
     * Handle GET requests - process mentor acceptance
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
        
        // Get assignment ID from request
        String assignmentIdStr = request.getParameter("assignmentId");
        
        if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
            // No assignment ID provided
            session.setAttribute("error", "No assignment selected");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
            return;
        }
        
        try {
            int assignmentId = Integer.parseInt(assignmentIdStr);
            MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
            
            if (assignment == null || assignment.getStudentId() != student.getStudentId()) {
                // Invalid assignment
                session.setAttribute("error", "Invalid assignment");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
                return;
            }
            
            // Accept the assignment
            boolean accepted = projectService.acceptAssignment(assignmentId);
            
            if (accepted) {
                session.setAttribute("message", "Mentor assignment accepted successfully");
            } else {
                session.setAttribute("error", "Failed to accept mentor assignment");
            }
        } catch (NumberFormatException e) {
            // Invalid assignment ID
            session.setAttribute("error", "Invalid assignment ID");
        }
        
        // Redirect to student dashboard
        response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
    }
}
