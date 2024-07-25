package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    @GetMapping("/my-practice")
    public String myPractice(Model model) {
        List<Test> tests = testService.getAllTests();
        model.addAttribute("tests", tests);
        return "my-practice";
    }

    @PostMapping("/create")
    public String createTest(@RequestParam Long quizBankId,
                             @RequestParam String title,
                             @RequestParam(value = "timeLimit", required = false) Integer timeLimit,
                             @RequestParam int numberOfQuestions,
                             @RequestParam String questionOrder,
                             @RequestParam(name = "shuffleChoices", defaultValue = "false") boolean shuffleChoices,
                             @RequestParam(name = "enableTimeLimit", defaultValue = "false") boolean enableTimeLimit,
                             Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        Long userId = user.getId(); // Replace with your actual user ID logic
        System.out.println("user id: " + userId);
        try {
            boolean shuffleQuestions = questionOrder.equals("shuffle");
            System.out.println("enableTimeLimit: "+enableTimeLimit);
            System.out.println("timeLimit: "+timeLimit);
            if (!enableTimeLimit) {
                timeLimit = null;
            }
            Test test = testService.createTest(userId, quizBankId, title, numberOfQuestions, timeLimit, shuffleQuestions, shuffleChoices);
            return "redirect:/tests/take/" + test.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Error creating test: " + e.getMessage());
            System.err.println("Error creating test: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/quiz-banks/quiz-bank-detail/" + quizBankId;
        }
    }


    @GetMapping("/take/{testId}")
    public String takeTest(@PathVariable Long testId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (testService.isTestSubmitted(testId)) {
            redirectAttributes.addFlashAttribute("message", "Test has already been submitted. You can retake the test.");
            return "redirect:/tests/my-practice";
        }
        Test test = testService.getTestById(testId);
        List<TestHistory> history = testHistoryService.getTestHistoriesByTest(test);
        System.out.print("Thu tu dap an: ");
        for(TestHistory historyItem : history) {
            for(QuestionChoice questionChoice : historyItem.getQuestion().getQuestionChoices()) {
                System.out.println(questionChoice.getId());
            }

        }

        model.addAttribute("test", test);
        return "take-test";
    }

//    @PostMapping("/submit")
//    public String submitTest(@RequestParam Long testId,
//                             @RequestParam Map<String, String> allParams,
//                             Model model) {
//        try {
//            List<Long> selectedChoiceIds = new ArrayList<>();
//            Test test = testService.getTestById(testId);
//            if (test == null) {
//                throw new Exception("Test not found");
//            }
//            test.getTestHistories().forEach(history -> {
//                String choiceParam = allParams.get("question-" + history.getQuestion().getId());
//                selectedChoiceIds.add(choiceParam != null ? Long.parseLong(choiceParam) : null);
//            });
//
//            test = testService.submitTest(testId, selectedChoiceIds);
//            model.addAttribute("test", test);
//            return "redirect:/tests/my-practice";
//        } catch (Exception e) {
//            model.addAttribute("error", "Error submitting test: " + e.getMessage());
//            return "take-test";
//        }
//    }

    @PostMapping("/submit")
    public String submitTest(@RequestParam Long testId, @RequestParam Map<String, String> allParams, Model model) {
        try {
            Test test = testService.getTestById(testId);
            if (test == null) {
                throw new Exception("Test not found");
            }

            List<Long> selectedChoiceIds = test.getTestHistories().stream()
                    .map(history -> {
                        String paramKey = "question-" + history.getQuestion().getId();
                        String choiceId = allParams.get(paramKey);
                        return choiceId != null ? Long.valueOf(choiceId) : null;
                    })
                    .collect(Collectors.toList());

            test = testService.submitTest(testId, selectedChoiceIds);
            model.addAttribute("test", test);
            return "redirect:/tests/my-practice";
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Invalid input. Please select valid choices.");
            return "take-test";
        } catch (Exception e) {
            model.addAttribute("error", "Error submitting test: " + e.getMessage());
            return "take-test";
        }
    }



    @GetMapping("/detail/{testId}")
    public String testDetail(@PathVariable Long testId, Model model) {
        Test test = testService.getTestById(testId);
        List<TestHistory> testHistories = testHistoryService.getTestHistoriesByTest(test);
        model.addAttribute("test", test);
        model.addAttribute("testHistories", testHistories);
        return "test-detail";
    }

    @PostMapping("/exit")
    public String exitTest(@RequestParam Long testId, RedirectAttributes redirectAttributes) {
        try {
            testService.markTestInProgress(testId);
            redirectAttributes.addFlashAttribute("message", "Test has been saved. You can continue later.");
            return "redirect:/tests/my-practice";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error exiting test: " + e.getMessage());
            return "redirect:/tests/take/" + testId;
        }
    }

}
