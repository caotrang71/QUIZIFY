package com.quizify.controller;

import com.quizify.model.Test;
import com.quizify.model.User;
import com.quizify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.TreeMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller

public class StatisticsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRepository testRepository;
    //return data statistics , data user
    @GetMapping("/api/data/chart")
    @ResponseBody
    public Map<String, Long> getChartData() {
        List<User> userList = userRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Map<String, Long> weeklyResults = userList.stream()
                .collect(Collectors.groupingBy(
                        user -> {
                            LocalDate createdAt = user.getCreatedAt().toLocalDate();
                            LocalDate startOfWeek = createdAt.with(DayOfWeek.MONDAY);
                            LocalDate endOfWeek = startOfWeek.plusDays(6);
                            return startOfWeek.format(formatter) + " to " + endOfWeek.format(formatter);
                        },
                        TreeMap::new, // Use TreeMap to sort keys by natural order
                        Collectors.counting()
                ));
        return weeklyResults;
    }
    //data take quiz
    @GetMapping("/api/data/chart/take-quiz")
    @ResponseBody
    public Map<String, Long> getChartDataTakeQuiz() {
        List<Test> testList = testRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Map<String, Long> weeklyResults = testList.stream()
                .collect(Collectors.groupingBy(
                        user -> {
                            LocalDate createdAt = user.getStartedAt().toLocalDate();
                            LocalDate startOfWeek = createdAt.with(DayOfWeek.MONDAY);
                            LocalDate endOfWeek = startOfWeek.plusDays(6);
                            return startOfWeek.format(formatter) + " to " + endOfWeek.format(formatter);
                        },
                        TreeMap::new, // Use TreeMap to sort keys by natural order
                        Collectors.counting()
                ));
        return weeklyResults;
    }

    //trả về trang thống kê
    @GetMapping("/show/statistics")
    public String showChart(Model model) {
        List<User> userList = userRepository.findAll();
        List<Test> testList = testRepository.findAll();

        String totalUser = "total: "+userList.size()+" people have used quizify";
        String totaTakeQuiz = "total: "+testList.size()+" people have taken quizify";
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("totalTakeQuiz", totaTakeQuiz);

        return "statistics";
    }
}
