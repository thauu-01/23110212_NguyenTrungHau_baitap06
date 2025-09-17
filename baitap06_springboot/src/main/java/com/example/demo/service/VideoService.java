package com.example.demo.service;

import com.example.demo.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VideoService {
    Page<Video> findByTitleContaining(String keyword, Pageable pageable);
    List<Video> findByUserId(int userId);
    Video create(Video video);
    Video update(Video video);
    void delete(int id);
    List<Video> findAll();
    Video findById(int id); 
}