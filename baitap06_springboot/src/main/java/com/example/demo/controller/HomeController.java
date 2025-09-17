package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.CategoryService;
import com.example.demo.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private VideoService videoService;

    @GetMapping("/user/home")
    public String userHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("videos", videoService.findAll());
        return "user/home";
    }

    @GetMapping("/manager/home")
    public String managerHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.findByUserId(user.getId()));
        model.addAttribute("videos", videoService.findByUserId(user.getId()));
        return "manager/home";
    }

    @GetMapping("/admin/home")
    public String adminHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("videos", videoService.findAll());
        return "admin/home";
    }
}