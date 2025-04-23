package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import model.User;
import model.Student;
import model.Mentor;
import model.ProjectProposal;
import model.MentorAssignment;
import model.MentorInterest;
import service.AuthService;
import service.ProjectService;
import dao.StudentDAO;
import dao.MentorDAO;
import dao.UserDAO;

/**
 * Servlet for handling user profile viewing and editing
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService authService;
    private StudentDAO studentDAO;
    private MentorDAO mentorDAO;
    private UserDAO userDAO;
    private ProjectService projectService;

    /**
     * Initialize the servlet
     */
    @Override
    public void init() throws ServletException {
        authService = new AuthService();
        studentDAO = new StudentDAO();
        mentorDAO = new MentorDAO();
        userDAO = new UserDAO();
        projectService = new ProjectService();
    }

    /**
     * Handle GET requests - display profile page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get current user
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            // User not logged in
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/login"));
            return;
        }

        // Load additional profile information based on user role
        switch (user.getRole()) {
            case "student":
                Student student = studentDAO.getStudentByUserId(user.getUserId());
                if (student != null) {
                    // Get student statistics
                    List<ProjectProposal> proposals = projectService.getProposalsByStudentId(student.getStudentId());
                    List<MentorAssignment> assignments = projectService.getAssignmentsByStudentId(student.getStudentId());

                    int proposalCount = proposals.size();
                    int activeProjectCount = 0;
                    int mentorCount = 0;

                    for (ProjectProposal proposal : proposals) {
                        if ("in_progress".equals(proposal.getStatus())) {
                            activeProjectCount++;
                        }
                    }

                    for (MentorAssignment assignment : assignments) {
                        if ("accepted".equals(assignment.getStatus())) {
                            mentorCount++;
                        }
                    }

                    request.setAttribute("student", student);
                    request.setAttribute("proposalCount", proposalCount);
                    request.setAttribute("activeProjectCount", activeProjectCount);
                    request.setAttribute("mentorCount", mentorCount);
                }
                request.getRequestDispatcher("/WEB-INF/views/profile/student-profile.jsp").forward(request, response);
                break;

            case "mentor":
                Mentor mentor = mentorDAO.getMentorByUserId(user.getUserId());
                if (mentor != null) {
                    // Get mentor statistics
                    List<MentorAssignment> assignments = projectService.getAssignmentsByMentorId(mentor.getMentorId());
                    List<MentorInterest> interests = projectService.getInterestsForMentor(mentor.getMentorId());

                    int menteeCount = 0;
                    int activeProjectCount = 0;
                    int interestCount = interests.size();

                    for (MentorAssignment assignment : assignments) {
                        if ("accepted".equals(assignment.getStatus())) {
                            menteeCount++;
                            activeProjectCount++;
                        }
                    }

                    request.setAttribute("mentor", mentor);
                    request.setAttribute("menteeCount", menteeCount);
                    request.setAttribute("activeProjectCount", activeProjectCount);
                    request.setAttribute("interestCount", interestCount);
                }
                request.getRequestDispatcher("/WEB-INF/views/profile/mentor-profile.jsp").forward(request, response);
                break;

            case "admin":
                // Get admin statistics
                int studentCount = studentDAO.getAllStudents().size();
                int mentorCount = mentorDAO.getAllMentors().size();
                int proposalCount = projectService.getAllProposals().size();

                request.setAttribute("studentCount", studentCount);
                request.setAttribute("mentorCount", mentorCount);
                request.setAttribute("proposalCount", proposalCount);

                request.getRequestDispatcher("/WEB-INF/views/profile/admin-profile.jsp").forward(request, response);
                break;

            default:
                // Invalid role
                session.setAttribute("error", "Invalid user role");
                response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/login"));
                break;
        }
    }

    /**
     * Handle POST requests - process profile updates
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

        // Get form data for basic user information
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        // Update user information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        boolean userUpdated = userDAO.updateUser(user);

        if (!userUpdated) {
            session.setAttribute("error", "Failed to update user information");
            response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/profile"));
            return;
        }

        // Update session with new user information
        session.setAttribute("user", user);

        // Update role-specific information
        boolean profileUpdated = true;

        switch (user.getRole()) {
            case "student":
                Student student = studentDAO.getStudentByUserId(user.getUserId());
                if (student != null) {
                    String major = request.getParameter("major");
                    String graduationYearStr = request.getParameter("graduationYear");

                    student.setMajor(major);
                    try {
                        int graduationYear = Integer.parseInt(graduationYearStr);
                        student.setGraduationYear(graduationYear);
                    } catch (NumberFormatException e) {
                        // Invalid graduation year
                    }

                    profileUpdated = studentDAO.updateStudent(student);
                }
                break;

            case "mentor":
                Mentor mentor = mentorDAO.getMentorByUserId(user.getUserId());
                if (mentor != null) {
                    String department = request.getParameter("department");
                    String specialization = request.getParameter("specialization");
                    String maxMenteesStr = request.getParameter("maxMentees");

                    mentor.setDepartment(department);
                    mentor.setSpecialization(specialization);
                    try {
                        int maxMentees = Integer.parseInt(maxMenteesStr);
                        mentor.setMaxMentees(maxMentees);
                    } catch (NumberFormatException e) {
                        // Invalid max mentees
                    }

                    profileUpdated = mentorDAO.updateMentor(mentor);
                }
                break;

            case "admin":
                // No additional profile information for admin
                break;

            default:
                // Invalid role
                profileUpdated = false;
                break;
        }

        if (profileUpdated) {
            session.setAttribute("message", "Profile updated successfully");
        } else {
            session.setAttribute("error", "Failed to update profile information");
        }

        response.sendRedirect(response.encodeRedirectURL(getServletContext().getContextPath() + "/profile"));
    }
}
