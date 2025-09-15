package servlet;

import dao.CategoryDAO;
import entity.Category;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({"/category/create", "/category/update", "/category/delete", "/category/view"})
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CategoryDAO dao = new CategoryDAO();

    // ========== POST ==========
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String path = req.getServletPath();

        // Tạo mới category
        if (path.equals("/category/create")) {
            Category cat = new Category();
            cat.setCateName(req.getParameter("cateName"));
            cat.setIcons(req.getParameter("icons"));
            cat.setUser(currentUser);
            dao.create(cat);

        // Cập nhật category
        } else if (path.equals("/category/update")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category existing = dao.findById(id);
            if (existing != null && existing.getUser().getId() == currentUser.getId()) {
                existing.setCateName(req.getParameter("cateName"));
                existing.setIcons(req.getParameter("icons"));
                dao.update(existing);
            } else {
                resp.sendError(403, "Bạn chỉ có thể cập nhật category của mình");
                return;
            }
        }

        // Sau khi xử lý xong → quay về trang home phù hợp với role
        resp.sendRedirect(req.getContextPath() + getHomeUrl(currentUser.getRoleid()));
    }

    // ========== GET ==========
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String path = req.getServletPath();

        // Xóa category
        if (path.equals("/category/delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category cat = dao.findById(id);
            if (cat != null && cat.getUser().getId() == currentUser.getId()) {
                dao.delete(id, currentUser);
            } else {
                resp.sendError(403, "Bạn chỉ có thể xóa category của mình");
                return;
            }
            resp.sendRedirect(req.getContextPath() + getHomeUrl(currentUser.getRoleid()));

        // Xem chi tiết category
        } else if (path.equals("/category/view")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category cat = dao.findById(id);
            if (cat != null && cat.getUser().getId() == currentUser.getId()) {
                req.setAttribute("category", cat);

                // File view.jsp nằm trong thư mục webapp/category
                req.getRequestDispatcher("/category/view.jsp").forward(req, resp);
            } else {
                resp.sendError(403, "Bạn chỉ có thể xem category của mình");
            }
        }
    }

    // Hàm điều hướng home theo role
    private String getHomeUrl(int roleid) {
        return switch (roleid) {
            case 1 -> "/user/home";
            case 2 -> "/manager/home";
            case 3 -> "/admin/home";
            default -> "/login";
        };
    }
}
