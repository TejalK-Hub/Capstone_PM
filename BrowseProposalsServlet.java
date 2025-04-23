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
import model.Mentor;
import model.ProjectProposal;
import service.ProjectService;
import dao.MentorDAO;

/**
 * Servlet for handling browsing of project proposals
 */
@WebServlet("/mentor/browse-proposals")
public class BrowseProposalsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProjectService projectService;
    private MentorDAO mentorDAO;
    
    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        projectService = new ProjectService();
        mentorDAO = new MentorDAO();
    }
    
    /**
     * Handle GET requests - display list of proposals
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Get mentor profile
        Mentor mentor = mentorDAO.getMentorByUserId(user.getUserId());
        
        if (mentor == null) {
            // Mentor profile not found
            session.setAttribute("error", "Mentor profile not found");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/mentor/dashboard"));
            return;
        }
        
        // Get approved proposals
        List<ProjectProposal> proposals = projectService.getProposalsByStatus("approved");
        
        // Set attributes for the view
        request.setAttribute("mentor", mentor);
        request.setAttribute("proposals", proposals);
        
        // Forward to proposals page
        request.getRequestDispatcher("/WEB-INF/views/mentor/browse-proposals.jsp").forward(request, response);
    }
}
