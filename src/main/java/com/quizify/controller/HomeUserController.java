package com.quizify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@Controller
public class HomeUserController {

    @GetMapping("/userHome")
    public String viewHomeUser(Model model) {
        return "HomePage";
    }

    @GetMapping("/")
    public String viewLandingPage(Model model) {
        return "landing-page";
    }

}
