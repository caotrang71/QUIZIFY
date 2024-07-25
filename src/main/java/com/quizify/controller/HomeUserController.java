package com.quizify.controller;

import com.quizify.model.Notifications;
import com.quizify.service.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
@Controller
public class HomeUserController {
    @Autowired
    private NotificationsService notificationsService;
    @GetMapping("/userHome")
    public String viewHomeUser(Model model) {
        List<Notifications> notifications = notificationsService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        return "HomePage";
    }

    @GetMapping("/")
    public String viewLandingPage(Model model) {
        return "landing-page";
    }

}
