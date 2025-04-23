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
import model.MentorAssignment;
import service.ProjectService;
import dao.StudentDAO;

/**
 * Servlet for handling student dashboard
 */
@WebServlet("/student/dashboard")
public class StudentDashboardServlet extends HttpServlet {
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
     * Handle GET requests - display student dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get student profile
        Student student = studentDAO.getStudentByUserId(user.getUserId());
        
        if (student != null) {
            // Get student's project proposals
            List<ProjectProposal> proposals = projectService.getProposalsByStudentId(student.getStudentId());
            
            // Get student's mentor assignments
            List<MentorAssignment> assignments = projectService.getAssignmentsByStudentId(student.getStudentId());
            
            // Set attributes for the view
            request.setAttribute("student", student);
            request.setAttribute("proposals", proposals);
            request.setAttribute("assignments", assignments);
        }
        
        // Forward to dashboard page
        request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
    }
}
