package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.ProjectProposal;
import model.MentorAssignment;
import model.User;
import service.ProjectService;
import dao.UserDAO;

/**
 * Servlet for handling admin dashboard
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private UserDAO userDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        userDAO = new UserDAO();
    }
    
    /**
     * Handle GET requests - display admin dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get counts for dashboard
        List<ProjectProposal> pendingProposals = projectService.getProposalsByStatus("pending");
        List<ProjectProposal> approvedProposals = projectService.getProposalsByStatus("approved");
        List<ProjectProposal> inProgressProposals = projectService.getProposalsByStatus("in_progress");
        List<ProjectProposal> completedProposals = projectService.getProposalsByStatus("completed");
        
        List<MentorAssignment> pendingAssignments = projectService.getAssignmentsByStatus("pending");
        List<MentorAssignment> acceptedAssignments = projectService.getAssignmentsByStatus("accepted");
        List<MentorAssignment> completedAssignments = projectService.getAssignmentsByStatus("completed");
        
        List<User> students = userDAO.getUsersByRole("student");
        List<User> mentors = userDAO.getUsersByRole("mentor");
        
        // Set attributes for the view
        request.setAttribute("pendingProposals", pendingProposals);
        request.setAttribute("approvedProposals", approvedProposals);
        request.setAttribute("inProgressProposals", inProgressProposals);
        request.setAttribute("completedProposals", completedProposals);
        
        request.setAttribute("pendingAssignments", pendingAssignments);
        request.setAttribute("acceptedAssignments", acceptedAssignments);
        request.setAttribute("completedAssignments", completedAssignments);
        
        request.setAttribute("students", students);
        request.setAttribute("mentors", mentors);
        
        request.setAttribute("pendingProposalCount", pendingProposals.size());
        request.setAttribute("approvedProposalCount", approvedProposals.size());
        request.setAttribute("inProgressProposalCount", inProgressProposals.size());
        request.setAttribute("completedProposalCount", completedProposals.size());
        
        request.setAttribute("pendingAssignmentCount", pendingAssignments.size());
        request.setAttribute("acceptedAssignmentCount", acceptedAssignments.size());
        request.setAttribute("completedAssignmentCount", completedAssignments.size());
        
        request.setAttribute("studentCount", students.size());
        request.setAttribute("mentorCount", mentors.size());
        
        // Forward to dashboard page
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
