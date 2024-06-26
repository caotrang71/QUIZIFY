package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping("/tests-list")
    public String listTests(Model model) {
        List<Test> tests = testService.getAllTests();
        model.addAttribute("tests", tests);
        return "test-list";
    }

    @PostMapping("/create")
    public String createTest(@ModelAttribute("test") Test test,@RequestParam Long quizBankId, @RequestParam int numberOfQuestions, Model model) {
        Long userId = 3L;
        try {
            test = testService.createTest(userId, quizBankId, numberOfQuestions);
//            List<TestHistory> testHistory = test.getTestHistories();
//            List<Question> questions = new ArrayList<>();
//            for (TestHistory testHistoryItem : testHistory) {
//                questions.add(testHistoryItem.getQuestion());
//            }
//            Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
//            for(Question question : questions) {
//                List<QuestionChoice> questionChoices = questionChoiceService.getQuestionChoiceByQuestion(question);
//                questionChoicesMap.put(question, questionChoices);
//            }
//            model.addAttribute("questions", questions);
//            model.addAttribute("questions", questions);
//            model.addAttribute("questionChoicesMap", questionChoicesMap);
            model.addAttribute("test", test);
            System.out.println("Test created successfully with ID: {}"+ test.getId());

            return "redirect:/tests/take/" + test.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            System.out.println("Error creating test: {}"+ e.getMessage()+ e);
            return "redirect:/quiz-banks/quiz-bank-detail/" + quizBankId;
        }
    }

    @GetMapping("/take/{testId}")
    public String takeTest(@PathVariable(value="testId") Long testId, Model model) {
        Test test = testService.getTestById(testId);
        model.addAttribute("test", test);
        return "take-test";
    }

    @PostMapping("/submit")
    public String submitTest(@RequestParam Long testId, @RequestParam Map<String, String> allParams, Model model) {
        try {
            List<Long> selectedChoiceIds = new ArrayList<>();
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("question-")) {
                    selectedChoiceIds.add(Long.parseLong(entry.getValue()));
                }
            }
            Test test = testService.submitTest(testId, selectedChoiceIds);
            model.addAttribute("test", test);
            return "result";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "take-test";
        }
    }


//    @PostMapping("/submit")
//    public String submitTest(@RequestParam Long testId, Model model) {
//        try {
//            Test test = testService.submitTest(testId);
//            model.addAttribute("test", test);
//            return "result";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return "take-test";
//        }
//    }

    @GetMapping("/detail/{testId}")
    public String testDetail(@PathVariable Long testId, Model model) {
        Test test = testService.getTestById(testId);
        List<TestHistory> testHistories = testHistoryService.getTestHistoriesByTest(test);
        model.addAttribute("test", test);
        model.addAttribute("testHistories", testHistories);
        return "test-detail";
    }


}
