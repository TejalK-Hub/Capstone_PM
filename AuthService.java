package service;

import dao.UserDAO;
import dao.StudentDAO;
import dao.MentorDAO;
import model.User;
import model.Student;
import model.Mentor;
import util.PasswordUtil;

/**
 * Service class for authentication and user management
 */
public class AuthService {
    private UserDAO userDAO;
    private StudentDAO studentDAO;
    private MentorDAO mentorDAO;
    
    /**
     * Constructor
     */
    public AuthService() {
        userDAO = new UserDAO();
        studentDAO = new StudentDAO();
        mentorDAO = new MentorDAO();
    }
    
    /**
     * Authenticate a user
     * @param username Username
     * @param password Plain text password
     * @return User object if authentication is successful, null otherwise
     */
    public User authenticate(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        
        if (user != null && PasswordUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }
    
    /**
     * Register a new student
     * @param username Username
     * @param password Plain text password
     * @param email Email address
     * @param firstName First name
     * @param lastName Last name
     * @param studentNumber Student number
     * @param major Major
     * @param graduationYear Graduation year
     * @return User object if registration is successful, null otherwise
     */
    public User registerStudent(String username, String password, String email, String firstName, 
                               String lastName, String studentNumber, String major, int graduationYear) {
        // Check if username or email already exists
        if (userDAO.getUserByUsername(username) != null || userDAO.getUserByEmail(email) != null) {
            return null;
        }
        
        // Create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole("student");
        
        User createdUser = userDAO.createUser(user);
        
        if (createdUser != null) {
            // Create student profile
            Student student = new Student();
            student.setUserId(createdUser.getUserId());
            student.setStudentNumber(studentNumber);
            student.setMajor(major);
            student.setGraduationYear(graduationYear);
            
            Student createdStudent = studentDAO.createStudent(student);
            
            if (createdStudent != null) {
                return createdUser;
            } else {
                // Rollback user creation if student profile creation fails
                userDAO.deleteUser(createdUser.getUserId());
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Register a new mentor
     * @param username Username
     * @param password Plain text password
     * @param email Email address
     * @param firstName First name
     * @param lastName Last name
     * @param department Department
     * @param specialization Specialization
     * @param maxMentees Maximum number of mentees
     * @return User object if registration is successful, null otherwise
     */
    public User registerMentor(String username, String password, String email, String firstName, 
                              String lastName, String department, String specialization, int maxMentees) {
        // Check if username or email already exists
        if (userDAO.getUserByUsername(username) != null || userDAO.getUserByEmail(email) != null) {
            return null;
        }
        
        // Create user
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordUtil.hashPassword(password));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole("mentor");
        
        User createdUser = userDAO.createUser(user);
        
        if (createdUser != null) {
            // Create mentor profile
            Mentor mentor = new Mentor();
            mentor.setUserId(createdUser.getUserId());
            mentor.setDepartment(department);
            mentor.setSpecialization(specialization);
            mentor.setMaxMentees(maxMentees);
            
            Mentor createdMentor = mentorDAO.createMentor(mentor);
            
            if (createdMentor != null) {
                return createdUser;
            } else {
                // Rollback user creation if mentor profile creation fails
                userDAO.deleteUser(createdUser.getUserId());
                return null;
            }
        }
        
        return null;
    }
    
    /**
     * Update user password
     * @param userId User ID
     * @param currentPassword Current password
     * @param newPassword New password
     * @return true if password update is successful, false otherwise
     */
    public boolean updatePassword(int userId, String currentPassword, String newPassword) {
        User user = userDAO.getUserById(userId);
        
        if (user != null && PasswordUtil.verifyPassword(currentPassword, user.getPassword())) {
            user.setPassword(PasswordUtil.hashPassword(newPassword));
            return userDAO.updateUser(user);
        }
        
        return false;
    }
    
    /**
     * Update user profile
     * @param user User object with updated information
     * @return true if profile update is successful, false otherwise
     */
    public boolean updateUserProfile(User user) {
        return userDAO.updateUser(user);
    }
}
