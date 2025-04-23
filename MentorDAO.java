package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Mentor;
import model.User;
import util.DBUtil;

/**
 * Data Access Object for Mentor entity
 */
public class MentorDAO {
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Get mentor by ID
     * @param mentorId Mentor ID
     * @return Mentor object or null if not found
     */
    public Mentor getMentorById(int mentorId) {
        Mentor mentor = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentors WHERE mentor_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mentorId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                mentor = mapMentor(rs);
                // Load associated user
                User user = userDAO.getUserById(mentor.getUserId());
                mentor.setUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return mentor;
    }
    
    /**
     * Get mentor by user ID
     * @param userId User ID
     * @return Mentor object or null if not found
     */
    public Mentor getMentorByUserId(int userId) {
        Mentor mentor = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentors WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                mentor = mapMentor(rs);
                // Load associated user
                User user = userDAO.getUserById(mentor.getUserId());
                mentor.setUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return mentor;
    }
    
    /**
     * Get all mentors
     * @return List of all mentors
     */
    public List<Mentor> getAllMentors() {
        List<Mentor> mentors = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentors";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Mentor mentor = mapMentor(rs);
                // Load associated user
                User user = userDAO.getUserById(mentor.getUserId());
                mentor.setUser(user);
                mentors.add(mentor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return mentors;
    }
    
    /**
     * Get mentors by department
     * @param department Department name
     * @return List of mentors in the specified department
     */
    public List<Mentor> getMentorsByDepartment(String department) {
        List<Mentor> mentors = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentors WHERE department = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, department);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Mentor mentor = mapMentor(rs);
                // Load associated user
                User user = userDAO.getUserById(mentor.getUserId());
                mentor.setUser(user);
                mentors.add(mentor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return mentors;
    }
    
    /**
     * Create a new mentor
     * @param mentor Mentor object to create
     * @return Created mentor with ID set, or null if creation failed
     */
    public Mentor createMentor(Mentor mentor) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO mentors (user_id, department, specialization, max_mentees) "
                       + "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, mentor.getUserId());
            stmt.setString(2, mentor.getDepartment());
            stmt.setString(3, mentor.getSpecialization());
            stmt.setInt(4, mentor.getMaxMentees());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return null;
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                mentor.setMentorId(rs.getInt(1));
                return mentor;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update an existing mentor
     * @param mentor Mentor object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateMentor(Mentor mentor) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE mentors SET department = ?, specialization = ?, max_mentees = ? "
                       + "WHERE mentor_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, mentor.getDepartment());
            stmt.setString(2, mentor.getSpecialization());
            stmt.setInt(3, mentor.getMaxMentees());
            stmt.setInt(4, mentor.getMentorId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    /**
     * Delete a mentor
     * @param mentorId ID of the mentor to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteMentor(int mentorId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM mentors WHERE mentor_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mentorId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    /**
     * Map ResultSet to Mentor object
     * @param rs ResultSet containing mentor data
     * @return Mentor object
     * @throws SQLException if a database access error occurs
     */
    private Mentor mapMentor(ResultSet rs) throws SQLException {
        Mentor mentor = new Mentor();
        mentor.setMentorId(rs.getInt("mentor_id"));
        mentor.setUserId(rs.getInt("user_id"));
        mentor.setDepartment(rs.getString("department"));
        mentor.setSpecialization(rs.getString("specialization"));
        mentor.setMaxMentees(rs.getInt("max_mentees"));
        return mentor;
    }
    
    /**
     * Close database resources
     * @param conn Connection object
     * @param stmt Statement object
     * @param rs ResultSet object
     */
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
