package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.MentorAssignment;
import model.Mentor;
import model.Student;
import model.ProjectProposal;
import util.DBUtil;

/**
 * Data Access Object for MentorAssignment entity
 */
public class MentorAssignmentDAO {
    private MentorDAO mentorDAO = new MentorDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private ProjectProposalDAO proposalDAO = new ProjectProposalDAO();
    
    /**
     * Get mentor assignment by ID
     * @param assignmentId Assignment ID
     * @return MentorAssignment object or null if not found
     */
    public MentorAssignment getAssignmentById(int assignmentId) {
        MentorAssignment assignment = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_assignments WHERE assignment_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assignmentId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                assignment = mapAssignment(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(assignment.getMentorId());
                Student student = studentDAO.getStudentById(assignment.getStudentId());
                ProjectProposal proposal = proposalDAO.getProposalById(assignment.getProposalId());
                
                assignment.setMentor(mentor);
                assignment.setStudent(student);
                assignment.setProposal(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return assignment;
    }
    
    /**
     * Get assignment by proposal ID
     * @param proposalId Proposal ID
     * @return MentorAssignment object or null if not found
     */
    public MentorAssignment getAssignmentByProposalId(int proposalId) {
        MentorAssignment assignment = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_assignments WHERE proposal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, proposalId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                assignment = mapAssignment(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(assignment.getMentorId());
                Student student = studentDAO.getStudentById(assignment.getStudentId());
                ProjectProposal proposal = proposalDAO.getProposalById(assignment.getProposalId());
                
                assignment.setMentor(mentor);
                assignment.setStudent(student);
                assignment.setProposal(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return assignment;
    }
    
    /**
     * Get assignments by mentor ID
     * @param mentorId Mentor ID
     * @return List of assignments for the mentor
     */
    public List<MentorAssignment> getAssignmentsByMentorId(int mentorId) {
        List<MentorAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_assignments WHERE mentor_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mentorId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                MentorAssignment assignment = mapAssignment(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(assignment.getMentorId());
                Student student = studentDAO.getStudentById(assignment.getStudentId());
                ProjectProposal proposal = proposalDAO.getProposalById(assignment.getProposalId());
                
                assignment.setMentor(mentor);
                assignment.setStudent(student);
                assignment.setProposal(proposal);
                
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return assignments;
    }
    
    /**
     * Get assignments by student ID
     * @param studentId Student ID
     * @return List of assignments for the student
     */
    public List<MentorAssignment> getAssignmentsByStudentId(int studentId) {
        List<MentorAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_assignments WHERE student_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                MentorAssignment assignment = mapAssignment(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(assignment.getMentorId());
                Student student = studentDAO.getStudentById(assignment.getStudentId());
                ProjectProposal proposal = proposalDAO.getProposalById(assignment.getProposalId());
                
                assignment.setMentor(mentor);
                assignment.setStudent(student);
                assignment.setProposal(proposal);
                
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return assignments;
    }
    
    /**
     * Get assignments by status
     * @param status Assignment status
     * @return List of assignments with the specified status
     */
    public List<MentorAssignment> getAssignmentsByStatus(String status) {
        List<MentorAssignment> assignments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_assignments WHERE status = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                MentorAssignment assignment = mapAssignment(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(assignment.getMentorId());
                Student student = studentDAO.getStudentById(assignment.getStudentId());
                ProjectProposal proposal = proposalDAO.getProposalById(assignment.getProposalId());
                
                assignment.setMentor(mentor);
                assignment.setStudent(student);
                assignment.setProposal(proposal);
                
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return assignments;
    }
    
    /**
     * Create a new mentor assignment
     * @param assignment MentorAssignment object to create
     * @return Created assignment with ID set, or null if creation failed
     */
    public MentorAssignment createAssignment(MentorAssignment assignment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO mentor_assignments (mentor_id, student_id, proposal_id, status) "
                       + "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, assignment.getMentorId());
            stmt.setInt(2, assignment.getStudentId());
            stmt.setInt(3, assignment.getProposalId());
            stmt.setString(4, assignment.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return null;
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                assignment.setAssignmentId(rs.getInt(1));
                return assignment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update an existing mentor assignment
     * @param assignment MentorAssignment object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateAssignment(MentorAssignment assignment) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE mentor_assignments SET mentor_id = ?, student_id = ?, proposal_id = ?, "
                       + "status = ? WHERE assignment_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assignment.getMentorId());
            stmt.setInt(2, assignment.getStudentId());
            stmt.setInt(3, assignment.getProposalId());
            stmt.setString(4, assignment.getStatus());
            stmt.setInt(5, assignment.getAssignmentId());
            
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
     * Delete a mentor assignment
     * @param assignmentId ID of the assignment to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteAssignment(int assignmentId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM mentor_assignments WHERE assignment_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assignmentId);
            
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
     * Map ResultSet to MentorAssignment object
     * @param rs ResultSet containing assignment data
     * @return MentorAssignment object
     * @throws SQLException if a database access error occurs
     */
    private MentorAssignment mapAssignment(ResultSet rs) throws SQLException {
        MentorAssignment assignment = new MentorAssignment();
        assignment.setAssignmentId(rs.getInt("assignment_id"));
        assignment.setMentorId(rs.getInt("mentor_id"));
        assignment.setStudentId(rs.getInt("student_id"));
        assignment.setProposalId(rs.getInt("proposal_id"));
        assignment.setAssignedDate(rs.getTimestamp("assigned_date"));
        assignment.setStatus(rs.getString("status"));
        return assignment;
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
