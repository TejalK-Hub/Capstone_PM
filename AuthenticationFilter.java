package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;

/**
 * Filter for authentication and authorization
 */
@WebFilter(urlPatterns = {"/student/*", "/mentor/*", "/admin/*"})
public class AuthenticationFilter implements Filter {
    
    /**
     * Initialize the filter
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }
    
    /**
     * Filter requests to check authentication and authorization
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Get current session
        HttpSession session = httpRequest.getSession(false);
        
        // Check if user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        
        if (!isLoggedIn) {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(httpResponse.encodeRedirectURL(httpRequest.getContextPath() + "/login"));
            return;
        }
        
        // Check if user has appropriate role for the requested URL
        User user = (User) session.getAttribute("user");
        String requestURI = httpRequest.getRequestURI();
        
        if (requestURI.contains("/student/") && !user.getRole().equals("student") && !user.getRole().equals("admin")) {
            // User is not a student or admin, redirect to appropriate dashboard
            redirectToDashboard(httpResponse, user.getRole(), httpRequest.getContextPath());
            return;
        }
        
        if (requestURI.contains("/mentor/") && !user.getRole().equals("mentor") && !user.getRole().equals("admin")) {
            // User is not a mentor or admin, redirect to appropriate dashboard
            redirectToDashboard(httpResponse, user.getRole(), httpRequest.getContextPath());
            return;
        }
        
        if (requestURI.contains("/admin/") && !user.getRole().equals("admin")) {
            // User is not an admin, redirect to appropriate dashboard
            redirectToDashboard(httpResponse, user.getRole(), httpRequest.getContextPath());
            return;
        }
        
        // User is authenticated and authorized, continue with the request
        chain.doFilter(request, response);
    }
    
    /**
     * Clean up resources
     */
    @Override
    public void destroy() {
        // No cleanup needed
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
