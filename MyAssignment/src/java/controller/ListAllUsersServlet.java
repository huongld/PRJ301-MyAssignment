package controller;

import dal.UserDAO;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

// Map vào một URL mới, ví dụ /admin/list
@WebServlet(name = "ListAllUsersServlet", urlPatterns = {"/admin/list"})
public class ListAllUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");
        UserDAO userDAO = new UserDAO();

        // CHỈ Division Leader (Mr. A) mới được vào trang này
        if (admin == null || !admin.hasRole("Division Leader")) {
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        // 1. Lấy danh sách TẤT CẢ user
        List<User> allUsers = userDAO.getAllUsers();
        
        // 2. Lấy tên quản lý cho từng user (để hiển thị đẹp hơn)
        // (Bạn có thể bỏ qua bước này nếu muốn, và chỉ hiển thị ManagerID)
        // Tuy nhiên, làm vậy JSP sẽ phải gọi DAO, không tốt
        // Cách tốt hơn là sửa model User để chứa ManagerName
        
        request.setAttribute("userList", allUsers);
        request.getRequestDispatcher("/list_all_users.jsp").forward(request, response);
    }
}