package com.quizify.service;

import com.quizify.model.User;

import java.util.List;

public interface UserService {
    User getCurrentUser();
    User findById(Long id);
    List<User> findAll();
    User save(User user);
}

