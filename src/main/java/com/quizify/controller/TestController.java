package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/tests")
public class TestController {
    @Autowired
    private QuizBankService quizBankService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionChoiceService questionChoiceService;
    @Autowired
    private TestService testService;
    @Autowired
    private UserService userService;
    @Autowired
    private TestHistoryService testHistoryService;

    @PostMapping("/create")
    public String createTest(@RequestParam Long quizBankId, @RequestParam int numberOfQuestions, Model model) {
        Long userId = 3L; // Hardcoded user ID

        try {
            Test test = testService.createTest(userId, quizBankId, numberOfQuestions);
            model.addAttribute("test", test);
            return "redirect:/tests/take/" + test.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "quiz_bank_detail"; // Adjust this based on your error handling page
        }
    }

    @GetMapping("/take/{testId}")
    public String takeTest(@PathVariable Long testId, Model model) {
        Test test = testService.getTestById(testId);
        model.addAttribute("test", test);
        return "take_test";
    }

    @PostMapping("/submit")
    public String submitTest(@RequestParam Long testId, @RequestParam List<Long> selectedChoiceIds, Model model) {
        try {
            Test test = testService.submitTest(testId, selectedChoiceIds);
            model.addAttribute("test", test);
            return "result";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "take_test";
        }
    }

    @GetMapping("/detail/{testId}")
    public String testDetail(@PathVariable Long testId, Model model) {
        Test test = testService.getTestById(testId);
        List<TestHistory> testHistories = testHistoryService.getTestHistoriesByTest(test);
        model.addAttribute("test", test);
        model.addAttribute("testHistories", testHistories);
        return "test_detail";
    }


}
