package model;

import java.util.List;

public class User {
    private int userID;
    private String username;
    private String password; // Thường không lưu password trong session, nhưng cần cho DAO
    private String fullName;
    private int managerID; // ID của người quản lý trực tiếp
    
    // Các đối tượng liên quan (DAO sẽ join bảng để lấy)
    private Division division;
    private List<Role> roles;

    public User() {
    }

    // Getters and Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getManagerID() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID = managerID;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
    
    // (Optional) Hàm kiểm tra quyền nhanh
    public boolean hasRole(String roleName) {
        if (roles == null) return false;
        for (Role role : roles) {
            if (role.getRoleName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}