package com.quizify.service;


import com.quizify.model.*;
import com.quizify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean changePassword(String email, String oldPassword, String newPassword, String confirmPassword) {
        // Kiểm tra người dùng tồn tại hay không
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false; // Người dùng không tồn tại
        }

        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false; // Mật khẩu cũ không đúng
        }

        // Kiểm tra mật khẩu mới và mật khẩu xác nhận
        if (!newPassword.equals(confirmPassword)) {
            return false; // Mật khẩu mới và mật khẩu xác nhận không khớp
        }

        // Mã hóa mật khẩu mới
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        // Cập nhật mật khẩu mới trong cơ sở dữ liệu
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        return true;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        User usernew = new User();

        usernew.setEmail(user.getEmail());
        usernew.setFullName(user.getFullName());
        usernew.setUsername(user.getUsername());
        usernew.setPassword(passwordEncoder.encode(user.getPassword()));
        usernew.setGender(user.isGender());
        usernew.setBirthdate(user.getBirthdate());
        userRepository.save(usernew);
    }

    public boolean checkPasswordEncoder(String password, String encoderpassword) {
        return passwordEncoder.matches(password, encoderpassword);
    }
    public void updateProfile(long id,String fullname, String birthdate, boolean gender, String username) {
        User userExists = userRepository.findById(id).orElseThrow(null);
        if (userExists != null) {
            userExists.setFullName(fullname);
            userExists.setBirthdate(birthdate);
            userExists.setGender(gender);
            userExists.setUsername(username);
            userRepository.save(userExists);
        }
    }

    public void registerUser(String email, String fullname, String password,boolean status,Role role) {
        User user = new User();
        user.setEmail(email);
        user.setFullName(fullname);
        user.setPassword(password);
        user.setStatus(status);
        user.setRole(role);
        userRepository.save(user);
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
//
//    @Override
//    public List<User> findAll() {
//        return userRepository.findAll();
//    }
//
//    @Override
//    public User save(User user) {
//        return userRepository.save(user);
//    }user
}
