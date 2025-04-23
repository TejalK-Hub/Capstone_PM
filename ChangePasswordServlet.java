package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import service.AuthService;

/**
 * Servlet for handling password changes
 */
@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
    }
    
    /**
     * Handle POST requests - process password change
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            // User not logged in
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/login"));
            return;
        }
        
        // Get form data
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate passwords
        if (currentPassword == null || newPassword == null || confirmPassword == null ||
            currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            session.setAttribute("error", "All password fields are required");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/profile"));
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("error", "New password and confirmation do not match");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/profile"));
            return;
        }
        
        // Update password
        boolean passwordUpdated = authService.updatePassword(user.getUserId(), currentPassword, newPassword);
        
        if (passwordUpdated) {
            session.setAttribute("message", "Password updated successfully");
        } else {
            session.setAttribute("error", "Failed to update password. Please check your current password.");
        }
        
        response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/profile"));
    }
}
