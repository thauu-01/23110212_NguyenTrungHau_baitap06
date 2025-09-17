package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.entity.Video;
import com.example.demo.service.CategoryService;
import com.example.demo.service.VideoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Controller
public class VideoController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private VideoService videoService;

    @GetMapping("/video/create")
    public String createVideo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleid() != 3) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.findAll());
        return "video/create";
    }

    @PostMapping("/video/create")
    public String doCreateVideo(@RequestParam String title, @RequestParam String description, @RequestParam MultipartFile videoFile, @RequestParam int cateId, HttpSession session, Model model) throws IOException {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRoleid() != 3) {
            return "redirect:/login";
        }
        Category category = categoryService.findById(cateId);
        if (category == null) {
            model.addAttribute("error", "Danh mục không tồn tại");
            model.addAttribute("categories", categoryService.findAll());
            return "video/create";
        }

        String fileName = UUID.randomUUID() + ".mp4";
        File uploadDir = new File("src/main/webapp/uploads");
        if (!uploadDir.exists()) uploadDir.mkdirs();
        videoFile.transferTo(new File(uploadDir, fileName));

        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoFile(fileName);
        video.setCategory(category);
        video.setUser(user);
        video.setUploadDate(new Date());

        videoService.create(video);
        return "redirect:/admin/home";
    }

    // Thêm @GetMapping("/video/delete"), /video/view tương tự
}