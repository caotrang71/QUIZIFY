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

@Controller
public class AccountController {
    @Autowired
    private AccountService accountService;
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
        }
        return "manage-account";
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
        if (accountService.checkAccount(email)){
            accountService.saveAccount(fullName,email,username,password,true,roleSelect);
            emailService.sendMail(email);
            return "redirect:/manage_account";
        }else {
            redirectAttributes.addFlashAttribute("message","Email already existed!");
            return "redirect:/create_account";
        }
    }


}
