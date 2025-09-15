package servlet;

import dao.CategoryDAO;
import dao.VideoDAO;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet({"/user/home", "/manager/home", "/admin/home"})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());
    
    // Đường dẫn JSP theo cấu trúc thư mục thực tế
    private static final String USER_JSP = "/user/home.jsp";
    private static final String ADMIN_JSP = "/admin/home.jsp";
    private static final String MANAGER_JSP = "/manager/home.jsp";
    
    // Hằng số cho vai trò
    private static final int USER_ROLE = 1; // User
    private static final int MANAGER_ROLE = 2; // Manager
    private static final int ADMIN_ROLE = 3; // Admin
    
    private CategoryDAO categoryDAO;
    private VideoDAO videoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        categoryDAO = new CategoryDAO();
        videoDAO = new VideoDAO();
        if (categoryDAO == null || videoDAO == null) {
            throw new ServletException("Không thể khởi tạo DAO");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Kiểm tra session và user
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            LOGGER.log(Level.WARNING, "Không tìm thấy user trong session");
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Phiên người dùng không hợp lệ");
            return;
        }

        List<entity.Category> categories;
        List<entity.Video> videos;
        String jspPath;
        
        try {
            // Xác định vai trò và lấy danh sách categories, videos phù hợp
            switch (user.getRoleid()) {
                case USER_ROLE: // Role 1: User
                    categories = categoryDAO.findAll();
                    videos = videoDAO.findAll(); // User thấy tất cả video
                    jspPath = USER_JSP;
                    break;
                case MANAGER_ROLE: // Role 2: Manager
                    categories = categoryDAO.findByUserId(user.getId());
                    videos = videoDAO.findByUserId(user.getId()); // Manager chỉ thấy video của mình
                    jspPath = MANAGER_JSP;
                    break;
                case ADMIN_ROLE: // Role 3: Admin
                    categories = categoryDAO.findAll();
                    videos = videoDAO.findAll(); // Admin thấy tất cả video
                    jspPath = ADMIN_JSP;
                    break;
                default:
                    LOGGER.log(Level.WARNING, "ID vai trò không hợp lệ: {0}", user.getRoleid());
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Vai trò không hợp lệ");
                    return;
            }

            // Đặt danh sách categories và videos vào request
            req.setAttribute("categories", categories);
            req.setAttribute("videos", videos);
            req.getRequestDispatcher(jspPath).forward(req, resp);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xử lý yêu cầu home servlet", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi máy chủ: " + e.getMessage());
        }
    }
}