package com.quizify.controller;

import com.quizify.model.User;
import com.quizify.service.*;
import com.quizify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StatisticsController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/statistics")
    public String getStatistics(Model model){
        List<User> userList = userRepository.findAll();
        Map<String, Long> weeklyResults = userList.stream()
                .collect(Collectors.groupingBy(
                        result -> result.getCreatedAt().with(DayOfWeek.MONDAY).format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.counting()
                ));
        System.out.println("All Results: " + userList);  // Kiểm tra xem có kết quả nào không
        model.addAttribute("weeklyResults", weeklyResults);
        return "aaaaaaaaaaaaaaaaaa_test_thong_ke";
    }
}
