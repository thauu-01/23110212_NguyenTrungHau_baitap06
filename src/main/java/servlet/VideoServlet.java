package servlet;

import dao.CategoryDAO;
import dao.VideoDAO;
import entity.Category;
import entity.User;
import entity.Video;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@WebServlet({"/video/create", "/video/delete", "/video/view"})
@MultipartConfig
public class VideoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VideoDAO dao = new VideoDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();
    private static final String UPLOAD_DIR = "uploads"; // Đúng tên folder trong webapp

    /** Kiểm tra quyền admin */
    private boolean isAdmin(User user) {
        return user != null && user.getRoleid() == 3; // 3 = admin
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // Chặn user/manager
        if (!isAdmin(currentUser)) {
            resp.sendError(403, "Bạn không có quyền truy cập chức năng này!");
            return;
        }

        String path = req.getServletPath();

        try {
            if (path.equals("/video/create")) {
                // Hiển thị form thêm video
                req.setAttribute("categories", categoryDAO.findAll());
                req.getRequestDispatcher("/video/create.jsp").forward(req, resp);
            } else if (path.equals("/video/view")) {
                String videoIdParam = req.getParameter("video_id");
                if (videoIdParam == null || !videoIdParam.matches("\\d+")) {
                    resp.sendError(400, "ID video không hợp lệ");
                    return;
                }

                int videoId = Integer.parseInt(videoIdParam);
                Video video = dao.findById(videoId);
                if (video == null) {
                    resp.sendError(404, "Video không tồn tại");
                    return;
                }

                req.setAttribute("video", video);
                req.getRequestDispatcher("/video/view.jsp").forward(req, resp);
            } else if (path.equals("/video/delete")) {
                String videoIdParam = req.getParameter("video_id");
                if (videoIdParam == null || !videoIdParam.matches("\\d+")) {
                    resp.sendError(400, "ID video không hợp lệ");
                    return;
                }

                int videoId = Integer.parseInt(videoIdParam);
                Video video = dao.findById(videoId);
                if (video == null) {
                    resp.sendError(404, "Video không tồn tại");
                    return;
                }

                // Xóa file vật lý
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File file = new File(uploadPath + File.separator + video.getVideoFile());
                if (file.exists()) {
                    file.delete();
                }

                dao.delete(videoId, currentUser);
                resp.sendRedirect(req.getContextPath() + getHomeUrl(currentUser.getRoleid()));
            } else {
                resp.sendError(400, "Hành động không hợp lệ");
            }
        } catch (Exception e) {
            resp.sendError(500, "Lỗi server: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        // Chặn user/manager
        if (!isAdmin(currentUser)) {
            resp.sendError(403, "Bạn không có quyền truy cập chức năng này!");
            return;
        }

        String path = req.getServletPath();

        try {
            if (path.equals("/video/create")) {
                String title = req.getParameter("title");
                String description = req.getParameter("description");
                String cateIdParam = req.getParameter("cateId"); // Thay đổi từ cate_id thành cateId
                Part filePart = req.getPart("videoFile");

                if (title == null || title.trim().isEmpty() || cateIdParam == null || filePart == null) {
                    req.setAttribute("error", "Thiếu thông tin cần thiết");
                    req.setAttribute("categories", categoryDAO.findAll());
                    req.getRequestDispatcher("/video/create.jsp").forward(req, resp);
                    return;
                }

                int cateId;
                try {
                    cateId = Integer.parseInt(cateIdParam);
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID danh mục không hợp lệ");
                    req.setAttribute("categories", categoryDAO.findAll());
                    req.getRequestDispatcher("/video/create.jsp").forward(req, resp);
                    return;
                }

                Category category = categoryDAO.findById(cateId);
                if (category == null) {
                    req.setAttribute("error", "Danh mục không tồn tại");
                    req.setAttribute("categories", categoryDAO.findAll());
                    req.getRequestDispatcher("/video/create.jsp").forward(req, resp);
                    return;
                }

                // Lưu file video vào thư mục /upload
                String fileName = UUID.randomUUID().toString() + ".mp4";
                String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + File.separator + fileName);

                // Lưu thông tin video vào DB
                Video video = new Video();
                video.setTitle(title);
                video.setDescription(description);
                video.setVideoFile(fileName);
                video.setCategory(category);
                video.setUser(currentUser);
                video.setUploadDate(new Date());

                dao.create(video);
                resp.sendRedirect(req.getContextPath() + getHomeUrl(currentUser.getRoleid()));
            } else {
                resp.sendError(400, "Hành động không hợp lệ");
            }
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi server: " + e.getMessage());
            req.setAttribute("categories", categoryDAO.findAll());
            req.getRequestDispatcher("/video/create.jsp").forward(req, resp);
        }
    }

    private String getHomeUrl(int roleid) {
        switch (roleid) {
            case 1:
                return "/user/home";
            case 2:
                return "/manager/home";
            case 3:
                return "/admin/home";
            default:
                return "/login";
        }
    }
}