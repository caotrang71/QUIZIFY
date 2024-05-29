package com.example.demo.controller;

import com.example.demo.Repository.rolesService;
import com.example.demo.entity.roles;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class rolesController {
    @Autowired
    private rolesService roles;

    @GetMapping("/manage-account")
    public String getAllRole(Model model){
        List<roles> Lroles = roles.getRoles();
        model.addAttribute("roles", Lroles);
        return "manage-account";
    }
}
