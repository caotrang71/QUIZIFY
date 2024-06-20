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

    public boolean changePassword(String email, String oldPassword, String newPassword, String confirmPassword) {
        // Kiểm tra người dùng tồn tại hay không
        Users user = UsersRepository.findByEmail(email);
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
        UsersRepository.save(user);

        return true;
    }

    public Users findByEmail(String email) {
        return UsersRepository.findByEmail(email);
    }

    public void save(Users user) {
        Users usernew = new Users();

        usernew.setEmail(user.getEmail());
        usernew.setUsername(user.getFullname());
        usernew.setUsername(user.getUsername());
        usernew.setPassword(passwordEncoder.encode(user.getPassword()));
        usernew.setGender(user.getGender());
        usernew.setBirthdate(user.getBirthdate());
        UsersRepository.save(usernew);
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

    public void registerUser(String email, String fullname, String password,int status,int role) {
        Users user = new Users();
        user.setEmail(email);
        user.setFullname(fullname);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setStatus(status);
        user.setRole_id(role);
        UsersRepository.save(user);
    }

    public boolean userExists(String email) {
        return UsersRepository.findByEmail(email) != null;
    }
}
