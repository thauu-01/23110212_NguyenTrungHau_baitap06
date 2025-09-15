package filter;

import entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class RoleFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false); 

        User user = (session != null) ? (User) session.getAttribute("user") : null;
        String path = req.getServletPath();

        // Cho phép truy cập các đường dẫn công khai
        if (path.startsWith("/login") 
                || path.startsWith("/logout") 
                || path.startsWith("/assets/")  
                || path.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        // Nếu không có user trong session, chuyển hướng đến login
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Kiểm tra quyền theo roleid
        boolean allowed = false;
        switch (user.getRoleid()) {
            case 1: // User
                allowed = path.startsWith("/user/") || path.startsWith("/category/");
                break;
            case 2: // Manager
                allowed = path.startsWith("/manager/") || path.startsWith("/category/");
                break;
            case 3: // Admin
                allowed = path.startsWith("/admin/") || path.startsWith("/category/") || path.startsWith("/video/");
                break;
        }

        if (allowed) {
            chain.doFilter(request, response);
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        }
    }
}