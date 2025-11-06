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

@WebServlet(name = "ApproveRequestServlet", urlPatterns = {"/request/approve"})
public class ApproveRequestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int requestID = Integer.parseInt(request.getParameter("id"));
            RequestDAO requestDAO = new RequestDAO();
            Request req = requestDAO.getRequestById(requestID); 

            request.setAttribute("requestDetail", req);
            request.getRequestDispatcher("/approve_request.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User manager = (User) session.getAttribute("user");
        if (manager == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            int requestID = Integer.parseInt(request.getParameter("requestID"));
            String action = request.getParameter("action"); // "approve" hoặc "reject"
            String comment = request.getParameter("comment");

            int newStatus = action.equals("approve") ? 2 : 3; // 2=Approved, 3=Rejected
            int processedBy = manager.getUserID();

            RequestDAO requestDAO = new RequestDAO();
            requestDAO.processRequest(requestID, processedBy, newStatus, comment); 

            response.sendRedirect(request.getContextPath() + "/list");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/list");
        }
    }
}