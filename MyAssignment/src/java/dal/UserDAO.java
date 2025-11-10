package dal;

import model.Division;
import model.Role;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection; // Đảm bảo đã import
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO extends DBContext {

    // ===== PHẦN SỬA ĐỂ CHIA SẺ CONNECTION =====
    // Constructor mặc định: tự tạo connection
    public UserDAO() {
        super();
    }

    // Constructor chia sẻ: nhận connection từ bên ngoài
    public UserDAO(Connection connection) {
        this.connection = connection;
    }
    // ==========================================

    /**
     * Kiểm tra đăng nhập. Trả về đối tượng User nếu thành công, null nếu thất bại.
     */
    public User checkLogin(String username, String password) {
        String sql = "SELECT u.*, d.DivisionName "
                   + "FROM [User] u "
                   + "LEFT JOIN [Division] d ON u.DivisionID = d.DivisionID "
                   + "WHERE u.Username = ? AND u.[Password] = ? AND u.IsActive = 1"; // Thêm IsActive
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            stm.setString(2, password); 
            
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setFullName(rs.getString("FullName"));
                    user.setManagerID(rs.getInt("ManagerID"));
                    
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
     * Lấy danh sách nhân viên trong một phòng ban (dùng cho Agenda/Tạo User).
     */
    public List<User> getStaffByDivision(int divisionID) {
        List<User> staffList = new ArrayList<>();
        String sql = "SELECT UserID, FullName, Username FROM [User] WHERE DivisionID = ? AND IsActive = 1";
        
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, divisionID);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setUsername(rs.getString("Username"));
                    
                    // Lấy vai trò (quan trọng cho logic lọc)
                    user.setRoles(getUserRoles(user.getUserID()));
                    
                    staffList.add(user);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return staffList;
    }
    
    /**
     * Lấy thông tin User cơ bản bằng ID (hàm hỗ trợ cho RequestDAO).
     */
    public User getUserByID(int userID) {
        String sql = "SELECT UserID, FullName, Username, ManagerID FROM [User] WHERE UserID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setInt(1, userID);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setUsername(rs.getString("Username"));
                    user.setManagerID(rs.getInt("ManagerID"));
                    return user;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    // ----- CÁC HÀM TẠO/XÓA USER (Giữ nguyên) -----

    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT RoleID, RoleName FROM [Role]";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
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

    public boolean isUsernameExists(String username) {
        String sql = "SELECT UserID FROM [User] WHERE Username = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, username);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next(); 
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean createUser(User newUser, int roleID) {
        String sqlUser = "INSERT INTO [User] (Username, [Password], FullName, ManagerID, DivisionID, IsActive) "
                       + "VALUES (?, ?, ?, ?, ?, 1)"; // Thêm IsActive
        String sqlUserRole = "INSERT INTO [UserRole] (UserID, RoleID) VALUES (?, ?)";
        
        try {
            connection.setAutoCommit(false); 
            PreparedStatement stmUser = connection.prepareStatement(sqlUser, 
                                            java.sql.Statement.RETURN_GENERATED_KEYS);
            stmUser.setString(1, newUser.getUsername());
            stmUser.setString(2, newUser.getPassword()); 
            stmUser.setString(3, newUser.getFullName());
            stmUser.setInt(4, newUser.getManagerID());
            stmUser.setInt(5, newUser.getDivision().getDivisionID());
            
            stmUser.executeUpdate();
            
            int newUserID = -1;
            try (ResultSet generatedKeys = stmUser.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newUserID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Tạo user thất bại, không lấy được ID.");
                }
            }
            
            PreparedStatement stmRole = connection.prepareStatement(sqlUserRole);
            stmRole.setInt(1, newUserID);
            stmRole.setInt(2, roleID);
            stmRole.executeUpdate();
            
            connection.commit();
            return true;
            
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
            try {
                connection.rollback(); 
            } catch (SQLException ex) {
                Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                 Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean setUserStatus(int userID, boolean isActive) {
        String sql = "UPDATE [User] SET IsActive = ? WHERE UserID = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setBoolean(1, isActive);
            stm.setInt(2, userID);
            int result = stm.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }
    
    public Role getRoleByName(String roleName) {
        String sql = "SELECT RoleID, RoleName FROM [Role] WHERE RoleName = ?";
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, roleName);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("RoleID"), rs.getString("RoleName"));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT u.*, d.DivisionName "
                   + "FROM [User] u "
                   + "LEFT JOIN [Division] d ON u.DivisionID = d.DivisionID "
                   + "WHERE u.IsActive = 1";
        
        try (PreparedStatement stm = connection.prepareStatement(sql)) {
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setFullName(rs.getString("FullName"));
                    user.setManagerID(rs.getInt("ManagerID"));
                    
                    Division div = new Division();
                    div.setDivisionID(rs.getInt("DivisionID"));
                    div.setDivisionName(rs.getString("DivisionName"));
                    user.setDivision(div);
                    
                    user.setRoles(getUserRoles(user.getUserID()));
                    
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return userList;
    }
}