package com.quizify.controller;

import com.quizify.model.Notifications;
import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.service.NotificationsService;
import com.quizify.service.QuizBankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
public class HomeUserController {
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private QuizBankService quizBankService;

    @GetMapping("/userHome")
    public String viewHomeUser(Model model, HttpSession session
                               ) {
//        User user = (User) session.getAttribute("user");
//        List<Notifications> notifications = notificationsService.getNotificationsByReceiver(user);
//
        List<Notifications> notifications = notificationsService.getAllNotifications();
        List<QuizBank> quizBanks = quizBankService.getPublicQuizBanks();
//        if(keyword != null) {
//            List<QuizBank> quizBanks = quizBankService.searchQuizBank(keyword);
//            model.addAttribute("quizBanks", quizBanks);
//        }else {
//            model.addAttribute("quizBanks", List.of()); // Empty list if no keyword is provided
//        }
        model.addAttribute("quizBanks", quizBanks);
        model.addAttribute("notifications", notifications);
        return "HomePage";
    }



    @GetMapping("/")
    public String viewLandingPage() {
        return "landing-page";
    }
}
