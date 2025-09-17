package com.example.demo.service;

import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<Category> findByNameContaining(String keyword, Pageable pageable);
    List<Category> findByUserId(int userId);
    Category create(Category category);
    Category update(Category category);
    void delete(int id);
    List<Category> findAll();
    Category findById(int id); 
}