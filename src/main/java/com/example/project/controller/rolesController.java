//package com.example.project.controller;
//
//import com.example.project.Service.rolesService;
//import com.example.project.entity.roles;
//import org.springframework.ui.Model;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//
//@Controller
//@RequestMapping("/manage-account")
//
//public class rolesController {
//    @Autowired
//    private rolesService roleService;
//    //post
//    @GetMapping
//    public String getAllRole(Model model){
//        List<roles> Lroles = roleService.getRoles();
//        model.addAttribute("roles", Lroles);
//        return "manage-account";
//    }
//
//    //put
//    @PutMapping("/{id}")
//    @ResponseBody
//    public roles updateRole(@PathVariable int id, @RequestBody roles newRole) {
//        return roleService.updateRole(id, newRole);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseBody
//    public void deleteRole(@PathVariable int id)  {
//        roleService.deleteRole(id);
//    }
//
//}
