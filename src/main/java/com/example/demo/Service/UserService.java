package com.example.demo.Service;

import com.example.demo.Entity.Users;
import com.example.demo.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UsersRepository UsersRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean changePassword(String email,String oldPassword, String newPassword, String comfirmpass) {
        // Kiểm tra mật khẩu cũ
        Users user = UsersRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(oldPassword)) {
            return false; // Mật khẩu cũ không đúng
        }

        // Kiểm tra mật khẩu mới và mật khẩu xác nhận
        if (!newPassword.equals(comfirmpass)) {
            return false; // Mật khẩu mới và mật khẩu xác nhận không khớp
        }

        // Cập nhật mật khẩu mới trong cơ sở dữ liệu
        user.setPassword(newPassword);
        UsersRepository.save(user);
        return true;
    }

    public Users findByEmail(String email) {
        return UsersRepository.findByEmail(email);
    }

    public void save(Users user) {
        UsersRepository.save(user);
    }

    public boolean checkPasswordEncoder(String password, String encoderpassword) {
        return passwordEncoder.matches(password, encoderpassword);
    }
    public void updateProfile(int id,String fullname, String birthdate, int gender, String username) {
        Users userExists = UsersRepository.findById(id).orElseThrow(null);
        if (userExists != null) {
            userExists.setFullname(fullname);
            userExists.setBirthdate(birthdate);
            userExists.setGender(gender);
            userExists.setUsername(username);
            UsersRepository.save(userExists);
        }

    }
}
