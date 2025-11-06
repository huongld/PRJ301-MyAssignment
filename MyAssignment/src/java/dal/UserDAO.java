package dal;

import model.Division;
import model.Role;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends DBContext {

    /**
     * Kiểm tra đăng nhập. Trả về đối tượng User nếu thành công, null nếu thất bại.
     */
    public User checkLogin(String username, String password) {
        String sql = "SELECT u.*, d.DivisionName "
                   + "FROM [User] u "
                   + "LEFT JOIN [Division] d ON u.DivisionID = d.DivisionID "
                   + "WHERE u.Username = ? AND u.[Password] = ?"; // Bỏ [Password] nếu bạn hash
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            stm.setString(2, password); // Cần hash password ở đây nếu CSDL lưu hash
            
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setFullName(rs.getString("FullName"));
                    user.setManagerID(rs.getInt("ManagerID"));
                    
                    // Lấy thông tin phòng ban
                    Division div = new Division();
                    div.setDivisionID(rs.getInt("DivisionID"));
                    div.setDivisionName(rs.getString("DivisionName"));
                    user.setDivision(div);
                    
                    // Lấy danh sách Roles
                    user.setRoles(getUserRoles(user.getUserID()));
                    return user;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Lấy danh sách Role của một User.
     */
    public List<Role> getUserRoles(int userID) {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.RoleID, r.RoleName "
                   + "FROM [UserRole] ur "
                   + "JOIN [Role] r ON ur.RoleID = r.RoleID "
                   + "WHERE ur.UserID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userID);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    roles.add(new Role(rs.getInt("RoleID"), rs.getString("RoleName")));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return roles;
    }

    /**
     * Lấy danh sách nhân viên trong một phòng ban (dùng cho Agenda).
     */
    public List<User> getStaffByDivision(int divisionID) {
        List<User> staffList = new ArrayList<>();
        String sql = "SELECT UserID, FullName, Username FROM [User] WHERE DivisionID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, divisionID);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setUsername(rs.getString("Username"));
                    staffList.add(user);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return staffList;
    }
    
    /**
     * Lấy thông tin User cơ bản bằng ID (hàm hỗ trợ).
     */
    public User getUserByID(int userID) {
        String sql = "SELECT UserID, FullName, Username FROM [User] WHERE UserID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userID);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setUsername(rs.getString("Username"));
                    // Chúng ta không cần lấy Division hoặc Role ở đây
                    // vì đây chỉ là thông tin hỗ trợ cho bảng Request
                    return user;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // === ĐÃ XÓA HÀM BỊ TRÙNG LẶP Ở ĐÂY ===
}