package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Controller
public class ProfileController {
    @Autowired
    private UserService userService;

    @GetMapping({"/user/profile", "/manager/profile", "/admin/profile"})
    public String profile(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "views/profile";
    }

    @PostMapping({"/user/profile", "/manager/profile", "/admin/profile"})
    public String doUpdateProfile(@RequestParam String fullname, @RequestParam String phone, @RequestParam MultipartFile image, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        user.setFullname(fullname);
        user.setPhone(phone);
        if (!image.isEmpty()) {
            String fileName = Paths.get(image.getOriginalFilename()).getFileName().toString();
            File uploadDir = new File("src/main/webapp/uploads");
            if (!uploadDir.exists()) uploadDir.mkdirs();
            image.transferTo(new File(uploadDir, fileName));
            user.setImage(fileName);
        }
        userService.updateProfile(user);
        session.setAttribute("user", user);
        switch (user.getRoleid()) {
            case 1: return "redirect:/user/home";
            case 2: return "redirect:/manager/home";
            case 3: return "redirect:/admin/home";
            default: return "redirect:/login";
        }
    }
}