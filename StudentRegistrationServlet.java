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
 * Servlet for handling student registration
 */
@WebServlet("/register/student")
public class StudentRegistrationServlet extends HttpServlet {
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
        request.getRequestDispatcher("/WEB-INF/views/student-registration.jsp").forward(request, response);
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
        String studentNumber = request.getParameter("studentNumber");
        String major = request.getParameter("major");
        String graduationYearStr = request.getParameter("graduationYear");
        
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
        
        if (studentNumber == null || studentNumber.trim().isEmpty()) {
            request.setAttribute("studentNumberError", "Student number is required");
            hasError = true;
        }
        
        int graduationYear = 0;
        try {
            graduationYear = Integer.parseInt(graduationYearStr);
            if (graduationYear < 2000 || graduationYear > 2100) {
                request.setAttribute("graduationYearError", "Invalid graduation year");
                hasError = true;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("graduationYearError", "Graduation year must be a number");
            hasError = true;
        }
        
        if (hasError) {
            // Preserve entered values
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("studentNumber", studentNumber);
            request.setAttribute("major", major);
            request.setAttribute("graduationYear", graduationYearStr);
            
            // Forward back to registration form with errors
            request.getRequestDispatcher("/WEB-INF/views/student-registration.jsp").forward(request, response);
            return;
        }
        
        // Register student
        User user = authService.registerStudent(username, password, email, firstName, lastName, 
                                               studentNumber, major, graduationYear);
        
        if (user != null) {
            // Registration successful, create session and log in
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setAttribute("message", "Registration successful! Welcome to the Capstone Project Management System.");
            
            // Redirect to student dashboard
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
        } else {
            // Registration failed
            request.setAttribute("error", "Registration failed. Username or email may already be in use.");
            
            // Preserve entered values
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("studentNumber", studentNumber);
            request.setAttribute("major", major);
            request.setAttribute("graduationYear", graduationYearStr);
            
            // Forward back to registration form with error
            request.getRequestDispatcher("/WEB-INF/views/student-registration.jsp").forward(request, response);
        }
    }
}
