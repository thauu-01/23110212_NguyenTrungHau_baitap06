package com.example.demo.service;

import com.example.demo.entity.User;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> login(String username, String password);
    User updateProfile(User user);
}