package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.*;
import com.quizify.service.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
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
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/show_page_login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "Login_page";
    }
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        RedirectAttributes redirectAttributes
                        , HttpSession session) {

        // Kiểm tra xem người dùng có tồn tại không
        User user = userService.findByEmail(email);
        if (user != null && userService.checkPasswordEncoder(password, user.getPassword())) {
            // Nếu email và mật khẩu khớp, chuyển hướng đến trang home
            session.setAttribute("user", user);
            return "redirect:/userHome";
        } else {
            // Nếu không khớp, hiển thị thông báo lỗi và chuyển lại trang đăng nhập
            redirectAttributes.addFlashAttribute("mess", "Invalid email or password");
            return "redirect:/show_page_login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session,RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            session.invalidate();
            redirectAttributes.addFlashAttribute("mess", "You have been logged out");
            return "redirect:/show_page_login";
        }else {
            return "error";
        }
    }
    @GetMapping("/profile/{id}")
    public String profile(@PathVariable long id, Model model,HttpSession session) {
        User userSession = (User) session.getAttribute("user");
        if (userSession.getId() == id) {
            Optional<User> user = userRepository.findById(id);
            model.addAttribute("user", user);
            return "profile";
        }else {
            return "error";
        }

    }
    @PostMapping("/update_profile")
    public String updateProfile(@RequestParam long id,
                                @RequestParam String fullName,
                                @RequestParam String birthdate,
                                @RequestParam String gender,
                                @RequestParam String username
            ,Model model) {
        Boolean genderValue;
        if ("Male".equals(gender)) {
            genderValue = true;
        } else {
            genderValue = false;
        }
        userService.updateProfile(id,fullName,birthdate,genderValue,username);
        model.addAttribute("message", "Profile updated successfully!");
        return "redirect:/profile/"+id;
    }
    @GetMapping("/change_pass/{email}")
    public String showChangePasswordForm(@PathVariable String email,
                                         Model model,HttpSession session) {

        User userSession =(User) session.getAttribute("user");
        if (userSession.getEmail().equals(email)) {
            User user = userRepository.findByEmail(email);
            model.addAttribute("user", user);
            return "ChangePassword";
        }else {
            return "error";
        }
    }

    @PostMapping("/changepassword/{email}")
    public String changePassword(@PathVariable String email,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String ConfirmPassword,
                                 Model model) {
        User user = userRepository.findByEmail(email);
        if (userService.changePassword(email, oldPassword, newPassword, ConfirmPassword)) {
            model.addAttribute("mess", "Change password successfully!");

            return "redirect:/profile/"+user.getId(); // Chuyển hướng đến trang profile nếu thay đổi thành công
        } else {
            model.addAttribute("mess", "Change password failed!");
            return "redirect:/change_pass?error"; // Chuyển hướng lại trang thay đổi mật khẩu với thông báo lỗi
        }
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user
                                                    ,RedirectAttributes redirectAttributes) {

        // Kiểm tra xem email đã tồn tại chưa

        if (userService.userExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("message", "Email already exists");
            return "redirect:/show_page_login";
        }
        // Tạo OTP và gửi email
        OTP otp = otpService.createOTP(user.getEmail());
        emailService.sendOTPEmail(user.getEmail(), otp.getOtp());
        String email = user.getEmail();
        String password = passwordEncoder.encode(user.getPassword());
        String fullname = user.getFullName();
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
                            @RequestParam String fullname, @RequestParam String otp,
                            Model model,RedirectAttributes redirectAttributes) {
        Optional<OTP> otpObjectOptional = otpService.getObjectOTP(email);

        if (otpObjectOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mess", "OTP không tồn tại hoặc đã hết hạn.");
            return "redirect:/showVerifyOTP?email=" + email + "&password=" + pass + "&fullname=" + fullname;
        }

        OTP otpObject = otpObjectOptional.get();

        // Kiểm tra nếu OTP đã hết hạn hoặc vượt quá số lần thử
        if (otpObject.getAttempts() >= 2 || Duration.between(otpObject.getExpriTime(), LocalDateTime.now()).toSeconds() >= 59) {
            otpService.deleteOTP(email);
            redirectAttributes.addFlashAttribute("mess", "OTP đã hết hạn hoặc bạn đã nhập sai quá 3 lần.");
            return "redirect:/show_page_login"; // Redirect đến trang đăng ký nếu OTP hết hạn hoặc vượt quá số lần thử
        }

        // Kiểm tra nếu OTP nhập vào đúng
        if (otpObject.getOtp().equals(otp)) {
            userService.registerUser(email, fullname, pass, true, roleRepository.getReferenceById(3));
            otpService.deleteOTP(email);
            redirectAttributes.addFlashAttribute("success","register successfully!");
            return "redirect:/show_page_login"; // Redirect đến trang chủ nếu OTP đúng
        } else {
            // Tăng số lần thử nếu OTP sai
            otpObject.setAttempts(otpObject.getAttempts() + 1);
            otpRepository.save(otpObject);
            redirectAttributes.addFlashAttribute("error", "OTP không chính xác.");
            return "redirect:/showVerifyOTP?email=" + email + "&password=" + pass + "&fullname=" + fullname; // Redirect để nhập lại OTP
        }
    }
    @GetMapping("/show_page_forgetPassword")
    public String showpageForgetPassword(Model model) {
        model.addAttribute("user", new User());
        return "forgetPass";
    }
    @PostMapping("/forget_password")
    public String forgetPassword(@RequestParam String email,
                                 RedirectAttributes redirectAttributes) {

        User user = userService.findByEmail(email);
        if (user != null) {
            //tạo otp và gửi otp
            OTP otpObject = otpService.createOTP(email);
            emailService.sendOTPEmail(email, otpObject.getOtp());
            return "redirect:/show_verifyOTP_forget?email="+email;
        }else {
            redirectAttributes.addFlashAttribute("error", "Email does not exist");
            return "redirect:/show_page_forgetPassword";
        }
    }
    @GetMapping("/show_verifyOTP_forget")
    public String showverifyOTP(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "verifyForgetPassOTP";
    }
    @PostMapping("/verifyOTP_resetPass")
    public String verifyOTPResetPass(@RequestParam String email,@RequestParam String otp,
                                     Model model,RedirectAttributes redirectAttributes) {

        Optional<OTP> otpObjectOptional = otpService.getObjectOTP(email);

        if (otpObjectOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("mess", "OTP không tồn tại hoặc đã hết hạn.");
            return "redirect:/show_page_login";
        }

        OTP otpObject = otpObjectOptional.get();

        // Kiểm tra nếu OTP đã hết hạn hoặc vượt quá số lần thử
        if (otpObject.getAttempts() >= 2 || Duration.between(otpObject.getExpriTime(), LocalDateTime.now()).toSeconds() >= 59) {
            otpService.deleteOTP(email);
            redirectAttributes.addFlashAttribute("mess", "OTP đã hết hạn hoặc bạn đã nhập sai quá 3 lần.");
            return "redirect:/show_page_login"; // Redirect đến trang đăng ký nếu OTP hết hạn hoặc vượt quá số lần thử
        }

        // Kiểm tra nếu OTP nhập vào đúng
        if (otpObject.getOtp().equals(otp)) {
            otpService.deleteOTP(email);
            return "redirect:/show_reset_Password?email="+email; // Redirect đến trang reset nếu OTP đúng
        } else {
            // Tăng số lần thử nếu OTP sai
            otpObject.setAttempts(otpObject.getAttempts() + 1);
            otpRepository.save(otpObject);
            redirectAttributes.addFlashAttribute("error", "OTP không chính xác.");
            return "redirect:/show_verifyOTP_forget?email=" + email; // Redirect để nhập lại OTP
        }
    }
    @GetMapping("/show_reset_Password")
    public String showresetPassword(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "resetPassword";
    }
    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword ,Model model) {
        User user = userService.findByEmail(email);
        if (user != null && newPassword.equals(confirmPassword)) {
            String encodepass = passwordEncoder.encode(newPassword);
            user.setPassword(encodepass);
            userRepository.save(user);
            return "redirect:/show_page_login";
        }else {
            model.addAttribute("mess","password doesn't match");
            return "redirect:/show_reset_Password?email=" + email;
        }

    }


}