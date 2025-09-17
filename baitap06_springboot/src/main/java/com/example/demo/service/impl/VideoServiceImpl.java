package com.example.demo.service.impl;

import com.example.demo.entity.Video;
import java.util.List;
import com.example.demo.repository.VideoRepository;
import com.example.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoRepository videoRepository;

    @Override
    public Page<Video> findByTitleContaining(String keyword, Pageable pageable) {
        return videoRepository.findByTitleContaining(keyword, pageable);
    }

    @Override
    public List<Video> findByUserId(int userId) {
        return videoRepository.findByUserId(userId);
    }

    @Override
    public Video create(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Video update(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public void delete(int id) {
        videoRepository.deleteById(id);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll(); 
    }
}