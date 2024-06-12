package com.example.demo.Controller;

import com.example.demo.Entity.Users;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    @GetMapping("/show_page_register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String fullname, @RequestParam String birthdate,
                                           @RequestParam int gender, @RequestParam String email,
                                           @RequestParam String username, @RequestParam String password) {
        // Kiểm tra xem email đã tồn tại chưa
        Users existingUser = userService.findByEmail(email);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        // Mã hoá mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = passwordEncoder.encode(password);

        // Tạo một đối tượng User mới và lưu vào cơ sở dữ liệu
        Users newUser = new Users();
        newUser.setFullname(fullname);
        newUser.setBirthdate(birthdate);
        newUser.setGender(gender);
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(encodedPassword); // Lưu mật khẩu đã mã hoá
        newUser.setRole_id(3); // Mặc định là role user

        try {
            // Thực hiện lưu mới người dùng vào cơ sở dữ liệu
            userService.save(newUser);
            return ResponseEntity.ok("Registration successful");
        } catch (DataIntegrityViolationException e) {
            // Xử lý nếu có lỗi xảy ra khi lưu vào cơ sở dữ liệu
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user");
        }
    }
    @GetMapping("show_page_login")
    public String showLoginForm(Model model) {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        // Kiểm tra xem người dùng có tồn tại không
        Users user = userService.findByEmail(email);
        if (user != null && userService.checkPasswordEncoder(password, user.getPassword())) {
            // Nếu email và mật khẩu khớp, chuyển hướng đến trang home
            model.addAttribute("user", user);
            return "landing-page";
        } else {
            // Nếu không khớp, hiển thị thông báo lỗi và chuyển lại trang đăng nhập
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @PostMapping("/update_profile")
    public String updateProfile(@RequestParam int id,
                                @RequestParam String fullname,
                                @RequestParam String birthdate,
                                @RequestParam String  gender,
                                @RequestParam String username
            , Model model) {
        int gen;
        if (gender.equals("Female")){
             gen = 0;
        }else{
             gen = 1;
        }
        userService.updateProfile(id,fullname,birthdate,gen,username);
        model.addAttribute("message", "Profile updated successfully!");
        return "redirect:/profile/1";
    }

}