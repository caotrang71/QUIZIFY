package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.*;
import com.quizify.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;


    @GetMapping("/manage_account")
    public String getListUsers(Model model, HttpSession session) {
        User userLogin = (User) session.getAttribute("user");
        if (userLogin.getRole().getId() == 1 || userLogin.getRole().getId()==2) {
            List<User> users = accountService.getAllUsers();
            model.addAttribute("users", users);
            return "manage-account";
        }else {
            return "error";
        }

    }

    @PostMapping("/changeRole/{id}")
    public String ChangeRolesPage(@PathVariable long id, @RequestParam int roleID,
                                  RedirectAttributes redirectAttributes) {

        Role role = roleRepository.getReferenceById(roleID);
        accountService.updateRole(id,role);

        String mess = "update role successfully";
        redirectAttributes.addFlashAttribute("mess", mess);

        return "redirect:/manage_account";
    }

    @DeleteMapping("/manage-account/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable long id) {
        accountService.deleteUsers(id);
    }

    @GetMapping("/view_account/{id}")
    public String viewAccount(@PathVariable long id, Model model) {
        User users = userService.findById(id);
        if (users != null) {
            model.addAttribute("user", users);
            return "details_account";
        }else {
            return "redirect:/ERROR";
        }
    }

    @GetMapping("/create_account")
    public String showPageCreateUser() {
        return "CreateAccount";
    }

    @PostMapping("/user")
    public String createAccount(@RequestParam String fullName,
                                @RequestParam String email,
                                @RequestParam String username,
                                @RequestParam String password,
                                @RequestParam("roleID") int roleSelect,
                                RedirectAttributes redirectAttributes) {

        //kiểm tra độ dài mật khẩu và kí tự đặc biệt
        String regexPass = "^[a-zA-Z0-9][a-zA-Z0-9\\-_@&]{6,14}[a-zA-Z0-9]$";
        String inputPass = password;

        Pattern patternPass = Pattern.compile(regexPass);
        Matcher matcherPass = patternPass.matcher(inputPass);
        //kiểm tra email có đúng format không
        String regexEmail = "^[a-zA-Z0-9]+@gmail\\.com$";
        String inputEmail =  email;

        Pattern patternEmail = Pattern.compile(regexEmail);
        Matcher matcherEmail = patternEmail.matcher(inputEmail);

        if (accountService.checkAccount(email)){
            if (!matcherEmail.matches()) {
                redirectAttributes.addFlashAttribute("message", "Your email must end in .com and contain only numbers and letters.");
                return "redirect:/create_account";
            }else if (!matcherPass.matches()){
                redirectAttributes.addFlashAttribute("message", "You must enter a password of 8-16 characters including letters, numbers," +
                        " some special characters such as - _ @ & and cannot begin or end with those characters");
                return "redirect:/create_account";
            }else {
                accountService.saveAccount(fullName, email, username, password, true, roleSelect);
                emailService.sendMail(email);
                return "redirect:/manage_account";
            }
        }else {
            redirectAttributes.addFlashAttribute("message","Email already existed!");
            return "redirect:/create_account";
        }
    }

    @PutMapping("/manager_account/ban/{id}")
    @ResponseBody
    public void banAccount(@PathVariable long id){
        User user = userService.findById(id);
        if (user != null){
            user.setStatus(false);
            userRepository.save(user);
        }
    }

    @PutMapping("/manager_account/active/{id}")
    @ResponseBody
    public void activeAccount(@PathVariable long id){
        User user = userService.findById(id);
        if (user != null){
            user.setStatus(true);
            userRepository.save(user);
        }
    }


}
