package com.example.demo.repository;

import com.example.demo.entity.Category;
import java.util.List; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Page<Category> findByCateNameContaining(String keyword, Pageable pageable);
    List<Category> findByUserId(int userId);
}