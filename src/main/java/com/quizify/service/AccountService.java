package com.quizify.service;


import com.quizify.model.*;
import com.quizify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUsers(long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.deleteById(id);
        }
    }

    public void updateRole(long id,Role role) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            user.setRole(role);
            userRepository.save(user);
        }
    }

    public String encodePassword(String password) {
            return passwordEncoder.encode(password);
    }

    public boolean checkAccount(String email) {
        List<User> listUser =accountRepository.findByEmail(email);
        if (listUser.isEmpty()) {
            return true;
        }else {
            return false;
        }
    }

    public List<User> findByFullNameContainingIgnoreCase(String fullName){
        return accountRepository.findByFullNameContainingIgnoreCase(fullName);
    }

    public List<User> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

}
