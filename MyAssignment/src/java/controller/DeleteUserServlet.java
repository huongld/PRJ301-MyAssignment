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

@WebServlet(name = "DeleteUserServlet", urlPatterns = {"/user/delete"})
public class DeleteUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("user");

        // CHỈ Division Leader (Mr. A) mới được xóa
        if (admin == null || !admin.hasRole("Division Leader")) {
            response.sendRedirect(request.getContextPath() + "/list");
            return;
        }

        try {
            int idToDelete = Integer.parseInt(request.getParameter("id"));

            // *** CỰC KỲ QUAN TRỌNG: KHÔNG CHO ADMIN TỰ XÓA MÌNH ***
            if (idToDelete == admin.getUserID()) {
                // Không thể tự xóa
                response.sendRedirect(request.getContextPath() + "/agenda");
                return;
            }

            // Tiến hành "xóa mềm"
            UserDAO userDAO = new UserDAO();
            userDAO.setUserStatus(idToDelete, false); // false = Inactive (Đã xóa)

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        
        // Quay về trang Agenda (vì nút xóa đặt ở đó)
        response.sendRedirect(request.getContextPath() + "/admin/list");
    }
}