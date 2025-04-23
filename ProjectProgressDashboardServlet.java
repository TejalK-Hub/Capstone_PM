package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MentorAssignment;
import model.ProgressUpdate;
import service.ProjectService;
import service.ProgressService;

/**
 * Servlet for handling project progress dashboard
 */
@WebServlet("/admin/project-progress")
public class ProjectProgressDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private ProgressService progressService;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        progressService = new ProgressService();
    }
    
    /**
     * Handle GET requests - display progress dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get all active assignments
        List<MentorAssignment> assignments = projectService.getAssignmentsByStatus("accepted");
        
        // Create a map of assignment ID to progress updates
        Map<Integer, List<ProgressUpdate>> progressMap = new HashMap<>();
        
        // Get progress updates for each assignment
        for (MentorAssignment assignment : assignments) {
            List<ProgressUpdate> updates = progressService.getUpdatesByAssignmentId(assignment.getAssignmentId());
            progressMap.put(assignment.getAssignmentId(), updates);
        }
        
        // Set attributes for the view
        request.setAttribute("assignments", assignments);
        request.setAttribute("progressMap", progressMap);
        
        // Forward to progress dashboard page
        request.getRequestDispatcher("/WEB-INF/views/admin/project-progress.jsp").forward(request, response);
    }
}
