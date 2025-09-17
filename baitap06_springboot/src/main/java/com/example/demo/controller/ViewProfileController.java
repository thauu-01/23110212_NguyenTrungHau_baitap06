package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewProfileController {

    @GetMapping({"/user/view-profile", "/manager/view-profile", "/admin/view-profile"})
    public String viewProfile(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "views/view-profile";
    }
}