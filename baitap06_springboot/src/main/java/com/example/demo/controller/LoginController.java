package com.example.demo.controller;

import com.example.demo.entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Nếu đã login thì chuyển hướng về home theo role
            return "redirect:" + getHomeUrl(user.getRoleid());
        }
        return "login"; // Trả về login.jsp
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "redirect:" + getHomeUrl(user.getRoleid());
    }

    private String getHomeUrl(int roleid) {
        switch (roleid) {
            case 1: return "/user/home";
            case 2: return "/manager/home";
            case 3: return "/admin/home";
            default: return "/login";
        }
    }
}
