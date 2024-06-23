package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.*;
import com.quizify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public String getListUsers(Model model) {
        List<User> users = accountService.getAllUsers();
        model.addAttribute("users", users);
        return "manage-account";
    }

    @PostMapping("/changeRole/{id}")
    public String ChangeRolesPage(@PathVariable long id, @RequestParam int roleID, Model model) {
        Role role = roleRepository.getReferenceById(roleID);
        accountService.updateRole(id,role);
        model.addAttribute("mess", "update Role successfully");
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
                                @RequestParam("roleID") int roleSelect,Model model) {
        if (accountService.checkAccount(email)){
            accountService.saveAccount(fullName,email,username,password,true,roleSelect);
            emailService.sendMail(email);
            return "redirect:/manage_account";
        }else {
            model.addAttribute("message","Email is existed!");
            return "redirect:/create_account";
        }
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("key") String key, Model model) {
        List<User> users = accountService.findByEmail(key);
        List<User> usersF = accountService.findByFullNameContainingIgnoreCase(key);
        if (!users.isEmpty()) {
            model.addAttribute("searchUser", users);
        }
        else if (!usersF.isEmpty()) {
            model.addAttribute("searchUser", usersF);
        }
        return "manage-account";
    }


}
