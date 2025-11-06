package controller;

import dal.RequestDAO;
import dal.UserDAO;
import model.Request;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name = "AgendaServlet", urlPatterns = {"/agenda"})
public class AgendaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        java.sql.Date fromDate = new java.sql.Date(System.currentTimeMillis());
        java.sql.Date toDate = new java.sql.Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
        
        UserDAO userDAO = new UserDAO();
        List<User> staffList = userDAO.getStaffByDivision(user.getDivision().getDivisionID());

        RequestDAO requestDAO = new RequestDAO();
        List<Request> leaveList = requestDAO.getApprovedRequestsByDivision(user.getDivision().getDivisionID(), fromDate, toDate);

        request.setAttribute("staffList", staffList);
        request.setAttribute("leaveList", leaveList);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);

        request.getRequestDispatcher("/agenda.jsp").forward(request, response);
    }
}