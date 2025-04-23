package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.MentorAssignment;
import model.ProgressUpdate;
import model.User;
import util.DBUtil;

/**
 * Data Access Object for ProgressUpdate entity
 */
public class ProgressUpdateDAO {
    private MentorAssignmentDAO assignmentDAO = new MentorAssignmentDAO();
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Get progress update by ID
     * @param updateId Update ID
     * @return ProgressUpdate object or null if not found
     */
    public ProgressUpdate getUpdateById(int updateId) {
        ProgressUpdate update = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM progress_updates WHERE update_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, updateId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                update = mapUpdate(rs);
                // Load associated assignment and creator
                MentorAssignment assignment = assignmentDAO.getAssignmentById(update.getAssignmentId());
                User creator = userDAO.getUserById(update.getCreatedBy());
                update.setAssignment(assignment);
                update.setCreator(creator);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return update;
    }
    
    /**
     * Get progress updates by assignment ID
     * @param assignmentId Assignment ID
     * @return List of progress updates for the assignment
     */
    public List<ProgressUpdate> getUpdatesByAssignmentId(int assignmentId) {
        List<ProgressUpdate> updates = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM progress_updates WHERE assignment_id = ? ORDER BY created_at DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assignmentId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                ProgressUpdate update = mapUpdate(rs);
                // Load creator
                User creator = userDAO.getUserById(update.getCreatedBy());
                update.setCreator(creator);
                updates.add(update);
            }
            
            // Load assignment for the first update if there are any
            if (!updates.isEmpty()) {
                MentorAssignment assignment = assignmentDAO.getAssignmentById(assignmentId);
                for (ProgressUpdate update : updates) {
                    update.setAssignment(assignment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return updates;
    }
    
    /**
     * Get progress updates by creator ID
     * @param createdBy User ID of the creator
     * @return List of progress updates created by the user
     */
    public List<ProgressUpdate> getUpdatesByCreator(int createdBy) {
        List<ProgressUpdate> updates = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM progress_updates WHERE created_by = ? ORDER BY created_at DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, createdBy);
            rs = stmt.executeQuery();
            
            User creator = userDAO.getUserById(createdBy);
            
            while (rs.next()) {
                ProgressUpdate update = mapUpdate(rs);
                update.setCreator(creator);
                
                // Load associated assignment
                MentorAssignment assignment = assignmentDAO.getAssignmentById(update.getAssignmentId());
                update.setAssignment(assignment);
                
                updates.add(update);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return updates;
    }
    
    /**
     * Create a new progress update
     * @param update ProgressUpdate object to create
     * @return Created update with ID set, or null if creation failed
     */
    public ProgressUpdate createUpdate(ProgressUpdate update) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO progress_updates (assignment_id, title, description, update_type, created_by) "
                       + "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, update.getAssignmentId());
            stmt.setString(2, update.getTitle());
            stmt.setString(3, update.getDescription());
            stmt.setString(4, update.getUpdateType());
            stmt.setInt(5, update.getCreatedBy());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return null;
            }
            
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                update.setUpdateId(rs.getInt(1));
                return update;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        
        return null;
    }
    
    /**
     * Update an existing progress update
     * @param update ProgressUpdate object to update
     * @return true if update was successful, false otherwise
     */
    public boolean updateProgressUpdate(ProgressUpdate update) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "UPDATE progress_updates SET title = ?, description = ?, update_type = ? "
                       + "WHERE update_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, update.getTitle());
            stmt.setString(2, update.getDescription());
            stmt.setString(3, update.getUpdateType());
            stmt.setInt(4, update.getUpdateId());
            
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
     * Delete a progress update
     * @param updateId ID of the update to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteUpdate(int updateId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBUtil.getConnection();
            String sql = "DELETE FROM progress_updates WHERE update_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, updateId);
            
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
     * Map ResultSet to ProgressUpdate object
     * @param rs ResultSet containing update data
     * @return ProgressUpdate object
     * @throws SQLException if a database access error occurs
     */
    private ProgressUpdate mapUpdate(ResultSet rs) throws SQLException {
        ProgressUpdate update = new ProgressUpdate();
        update.setUpdateId(rs.getInt("update_id"));
        update.setAssignmentId(rs.getInt("assignment_id"));
        update.setTitle(rs.getString("title"));
        update.setDescription(rs.getString("description"));
        update.setUpdateType(rs.getString("update_type"));
        update.setCreatedBy(rs.getInt("created_by"));
        update.setCreatedAt(rs.getTimestamp("created_at"));
        return update;
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
