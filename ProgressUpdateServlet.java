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
import model.MentorAssignment;
import model.ProgressUpdate;
import service.ProgressService;
import dao.MentorAssignmentDAO;

/**
 * Servlet for handling progress updates
 */
@WebServlet("/progress-update")
public class ProgressUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProgressService progressService;
    private MentorAssignmentDAO assignmentDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        progressService = new ProgressService();
        assignmentDAO = new MentorAssignmentDAO();
    }
    
    /**
     * Handle GET requests - display progress update form or list
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get assignment ID from request
        String assignmentIdStr = request.getParameter("assignmentId");
        String action = request.getParameter("action");
        
        if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
            // No assignment ID provided
            session.setAttribute("error", "No assignment selected");
            redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
            return;
        }
        
        try {
            int assignmentId = Integer.parseInt(assignmentIdStr);
            MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
            
            if (assignment == null) {
                // Invalid assignment
                session.setAttribute("error", "Invalid assignment");
                redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
                return;
            }
            
            // Check if user is authorized to access this assignment
            boolean isAuthorized = false;
            
            if (user.getRole().equals("student") && assignment.getStudentId() == assignment.getStudent().getUserId()) {
                isAuthorized = true;
            } else if (user.getRole().equals("mentor") && assignment.getMentorId() == assignment.getMentor().getUserId()) {
                isAuthorized = true;
            } else if (user.getRole().equals("admin")) {
                isAuthorized = true;
            }
            
            if (!isAuthorized) {
                // User is not authorized
                session.setAttribute("error", "You are not authorized to access this assignment");
                redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
                return;
            }
            
            // Set assignment for the view
            request.setAttribute("assignment", assignment);
            
            if ("new".equals(action)) {
                // Display form for new progress update
                request.getRequestDispatcher("/WEB-INF/views/progress-update-form.jsp").forward(request, response);
            } else {
                // Display list of progress updates
                List<ProgressUpdate> updates = progressService.getUpdatesByAssignmentId(assignmentId);
                request.setAttribute("updates", updates);
                request.getRequestDispatcher("/WEB-INF/views/progress-updates.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            // Invalid assignment ID
            session.setAttribute("error", "Invalid assignment ID");
            redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
        }
    }
    
    /**
     * Handle POST requests - process progress update form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get form parameters
        String assignmentIdStr = request.getParameter("assignmentId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String updateType = request.getParameter("updateType");
        
        // Validate input
        boolean hasError = false;
        
        if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Assignment ID is required");
            hasError = true;
        }
        
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("titleError", "Title is required");
            hasError = true;
        }
        
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("descriptionError", "Description is required");
            hasError = true;
        }
        
        if (updateType == null || updateType.trim().isEmpty() || 
            (!updateType.equals("milestone") && !updateType.equals("status_update") && !updateType.equals("issue"))) {
            request.setAttribute("updateTypeError", "Valid update type is required");
            hasError = true;
        }
        
        if (hasError) {
            // Preserve entered values
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("updateType", updateType);
            
            // Get assignment for the form
            try {
                int assignmentId = Integer.parseInt(assignmentIdStr);
                MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
                request.setAttribute("assignment", assignment);
            } catch (NumberFormatException e) {
                // Invalid assignment ID
            }
            
            // Forward back to form with errors
            request.getRequestDispatcher("/WEB-INF/views/progress-update-form.jsp").forward(request, response);
            return;
        }
        
        try {
            int assignmentId = Integer.parseInt(assignmentIdStr);
            MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
            
            if (assignment == null) {
                // Invalid assignment
                session.setAttribute("error", "Invalid assignment");
                redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
                return;
            }
            
            // Check if user is authorized to access this assignment
            boolean isAuthorized = false;
            
            if (user.getRole().equals("student") && assignment.getStudentId() == assignment.getStudent().getUserId()) {
                isAuthorized = true;
            } else if (user.getRole().equals("mentor") && assignment.getMentorId() == assignment.getMentor().getUserId()) {
                isAuthorized = true;
            } else if (user.getRole().equals("admin")) {
                isAuthorized = true;
            }
            
            if (!isAuthorized) {
                // User is not authorized
                session.setAttribute("error", "You are not authorized to access this assignment");
                redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
                return;
            }
            
            // Create progress update
            ProgressUpdate update = new ProgressUpdate();
            update.setAssignmentId(assignmentId);
            update.setTitle(title);
            update.setDescription(description);
            update.setUpdateType(updateType);
            update.setCreatedBy(user.getUserId());
            
            ProgressUpdate createdUpdate = progressService.createUpdate(update);
            
            if (createdUpdate != null) {
                session.setAttribute("message", "Progress update logged successfully");
            } else {
                session.setAttribute("error", "Failed to log progress update");
            }
            
            // Redirect to progress updates list
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + 
                    "/progress-update?assignmentId=" + assignmentId));
        } catch (NumberFormatException e) {
            // Invalid assignment ID
            session.setAttribute("error", "Invalid assignment ID");
            redirectToDashboard(response, user.getRole(), getServletContext().getContextPath());
        }
    }
    
    /**
     * Redirect to appropriate dashboard based on user role
     * @param response HTTP response
     * @param role User role
     * @param contextPath Context path
     * @throws IOException if an I/O error occurs
     */
    private void redirectToDashboard(HttpServletResponse response, String role, String contextPath) throws IOException {
        switch (role) {
            case "student":
                response.sendRedirect(response.encodeRedirectURL(contextPath + "/student/dashboard"));
                break;
            case "mentor":
                response.sendRedirect(response.encodeRedirectURL(contextPath + "/mentor/dashboard"));
                break;
            case "admin":
                response.sendRedirect(response.encodeRedirectURL(contextPath + "/admin/dashboard"));
                break;
            default:
                response.sendRedirect(response.encodeRedirectURL(contextPath + "/login"));
                break;
        }
    }
}
