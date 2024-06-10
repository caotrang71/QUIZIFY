package com.example.demo.Controller;

import com.example.demo.Entity.Users;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable int id, Model model) {
        Optional<Users> user = usersRepository.findById(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/change_pass/{username}")
    public String showChangePasswordForm(@PathVariable String username, Model model) {
        Users user = usersRepository.findByUsername(username);
        model.addAttribute("user", user);
        return "ChangePassword";
    }

    @PostMapping("/changepassword/{username}")
    public String changePassword(@PathVariable String username,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String ConfirmPassword,
                                 Model model) {
        if (userService.changePassword(username, oldPassword, newPassword, ConfirmPassword)) {
            model.addAttribute("mess", "Change password successfully!");

            return "redirect:/profile/1"; // Chuyển hướng đến trang profile nếu thay đổi thành công
        } else {
            model.addAttribute("mess", "Change password failed!");
            return "redirect:/change_pass?error"; // Chuyển hướng lại trang thay đổi mật khẩu với thông báo lỗi
        }
    }

}