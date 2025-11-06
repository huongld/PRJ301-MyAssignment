package controller;

import dal.RequestDAO;
import model.Request;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;

@WebServlet(name = "CreateRequestServlet", urlPatterns = {"/request/create"})
public class CreateRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        request.getRequestDispatcher("/create_request.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            String title = request.getParameter("title");
            String reason = request.getParameter("reason");
            Date fromDate = Date.valueOf(request.getParameter("fromDate"));
            Date toDate = Date.valueOf(request.getParameter("toDate"));

            Request newRequest = new Request();
            newRequest.setTitle(title);
            newRequest.setReason(reason);
            newRequest.setFromDate(fromDate);
            newRequest.setToDate(toDate);
            newRequest.setCreatedBy(user); 

            RequestDAO requestDAO = new RequestDAO();
            requestDAO.createRequest(newRequest); 

            response.sendRedirect(request.getContextPath() + "/list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/create_request.jsp").forward(request, response);
        }
    }
}