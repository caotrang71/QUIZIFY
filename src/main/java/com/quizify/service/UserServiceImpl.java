package com.quizify.service;

import com.quizify.model.User;
import com.quizify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // This method should get the current logged-in user
    @Override
    public User getCurrentUser() {
        // Implementation should fetch the currently logged-in user
        // This is just a placeholder, the actual implementation might involve Spring Security
        return userRepository.findById(1L).orElse(null); // Example to get user with ID 1
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
