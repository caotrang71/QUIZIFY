package com.example.demo.Controller;

import com.example.demo.Entity.Users;
import com.example.demo.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable int id, Model model) {
        Optional<Users> user = usersRepository.findById(id);
        model.addAttribute("user", user);
        return "profile";
    }
}