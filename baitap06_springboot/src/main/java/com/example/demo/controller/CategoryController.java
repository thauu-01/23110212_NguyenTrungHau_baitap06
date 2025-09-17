package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public String createCategory(@RequestParam String cateName, @RequestParam String icons, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Category category = new Category();
        category.setCateName(cateName);
        category.setIcons(icons);
        category.setUser(user);
        categoryService.create(category);
        return "redirect:" + getHomeUrl(user.getRoleid());
    }

    @PostMapping("/update")
    public String updateCategory(@RequestParam int id, @RequestParam String cateName, @RequestParam String icons, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Category category = categoryService.findById(id);
        if (category != null && category.getUser().getId() == user.getId()) {
            category.setCateName(cateName);
            category.setIcons(icons);
            categoryService.update(category);
        }
        return "redirect:" + getHomeUrl(user.getRoleid());
    }

    @GetMapping("/delete")
    public String deleteCategory(@RequestParam int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Category category = categoryService.findById(id);
        if (category != null && category.getUser().getId() == user.getId()) {
            categoryService.delete(id);
        }
        return "redirect:" + getHomeUrl(user.getRoleid());
    }

    @GetMapping("/view")
    public String viewCategory(@RequestParam int id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Category category = categoryService.findById(id);
        if (category != null && category.getUser().getId() == user.getId()) {
            model.addAttribute("category", category);
            return "category/view";
        }
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