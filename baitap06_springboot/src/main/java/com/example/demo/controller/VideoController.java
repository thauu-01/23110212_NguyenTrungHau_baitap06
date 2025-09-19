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

    /** Kiểm tra quyền admin */
    private boolean isAdmin(User user) {
        return user != null && user.getRoleid() == 3;
    }

    /** Lấy URL home dựa trên roleid */
    private String getHomeUrl(int roleid) {
        switch (roleid) {
            case 1: return "/user/home";
            case 2: return "/manager/home";
            case 3: return "/admin/home";
            default: return "/login";
        }
    }

    @GetMapping("/video/create")
    public String createVideo(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/login";
        }
        model.addAttribute("categories", categoryService.findAll());
        return "video/create";
    }

    @PostMapping("/video/create")
    public String doCreateVideo(@RequestParam String title,
                                @RequestParam String description,
                                @RequestParam MultipartFile videoFile,
                                @RequestParam int cateId,
                                HttpSession session,
                                Model model) throws IOException {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/login";
        }

        if (title == null || title.trim().isEmpty() || videoFile.isEmpty()) {
            model.addAttribute("error", "Thiếu thông tin cần thiết");
            model.addAttribute("categories", categoryService.findAll());
            return "video/create";
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
        return "redirect:" + getHomeUrl(user.getRoleid());
    }

    @GetMapping("/video/delete")
    public String deleteVideo(@RequestParam int videoId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/login";
        }
        Video video = videoService.findById(videoId); 
        if (video != null) {
            File videoFile = new File("src/main/webapp/uploads/" + video.getVideoFile());
            if (videoFile.exists()) {
                videoFile.delete();
            }
            videoService.delete(videoId);
        }
        return "redirect:" + getHomeUrl(user.getRoleid());
    }

    @GetMapping("/video/view")
    public String viewVideos(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (!isAdmin(user)) {
            return "redirect:/login";
        }
        model.addAttribute("videos", videoService.findAll());
        return "video/view";
    }
}
