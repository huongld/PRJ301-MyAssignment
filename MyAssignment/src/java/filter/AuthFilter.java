package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

// Lọc tất cả các request
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); // Lấy session, không tạo mới

        String contextPath = req.getContextPath();
        String requestURI = req.getRequestURI();

        // 1. Cho phép các tài nguyên public (CSS, JS, Images) đi qua
        if (requestURI.startsWith(contextPath + "/css/") ||
            requestURI.startsWith(contextPath + "/js/") ||
            requestURI.startsWith(contextPath + "/images/")) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Kiểm tra trang đăng nhập
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        boolean isLoginRequest = requestURI.equals(contextPath + "/login");

        if (isLoggedIn || isLoginRequest) {
            // Nếu đã đăng nhập, hoặc đang cố truy cập trang login
            // (Bạn có thể thêm logic: Nếu đã đăng nhập mà vào /login thì đá về /home)
            if(isLoggedIn && isLoginRequest) {
                res.sendRedirect(contextPath + "/home");
                return;
            }
            chain.doFilter(request, response); // Cho phép đi tiếp
        } else {
            // Chưa đăng nhập, và đang cố truy cập 1 trang được bảo vệ
            res.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Có thể bỏ qua
    }

    @Override
    public void destroy() {
        // Có thể bỏ qua
    }
}