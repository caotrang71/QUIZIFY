package com.example.project.controller;

import com.example.project.Repository.rolesRepository;
import com.example.project.Repository.usersRepository;
import com.example.project.Service.usersService;
import com.example.project.entity.roles;
import com.example.project.entity.users;
import jakarta.annotation.Nullable;
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

    @PutMapping("/{id}/{role_id}")
    public roles ChangeRolesPage(@PathVariable int id,@PathVariable int role_id,@RequestBody roles newRole) {
        return Uservice.changeRoles(id,role_id,newRole);
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
    public String createUser(@ModelAttribute users users, Model model) {
        userRepository.save(users);
        //model.addAttribute("users", users);
        model.addAttribute("message","Create Account Successfully!");
        return "redirect:/create_account";
    }

    @GetMapping("/search")
    public String searchUser(@RequestParam("fullname") String name, Model model) {
        List<users> users = userRepository.findByUsername(name);
        List<users> usersF = userRepository.findByFullname(name);
        if (!users.isEmpty()) {
            model.addAttribute("searchUser", users);
        }
        else if (!usersF.isEmpty()) {
            model.addAttribute("searchUser", usersF);
        }
        return "manage-account";
    }


}
