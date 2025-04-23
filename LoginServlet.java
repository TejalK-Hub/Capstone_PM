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
 * Servlet for handling user login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
     * Handle GET requests - display login form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            // Redirect to appropriate dashboard based on user role
            User user = (User) session.getAttribute("user");
            redirectToDashboard(response, user.getRole());
            return;
        }

        // Forward to login page
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    /**
     * Handle POST requests - process login form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        // Authenticate user
        User user = authService.authenticate(username, password);

        if (user != null) {
            // Create session and store user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Redirect to appropriate dashboard based on user role
            redirectToDashboard(response, user.getRole());
        } else {
            // Authentication failed
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    /**
     * Redirect to appropriate dashboard based on user role
     * @param response HTTP response
     * @param role User role
     * @throws IOException if an I/O error occurs
     */
    private void redirectToDashboard(HttpServletResponse response, String role) throws IOException {
        switch (role) {
            case "student":
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/student/dashboard"));
                break;
            case "mentor":
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/dashboard"));
                break;
            case "admin":
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/admin/dashboard"));
                break;
            default:
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/login"));
                break;
        }
    }
}
