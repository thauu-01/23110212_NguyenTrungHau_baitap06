package com.example.demo.repository;

import com.example.demo.entity.Video;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    Page<Video> findByTitleContaining(String keyword, Pageable pageable);
    List<Video> findByUserId(int userId);
}