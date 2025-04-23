package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.MentorInterest;
import model.Mentor;
import model.ProjectProposal;
import util.DBUtil;

/**
 * Data Access Object for MentorInterest entity
 */
public class MentorInterestDAO {
    private MentorDAO mentorDAO = new MentorDAO();
    private ProjectProposalDAO proposalDAO = new ProjectProposalDAO();
    
    /**
     * Get mentor interest by ID
     * @param interestId Interest ID
     * @return MentorInterest object or null if not found
     */
    public MentorInterest getInterestById(int interestId) {
        MentorInterest interest = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_interest WHERE interest_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, interestId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                interest = mapInterest(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(interest.getMentorId());
                ProjectProposal proposal = proposalDAO.getProposalById(interest.getProposalId());
                
                interest.setMentor(mentor);
                interest.setProposal(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return interest;
    }
    
    /**
     * Get interests by mentor ID
     * @param mentorId Mentor ID
     * @return List of interests for the mentor
     */
    public List<MentorInterest> getInterestsByMentorId(int mentorId) {
        List<MentorInterest> interests = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_interest WHERE mentor_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mentorId);
            rs = stmt.executeQuery();
            
            Mentor mentor = mentorDAO.getMentorById(mentorId);
            
            while (rs.next()) {
                MentorInterest interest = mapInterest(rs);
                interest.setMentor(mentor);
                
                // Load associated proposal
                ProjectProposal proposal = proposalDAO.getProposalById(interest.getProposalId());
                interest.setProposal(proposal);
                
                interests.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return interests;
    }
    
    /**
     * Get interests by proposal ID
     * @param proposalId Proposal ID
     * @return List of interests for the proposal
     */
    public List<MentorInterest> getInterestsByProposalId(int proposalId) {
        List<MentorInterest> interests = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_interest WHERE proposal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, proposalId);
            rs = stmt.executeQuery();
            
            ProjectProposal proposal = proposalDAO.getProposalById(proposalId);
            
            while (rs.next()) {
                MentorInterest interest = mapInterest(rs);
                interest.setProposal(proposal);
                
                // Load associated mentor
                Mentor mentor = mentorDAO.getMentorById(interest.getMentorId());
                interest.setMentor(mentor);
                
                interests.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return interests;
    }
    
    /**
     * Get interest by mentor ID and proposal ID
     * @param mentorId Mentor ID
     * @param proposalId Proposal ID
     * @return MentorInterest object or null if not found
     */
    public MentorInterest getInterestByMentorAndProposal(int mentorId, int proposalId) {
        MentorInterest interest = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM mentor_interest WHERE mentor_id = ? AND proposal_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, mentorId);
            stmt.setInt(2, proposalId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                interest = mapInterest(rs);
                // Load associated entities
                Mentor mentor = mentorDAO.getMentorById(interest.getMentorId());
                ProjectProposal proposal = proposalDAO.getProposalById(interest.getProposalId());
                
                interest.setMentor(mentor);
                interest.setProposal(proposal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return interest;
    }
    
    /**
     * Create a new mentor interest
     * @param interest MentorInterest object to create
     * @return Created interest with ID set, or null if creation failed
     */
    public MentorInterest createInterest(MentorInterest interest) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO mentor_interest (mentor_id, proposal_id, interest_level, comments) "
                       + "VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, interest.getMentorId());
            stmt.setInt(2, interest.getProposalId());
            stmt.setString(3, interest.getInterestLevel());
            stmt.setString(4, interest.getComments());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return null;
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                interest.setInterestId(rs.getInt(1));
                return interest;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update an existing mentor interest
     * @param interest MentorInterest object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateInterest(MentorInterest interest) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE mentor_interest SET interest_level = ?, comments = ? "
                       + "WHERE interest_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, interest.getInterestLevel());
            stmt.setString(2, interest.getComments());
            stmt.setInt(3, interest.getInterestId());
            
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
     * Delete a mentor interest
     * @param interestId ID of the interest to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteInterest(int interestId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM mentor_interest WHERE interest_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, interestId);
            
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
     * Map ResultSet to MentorInterest object
     * @param rs ResultSet containing interest data
     * @return MentorInterest object
     * @throws SQLException if a database access error occurs
     */
    private MentorInterest mapInterest(ResultSet rs) throws SQLException {
        MentorInterest interest = new MentorInterest();
        interest.setInterestId(rs.getInt("interest_id"));
        interest.setMentorId(rs.getInt("mentor_id"));
        interest.setProposalId(rs.getInt("proposal_id"));
        interest.setInterestLevel(rs.getString("interest_level"));
        interest.setComments(rs.getString("comments"));
        interest.setCreatedAt(rs.getTimestamp("created_at"));
        return interest;
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
