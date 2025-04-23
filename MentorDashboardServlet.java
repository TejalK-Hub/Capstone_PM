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
import model.MentorAssignment;
import model.MentorInterest;
import service.ProjectService;
import dao.MentorDAO;

/**
 * Servlet for handling mentor dashboard
 */
@WebServlet("/mentor/dashboard")
public class MentorDashboardServlet extends HttpServlet {
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
     * Handle GET requests - display mentor dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // Get mentor profile
        Mentor mentor = mentorDAO.getMentorByUserId(user.getUserId());

        if (mentor != null) {
            // Get mentor's interests
            List<MentorInterest> interests = projectService.getInterestsForMentor(mentor.getMentorId());

            // Get mentor's assignments
            List<MentorAssignment> assignments = projectService.getAssignmentsByMentorId(mentor.getMentorId());

            // Set attributes for the view
            request.setAttribute("mentor", mentor);
            request.setAttribute("interests", interests);
            request.setAttribute("assignments", assignments);
        }

        // Forward to dashboard page
        request.getRequestDispatcher("/WEB-INF/views/mentor/dashboard.jsp").forward(request, response);
    }
}
