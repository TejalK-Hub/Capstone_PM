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
 * Servlet for handling mentor registration
 */
@WebServlet("/register/mentor")
public class MentorRegistrationServlet extends HttpServlet {
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
     * Handle GET requests - display registration form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/WEB-INF/views/mentor-registration.jsp").forward(request, response);
    }
    
    /**
     * Handle POST requests - process registration form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get form parameters
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String department = request.getParameter("department");
        String specialization = request.getParameter("specialization");
        String maxMenteesStr = request.getParameter("maxMentees");
        
        // Validate input
        boolean hasError = false;
        
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("usernameError", "Username is required");
            hasError = true;
        }
        
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("passwordError", "Password is required");
            hasError = true;
        }
        
        if (confirmPassword == null || !confirmPassword.equals(password)) {
            request.setAttribute("confirmPasswordError", "Passwords do not match");
            hasError = true;
        }
        
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            request.setAttribute("emailError", "Valid email is required");
            hasError = true;
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            request.setAttribute("firstNameError", "First name is required");
            hasError = true;
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            request.setAttribute("lastNameError", "Last name is required");
            hasError = true;
        }
        
        if (department == null || department.trim().isEmpty()) {
            request.setAttribute("departmentError", "Department is required");
            hasError = true;
        }
        
        int maxMentees = 3; // Default value
        if (maxMenteesStr != null && !maxMenteesStr.trim().isEmpty()) {
            try {
                maxMentees = Integer.parseInt(maxMenteesStr);
                if (maxMentees < 1 || maxMentees > 10) {
                    request.setAttribute("maxMenteesError", "Maximum mentees must be between 1 and 10");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("maxMenteesError", "Maximum mentees must be a number");
                hasError = true;
            }
        }
        
        if (hasError) {
            // Preserve entered values
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("department", department);
            request.setAttribute("specialization", specialization);
            request.setAttribute("maxMentees", maxMenteesStr);
            
            // Forward back to registration form with errors
            request.getRequestDispatcher("/WEB-INF/views/mentor-registration.jsp").forward(request, response);
            return;
        }
        
        // Register mentor
        User user = authService.registerMentor(username, password, email, firstName, lastName, 
                                              department, specialization, maxMentees);
        
        if (user != null) {
            // Registration successful, create session and log in
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("message", "Registration successful! Welcome to the Capstone Project Management System.");
            
            // Redirect to mentor dashboard
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/dashboard"));
        } else {
            // Registration failed
            request.setAttribute("error", "Registration failed. Username or email may already be in use.");
            
            // Preserve entered values
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("department", department);
            request.setAttribute("specialization", specialization);
            request.setAttribute("maxMentees", maxMenteesStr);
            
            // Forward back to registration form with error
            request.getRequestDispatcher("/WEB-INF/views/mentor-registration.jsp").forward(request, response);
        }
    }
}
