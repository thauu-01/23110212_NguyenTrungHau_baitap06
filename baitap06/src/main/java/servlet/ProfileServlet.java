package servlet;

import dao.UserDAO;
import entity.User;
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
import java.nio.file.Paths;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,  
    maxFileSize = 1024 * 1024 * 10,   
    maxRequestSize = 1024 * 1024 * 50 
)
@WebServlet({"/user/profile", "/manager/profile", "/admin/profile"})
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();
    private static final String UPLOAD_DIR = "uploads";  

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getRequestDispatcher("/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        // Xử lý upload image
        Part filePart = req.getPart("image");  
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        if (!fileName.isEmpty()) {
            // Lưu file vào thư mục
            String appPath = req.getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(appPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            String filePath = appPath + File.separator + fileName;
            filePart.write(filePath);
            currentUser.setImage(fileName);  // Cập nhật tên file vào DB
        }

        // Cập nhật các trường khác
        currentUser.setFullname(fullname);
        currentUser.setPhone(phone);
        userDAO.updateProfile(currentUser);

        // Cập nhật session
        session.setAttribute("user", currentUser);

        // Redirect về home theo role
        String redirectPath = switch (currentUser.getRoleid()) {
            case 1 -> "/user/home";
            case 2 -> "/manager/home";
            case 3 -> "/admin/home";
            default -> "/login";
        };
        resp.sendRedirect(req.getContextPath() + redirectPath);
    }
}
