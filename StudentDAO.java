package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Student;
import model.User;
import util.DBUtil;

/**
 * Data Access Object for Student entity
 */
public class StudentDAO {
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Get student by ID
     * @param studentId Student ID
     * @return Student object or null if not found
     */
    public Student getStudentById(int studentId) {
        Student student = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM students WHERE student_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                student = mapStudent(rs);
                // Load associated user
                User user = userDAO.getUserById(student.getUserId());
                student.setUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return student;
    }
    
    /**
     * Get student by user ID
     * @param userId User ID
     * @return Student object or null if not found
     */
    public Student getStudentByUserId(int userId) {
        Student student = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM students WHERE user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                student = mapStudent(rs);
                // Load associated user
                User user = userDAO.getUserById(student.getUserId());
                student.setUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return student;
    }
    
    /**
     * Get student by student number
     * @param studentNumber Student number
     * @return Student object or null if not found
     */
    public Student getStudentByStudentNumber(String studentNumber) {
        Student student = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM students WHERE student_number = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, studentNumber);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                student = mapStudent(rs);
                // Load associated user
                User user = userDAO.getUserById(student.getUserId());
                student.setUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return student;
    }
    
    /**
     * Get all students
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM students";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Student student = mapStudent(rs);
                // Load associated user
                User user = userDAO.getUserById(student.getUserId());
                student.setUser(user);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return students;
    }
    
    /**
     * Create a new student
     * @param student Student object to create
     * @return Created student with ID set, or null if creation failed
     */
    public Student createStudent(Student student) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO students (user_id, student_number, major, graduation_year) "
                       + "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, student.getUserId());
            stmt.setString(2, student.getStudentNumber());
            stmt.setString(3, student.getMajor());
            stmt.setInt(4, student.getGraduationYear());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return null;
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                student.setStudentId(rs.getInt(1));
                return student;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update an existing student
     * @param student Student object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateStudent(Student student) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE students SET student_number = ?, major = ?, graduation_year = ? "
                       + "WHERE student_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getMajor());
            stmt.setInt(3, student.getGraduationYear());
            stmt.setInt(4, student.getStudentId());
            
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
     * Delete a student
     * @param studentId ID of the student to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteStudent(int studentId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM students WHERE student_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            
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
     * Map ResultSet to Student object
     * @param rs ResultSet containing student data
     * @return Student object
     * @throws SQLException if a database access error occurs
     */
    private Student mapStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setUserId(rs.getInt("user_id"));
        student.setStudentNumber(rs.getString("student_number"));
        student.setMajor(rs.getString("major"));
        student.setGraduationYear(rs.getInt("graduation_year"));
        return student;
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
