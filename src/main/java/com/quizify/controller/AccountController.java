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

    @GetMapping("/Home")
    public String ShowHomepage(Model model) {
        return "HomePage";
    }

    @GetMapping("/manage_account")
    public String getListUsers(Model model) {
        List<User> users = accountService.getAllUsers();
        model.addAttribute("users", users);
        return "manage-account";
    }

    @PostMapping("/changeRole/{id}")
    public String ChangeRolesPage(@PathVariable int id, @RequestParam int roleID, Model model) {
        Role role = roleRepository.getReferenceById(roleID);
        accountService.updateRole(id,role);
        model.addAttribute("mess", "update Role successfully");
        return "redirect:/manage_account";
    }

    @DeleteMapping("/manage-account/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable int id) {
        accountService.deleteUsers(id);
    }

    @GetMapping("/view_account/{id}")
    public String viewAccount(@PathVariable long id, Model model) {
        Optional<User> users = Optional.ofNullable(userService.findById(id));
        if (users.isPresent()) {
            model.addAttribute("user", users.get());
            return "details_account";
        }else {
            return "redirect:/ERROR";
        }
    }

    @GetMapping("/create_account")
    public String showPageCreateUser(Model model) {
        model.addAttribute("user", new User());
        return "CreateAccount";
    }

    @PostMapping("/user")
    public String createUser(@ModelAttribute User user, @RequestParam String password, Model model) {
        if (accountService.checkAccount(user.getEmail())){
            String encodePass = accountService.encodePassword(password);
            user.setPassword(encodePass);
//            emailService.sendMail(user.getEmail());
            userService.save(user);
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
