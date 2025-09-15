package servlet;

import dao.UserDAO;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");


        User user = userDAO.login(username, password);

        if (user != null) {
     
            HttpSession session = req.getSession();
            session.setAttribute("user", user);

        
            String redirectPath;
            switch (user.getRoleid()) {
                case 1:
                    redirectPath = "/user/home";
                    break;
                case 2:
                    redirectPath = "/manager/home";
                    break;
                case 3:
                    redirectPath = "/admin/home";
                    break;
                default:
                    redirectPath = "/login?error=invalid_role";
                    break;
            }


            resp.sendRedirect(req.getContextPath() + redirectPath);
        } else {
 
            req.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng!");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
