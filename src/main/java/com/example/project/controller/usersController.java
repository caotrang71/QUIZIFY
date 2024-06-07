package com.example.project.controller;

import com.example.project.Repository.rolesRepository;
import com.example.project.Service.usersService;
import com.example.project.entity.roles;
import com.example.project.entity.users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manage-account")
public class usersController {
    @Autowired
    private usersService Uservice;
    @Autowired
    private rolesRepository rolesRepository;

    @GetMapping
    public String getListUsers(Model model) {
        List<users> users = Uservice.getAllUsers();
        model.addAttribute("users", users);
        return "manage-account";
    }

    @PutMapping("/{id}/{role_id}")
    public roles ChangeRolesPage(@PathVariable int id,@PathVariable int role_id,@RequestBody roles newRole) {
        return Uservice.changeRoles(id,role_id,newRole);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable int id) {
        Uservice.deleteUsers(id);
    }

}
