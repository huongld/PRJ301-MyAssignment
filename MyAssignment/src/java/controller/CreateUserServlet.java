package controller;

import dal.UserDAO;
import model.Division;
import model.Role;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CreateUserServlet", urlPatterns = {"/user/create"})
public class CreateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();

        // Kiểm tra quyền (Leader HOẶC Trưởng nhóm)
        if (admin == null || (!admin.hasRole("Division Leader") && !admin.hasRole("Trưởng nhóm"))) {
            response.sendRedirect(request.getContextPath() + "/admin/list");
            return;
        }
        
        // --- LOGIC LỌC VAI TRÒ (Giữ nguyên) ---
        List<Role> allRoles = userDAO.getAllRoles();
        List<Role> allowedRoles = new ArrayList<>(); 
        if (admin.hasRole("Division Leader")) {
            allowedRoles = allRoles;
        } else if (admin.hasRole("Trưởng nhóm")) {
            for (Role r : allRoles) {
                if (r.getRoleName().equalsIgnoreCase("Nhân viên")) {
                    allowedRoles.add(r);
                    break; 
                }
            }
        }

        // ===================================================
        // === LOGIC MỚI ĐỂ LỌC DANH SÁCH QUẢN LÝ (MANAGER) ===
        // ===================================================
        
        // 1. Lấy TẤT CẢ nhân viên (đã có vai trò, nhờ Bước 1)
        List<User> allStaff = userDAO.getStaffByDivision(admin.getDivision().getDivisionID());
        
        // 2. Tạo danh sách quản lý được phép
        List<User> allowedManagers = new ArrayList<>();

        if (admin.hasRole("Division Leader")) {
            // Mr. A (Leader) được thấy tất cả mọi người
            allowedManagers = allStaff;
        } else if (admin.hasRole("Trưởng nhóm")) {
            // Mr. B (Trưởng nhóm) thấy tất cả, TRỪ 'Division Leader'
            for (User staff : allStaff) {
                if (!staff.hasRole("Division Leader")) {
                    allowedManagers.add(staff);
                }
            }
        }
        // ===================================================
        // === KẾT THÚC LOGIC MỚI ===========================
        // ===================================================

        // Gửi 2 danh sách ĐÃ LỌC ra JSP
        request.setAttribute("roleList", allowedRoles); 
        request.setAttribute("managerList", allowedManagers); // Gửi danh sách quản lý đã lọc
        
        request.getRequestDispatcher("/create_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();

        // Kiểm tra quyền (Leader HOẶC Trưởng nhóm)
        if (admin == null || (!admin.hasRole("Division Leader") && !admin.hasRole("Trưởng nhóm"))) {
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        try {
            String user = request.getParameter("username");
            String pass = request.getParameter("password"); 
            String fullName = request.getParameter("fullName");
            int managerID = Integer.parseInt(request.getParameter("managerID"));
            int roleID = Integer.parseInt(request.getParameter("roleID"));
            int divisionID = admin.getDivision().getDivisionID();

            // --- BẢO MẬT: KIỂM TRA QUYỀN TRƯỚNG NHÓM ---
            // Kiểm tra xem Mr. B có đang cố tạo quyền gì khác ngoài "Nhân viên" không
            // (Phòng trường hợp user sửa code HTML)
            if (admin.hasRole("Trưởng nhóm") && !admin.hasRole("Division Leader")) {
                Role nhanVienRole = userDAO.getRoleByName("Nhân viên"); // Dùng hàm mới
                
                if (nhanVienRole == null || roleID != nhanVienRole.getRoleID()) {
                    request.setAttribute("error", "Quyền không hợp lệ. Trưởng nhóm chỉ được phép tạo tài khoản 'Nhân viên'.");
                    doGet(request, response); // Tải lại form với lỗi
                    return;
                }
            }
            // --- KẾT THÚC BẢO MẬT ---

            // 1. Kiểm tra username tồn tại
            if (userDAO.isUsernameExists(user)) {
                request.setAttribute("error", "Username '" + user + "' đã tồn tại!");
                doGet(request, response); 
                return;
            }

            // 2. Tạo đối tượng User mới
            User newUser = new User();
            newUser.setUsername(user);
            newUser.setPassword(pass);
            newUser.setFullName(fullName);
            newUser.setManagerID(managerID);
            newUser.setDivision(admin.getDivision()); 

            // 3. Gọi DAO để tạo user
            boolean success = userDAO.createUser(newUser, roleID);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/list");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo user!");
                doGet(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ!");
            doGet(request, response);
        }
    }
}