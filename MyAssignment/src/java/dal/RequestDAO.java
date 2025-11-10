package dal;

import model.Request;
import model.User;
import java.sql.Connection; // Đảm bảo đã import
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestDAO extends DBContext {

    // ===== PHẦN SỬA ĐỂ CHIA SẺ CONNECTION =====
    private final UserDAO userDAO; // Chỉ khai báo

    // Constructor của RequestDAO
    public RequestDAO() {
        super(); // 1. Mở connection cho RequestDAO
        
        // 2. Khởi tạo UserDAO và "chia sẻ" connection này cho nó
        this.userDAO = new UserDAO(this.connection); 
    }
    // ==========================================

    /**
     * Tạo một đơn nghỉ phép mới.
     */
    public void createRequest(Request req) {
        String sql = "INSERT INTO [Request] (Title, Reason, FromDate, ToDate, CreatedBy, Status, CreatedDate) "
                   + "VALUES (?, ?, ?, ?, ?, 1, GETDATE())";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, req.getTitle());
            stm.setString(2, req.getReason());
            stm.setDate(3, req.getFromDate());
            stm.setDate(4, req.getToDate());
            
            // Lấy ID từ đối tượng User
            stm.setInt(5, req.getCreatedBy().getUserID()); 
            
            stm.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(RequestDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Lấy danh sách đơn (cho cả nhân viên và quản lý).
     * Bao gồm: Đơn của chính mình, VÀ đơn của cấp dưới (nếu có).
     */
    public List<Request> getRequestsForUser(int userID) {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT r.* "
                   + "FROM [Request] r "
                   + "JOIN [User] u ON r.CreatedBy = u.UserID "
                   + "WHERE (r.CreatedBy = ? OR u.ManagerID = ?) " // Đơn của mình HOẶC đơn của cấp dưới
                   + "AND u.IsActive = 1 " // Chỉ lấy đơn của user còn hoạt động
                   + "ORDER BY r.CreatedDate DESC";
        
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userID);
            stm.setInt(2, userID);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(RequestDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }

    /**
     * Lấy chi tiết một đơn bằng ID.
     */
    public Request getRequestById(int requestID) {
        String sql = "SELECT * FROM [Request] WHERE RequestID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, requestID);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRequest(rs);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(RequestDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Xử lý đơn (Approve/Reject).
     */
    public void processRequest(int requestID, int processedByID, int newStatus, String comment) {
        String sql = "UPDATE [Request] "
                   + "SET Status = ?, ProcessedBy = ?, ProcessedComment = ?, ProcessedDate = GETDATE() "
                   + "WHERE RequestID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, newStatus);
            stm.setInt(2, processedByID);
            stm.setString(3, comment);
            stm.setInt(4, requestID);
            stm.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(RequestDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Lấy các đơn đã được duyệt của một phòng ban (cho Agenda).
     */
    public List<Request> getApprovedRequestsByDivision(int divisionID, Date fromDate, Date toDate) {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT r.* "
                   + "FROM [Request] r "
                   + "JOIN [User] u ON r.CreatedBy = u.UserID "
                   + "WHERE u.DivisionID = ? "
                   + "AND u.IsActive = 1 " // Chỉ lấy user còn hoạt động
                   + "AND r.Status = 2 " // Chỉ lấy đơn Approved
                   + "AND r.FromDate <= ? " 
                   + "AND r.ToDate >= ? ";
        
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, divisionID);
            stm.setDate(2, toDate);
            stm.setDate(3, fromDate);
            
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(RequestDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return list;
    }
    
    /**
     * Hàm nội bộ để "map" dữ liệu từ ResultSet sang đối tượng Request.
     */
    private Request mapResultSetToRequest(ResultSet rs) throws SQLException {
        Request req = new Request();
        req.setRequestID(rs.getInt("RequestID"));
        req.setTitle(rs.getString("Title"));
        req.setReason(rs.getString("Reason"));
        req.setFromDate(rs.getDate("FromDate"));
        req.setToDate(rs.getDate("ToDate"));
        req.setStatus(rs.getInt("Status"));
        req.setCreatedDate(rs.getTimestamp("CreatedDate"));
        req.setProcessedDate(rs.getTimestamp("ProcessedDate"));
        req.setProcessedComment(rs.getString("ProcessedComment"));

        // UserDAO bây giờ đã được chia sẻ connection nên sẽ chạy đúng
        int createdByID = rs.getInt("CreatedBy");
        req.setCreatedBy(userDAO.getUserByID(createdByID));

        int processedByID = rs.getInt("ProcessedBy");
        if (!rs.wasNull()) {
            req.setProcessedBy(userDAO.getUserByID(processedByID));
        }
        
        return req;
    }
}