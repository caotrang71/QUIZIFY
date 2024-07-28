package com.quizify.controller;

import com.quizify.model.Notifications;
import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.model.Vote;
import com.quizify.repository.VoteRepository;
import com.quizify.service.NotificationsService;
import com.quizify.service.QuizBankService;
import com.quizify.service.VoteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class HomeUserController {
    @Autowired
    private NotificationsService notificationsService;
    @Autowired
    private QuizBankService quizBankService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteRepository voteRepository;

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
        Map<Long, Double> averageStars = new HashMap<>();

        for (QuizBank quiz : quizBanks) {
            List<Vote> votes = voteRepository.findByQuizBanksID(quiz.getId());
            double sum = 0;

            for (Vote star : votes) {
                sum += star.getStar();
            }

            double average = votes.isEmpty() ? 0.0 : (sum / votes.size());
            BigDecimal bd = new BigDecimal(average).setScale(1, RoundingMode.HALF_UP);
            averageStars.put(quiz.getId(), bd.doubleValue());
        }

        model.addAttribute("averageStars", averageStars);

        model.addAttribute("averageStar", averageStars);
        model.addAttribute("quizBanks", quizBanks);
        model.addAttribute("notifications", notifications);
        return "HomePage";
    }



    @GetMapping("/")
    public String viewLandingPage() {
        return "landing-page";
    }
}
