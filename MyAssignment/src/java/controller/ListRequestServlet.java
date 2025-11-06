package controller;

import dal.RequestDAO;
import model.Request;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // Bạn có thể xóa dòng này
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

// Chúng ta đã khai báo trong web.xml, nên @WebServlet không còn quá quan trọng
// @WebServlet(name = "ListRequestServlet", urlPatterns = {"/home", "/list"})
public class ListRequestServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        // 1. Kiểm tra đăng nhập (AuthFilter đã làm, nhưng cẩn thận vẫn hơn)
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // 2. Lấy dữ liệu từ DAO
        RequestDAO requestDAO = new RequestDAO();
        List<Request> requestList = requestDAO.getRequestsForUser(user.getUserID()); 

        // 3. Đặt dữ liệu vào request để gửi cho JSP
        request.setAttribute("requestList", requestList);
        
        // 4. Chuyển tiếp đến trang JSP (Đây là phần bị thiếu)
        request.getRequestDispatcher("/list_request.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Hầu hết trang list không cần doPost, cứ gọi doGet cho đơn giản
        doGet(request, response);
    }
}