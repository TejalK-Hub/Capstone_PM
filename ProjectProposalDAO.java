package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ProjectProposal;
import model.Student;
import util.DBUtil;

/**
 * Data Access Object for ProjectProposal entity
 */
public class ProjectProposalDAO {
    private StudentDAO studentDAO = new StudentDAO();
    
    /**
     * Get project proposal by ID
     * @param proposalId Proposal ID
     * @return ProjectProposal object or null if not found
     */
    public ProjectProposal getProposalById(int proposalId) {
        ProjectProposal proposal = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM project_proposals WHERE proposal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, proposalId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                proposal = mapProposal(rs);
                // Load associated student
                Student student = studentDAO.getStudentById(proposal.getStudentId());
                proposal.setStudent(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return proposal;
    }
    
    /**
     * Get all project proposals
     * @return List of all project proposals
     */
    public List<ProjectProposal> getAllProposals() {
        List<ProjectProposal> proposals = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM project_proposals";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                ProjectProposal proposal = mapProposal(rs);
                // Load associated student
                Student student = studentDAO.getStudentById(proposal.getStudentId());
                proposal.setStudent(student);
                proposals.add(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return proposals;
    }
    
    /**
     * Get project proposals by student ID
     * @param studentId Student ID
     * @return List of project proposals for the student
     */
    public List<ProjectProposal> getProposalsByStudentId(int studentId) {
        List<ProjectProposal> proposals = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM project_proposals WHERE student_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProjectProposal proposal = mapProposal(rs);
                // Load associated student
                Student student = studentDAO.getStudentById(proposal.getStudentId());
                proposal.setStudent(student);
                proposals.add(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return proposals;
    }
    
    /**
     * Get project proposals by status
     * @param status Proposal status
     * @return List of project proposals with the specified status
     */
    public List<ProjectProposal> getProposalsByStatus(String status) {
        List<ProjectProposal> proposals = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM project_proposals WHERE status = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProjectProposal proposal = mapProposal(rs);
                // Load associated student
                Student student = studentDAO.getStudentById(proposal.getStudentId());
                proposal.setStudent(student);
                proposals.add(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return proposals;
    }
    
    /**
     * Create a new project proposal
     * @param proposal ProjectProposal object to create
     * @return Created proposal with ID set, or null if creation failed
     */
    public ProjectProposal createProposal(ProjectProposal proposal) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO project_proposals (student_id, title, description, objectives, technologies, status) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, proposal.getStudentId());
            stmt.setString(2, proposal.getTitle());
            stmt.setString(3, proposal.getDescription());
            stmt.setString(4, proposal.getObjectives());
            stmt.setString(5, proposal.getTechnologies());
            stmt.setString(6, proposal.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return null;
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                proposal.setProposalId(rs.getInt(1));
                return proposal;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update an existing project proposal
     * @param proposal ProjectProposal object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateProposal(ProjectProposal proposal) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE project_proposals SET title = ?, description = ?, objectives = ?, "
                       + "technologies = ?, status = ? WHERE proposal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, proposal.getTitle());
            stmt.setString(2, proposal.getDescription());
            stmt.setString(3, proposal.getObjectives());
            stmt.setString(4, proposal.getTechnologies());
            stmt.setString(5, proposal.getStatus());
            stmt.setInt(6, proposal.getProposalId());
            
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
     * Delete a project proposal
     * @param proposalId ID of the proposal to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteProposal(int proposalId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM project_proposals WHERE proposal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, proposalId);
            
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
     * Map ResultSet to ProjectProposal object
     * @param rs ResultSet containing proposal data
     * @return ProjectProposal object
     * @throws SQLException if a database access error occurs
     */
    private ProjectProposal mapProposal(ResultSet rs) throws SQLException {
        ProjectProposal proposal = new ProjectProposal();
        proposal.setProposalId(rs.getInt("proposal_id"));
        proposal.setStudentId(rs.getInt("student_id"));
        proposal.setTitle(rs.getString("title"));
        proposal.setDescription(rs.getString("description"));
        proposal.setObjectives(rs.getString("objectives"));
        proposal.setTechnologies(rs.getString("technologies"));
        proposal.setStatus(rs.getString("status"));
        proposal.setSubmissionDate(rs.getTimestamp("submission_date"));
        proposal.setLastUpdated(rs.getTimestamp("last_updated"));
        return proposal;
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
