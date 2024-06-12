package com.example.project.controller;

import com.example.project.Repository.rolesRepository;
import com.example.project.Repository.usersRepository;
import com.example.project.Service.usersService;
import com.example.project.entity.roles;
import com.example.project.entity.users;
import jakarta.annotation.Nullable;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller

public class usersController {
    @Autowired
    private usersService Uservice;
    @Autowired
    private rolesRepository rolesRepository;
    @Autowired
    private usersRepository userRepository;

    @GetMapping("/manage-account")
    public String getListUsers(Model model) {
        List<users> users = Uservice.getAllUsers();
        model.addAttribute("users", users);
        return "manage-account";
    }

    @PostMapping("/changeRole/{id}")
    public String ChangeRolesPage(@PathVariable int id, @RequestParam int roleID,Model model) {
         Uservice.updateRole(id,roleID);
         model.addAttribute("mess", "update Role successfully");
         return "redirect:/manage-account";
    }

    @DeleteMapping("/manage-account/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable int id) {
        Uservice.deleteUsers(id);
    }

    @GetMapping("/view_account/{id}")
    public String viewAccount(@PathVariable int id, Model model) {
        Optional<users> users = userRepository.findById(id);
        if (users.isPresent()) {
            model.addAttribute("user", users.get());
            return "details_account";
        }else {
            return "redirect:/ERROR";
        }
    }

    @GetMapping("/create_account")
    public String showPageCreateUser(Model model) {
        model.addAttribute("user", new users());
        return "CreateAccount";
    }

    @PostMapping("/user")
    public String createUser(@ModelAttribute users user, @RequestParam String password, Model model) {
        if (Uservice.checkAccount(user.getEmail())){
            String encodePass = Uservice.encodePassword(password);
            user.setPassword(encodePass);
            userRepository.save(user);
            model.addAttribute("message","Create Account Successfully!");
            return "redirect:/create_account";
        }else {
            model.addAttribute("message","Email is existed!");
            return "redirect:/create_account";
        }


    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("key") String key, Model model) {
        List<users> users = userRepository.findByEmail(key);
        List<users> usersF = userRepository.findByFullname(key);
        if (!users.isEmpty()) {
            model.addAttribute("searchUser", users);
        }
        else if (!usersF.isEmpty()) {
            model.addAttribute("searchUser", usersF);
        }
        return "manage-account";
    }


}
