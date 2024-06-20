package com.example.demo.Controller;

import com.example.demo.Entity.OTP;
import com.example.demo.Entity.Users;
import com.example.demo.Repository.OTPRepository;
import com.example.demo.Repository.UsersRepository;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.OTPService;
import com.example.demo.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Controller
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OTPService otpService;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/home")
    public String showhome() {
        return "HomePage";
    }
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable int id, Model model) {
        Optional<Users> user = usersRepository.findById(id);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/change_pass/{email}")
    public String showChangePasswordForm(@PathVariable String email, Model model) {
        Users user = usersRepository.findByEmail(email);
        model.addAttribute("user", user);
        return "ChangePassword";
    }

    @PostMapping("/changepassword/{email}")
    public String changePassword(@PathVariable String email,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String ConfirmPassword,
                                 Model model) {
        if (userService.changePassword(email, oldPassword, newPassword, ConfirmPassword)) {
            model.addAttribute("mess", "Change password successfully!");

            return "redirect:/profile/4"; // Chuyển hướng đến trang profile nếu thay đổi thành công
        } else {
            model.addAttribute("mess", "Change password failed!");
            return "redirect:/change_pass?error"; // Chuyển hướng lại trang thay đổi mật khẩu với thông báo lỗi
        }
    }

    @GetMapping("show_page_login")
    public String showLoginForm() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model) {
        // Kiểm tra xem người dùng có tồn tại không
        Users user = userService.findByEmail(email);
        if (user != null && userService.checkPasswordEncoder(password, user.getPassword())) {
            // Nếu email và mật khẩu khớp, chuyển hướng đến trang home
            model.addAttribute("user", user);
            return "redirect:/profile/4";
        } else {
            // Nếu không khớp, hiển thị thông báo lỗi và chuyển lại trang đăng nhập
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/login?error";
        }
    }

    @PostMapping("/update_profile")
    public String updateProfile(@RequestParam int id,
                                @RequestParam String fullname,
                                @RequestParam String birthdate,
                                @RequestParam String  gender,
                                @RequestParam String username
                                ,Model model) {
        int gen;
        if (gender.equals("Female")){
             gen = 0;
        }else{
             gen = 1;
        }
        userService.updateProfile(id,fullname,birthdate,gen,username);
        model.addAttribute("message", "Profile updated successfully!");
        return "redirect:/profile/4";
    }

    @GetMapping("/show_page_register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") Users user
                                                    ,Model model) {

        // Kiểm tra xem email đã tồn tại chưa

        if (userService.userExists(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "redirect:/show_page_register";
        }
        // Tạo OTP và gửi email
        OTP otp = otpService.createOTP(user.getEmail());
        emailService.sendOTPEmail(user.getEmail(), otp.getOtp());
        String email = user.getEmail();
        String password = user.getPassword();
        String fullname = user.getFullname();
        return "redirect:/showVerifyOTP?email=" + email + "&password=" + password + "&fullname=" + fullname;
    }

    @GetMapping("/showVerifyOTP")
    public String showVerifyPage(@RequestParam String email,@RequestParam String password,
            @RequestParam String fullname, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("fullname", fullname);
        model.addAttribute("pass", password);
        return "verify";
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam String email, @RequestParam String pass,
                            @RequestParam String fullname, @RequestParam String otp, Model model) {
        Optional<OTP> otpObjectOptional = otpService.getObjectOTP(email);

        if (otpObjectOptional.isEmpty()) {
            model.addAttribute("error", "OTP không tồn tại hoặc đã hết hạn.");
            return "redirect:/showVerifyOTP?email=" + email + "&password=" + pass + "&fullname=" + fullname;
        }
        if (userService.findByEmail(email) != null){
            model.addAttribute("error", "Email already exists");
            return "redirect:/show_page_register";
        }

        OTP otpObject = otpObjectOptional.get();

        // Kiểm tra nếu OTP đã hết hạn hoặc vượt quá số lần thử
        if (otpObject.getAttempts() >= 3 || Duration.between(otpObject.getExpriTime(), LocalDateTime.now()).toSeconds() >= 60) {
            otpService.deleteOTP(email);
            model.addAttribute("error", "OTP đã hết hạn hoặc bạn đã nhập sai quá 3 lần.");
            return "redirect:/show_page_register"; // Redirect đến trang đăng ký nếu OTP hết hạn hoặc vượt quá số lần thử
        }

        // Kiểm tra nếu OTP nhập vào đúng
        if (otpObject.getOtp().equals(otp)) {
            userService.registerUser(email, fullname, pass, 1, 3);
            otpService.deleteOTP(email);

            return "redirect:/show_page_login"; // Redirect đến trang chủ nếu OTP đúng
        } else {
            // Tăng số lần thử nếu OTP sai
            otpObject.setAttempts(otpObject.getAttempts() + 1);
            otpRepository.save(otpObject);
            model.addAttribute("error", "OTP không chính xác.");
            return "redirect:/showVerifyOTP?email=" + email + "&password=" + pass + "&fullname=" + fullname; // Redirect để nhập lại OTP
        }
    }






}