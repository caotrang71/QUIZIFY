package com.quizify.controller;

import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.service.SubcategoryService;
import com.quizify.service.UserService;
import com.quizify.service.QuizBankService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/quiz-banks")
public class QuizBankController {
    @Autowired
    private final QuizBankService quizBankService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubcategoryService subcategoryService;

    public QuizBankController(QuizBankService quizBankService) {
        this.quizBankService = quizBankService;
    }

    @GetMapping("/homePage")
    public String homePage(Model model) {
        return "quiz-banks/home-page";
    }
    @GetMapping("/quizList")
    public String listUserQuizBanks(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("createdQuizBanks", quizBankService.getUserCreatedQuizBanks(currentUser));
        model.addAttribute("joinedQuizBanks", quizBankService.getPublicQuizBanks(currentUser));
        return "quiz-banks/quiz-banks-list";
    }

    @GetMapping
    public ModelAndView listQuizBanks(Model model) {
        List<QuizBank> list = quizBankService.getAllQuizBank();
        return new ModelAndView("quiz-banks/quiz-banks-list","bank",list);
    }

    @GetMapping("/create")
    public String createQuizBankForm(Model model) {
        model.addAttribute("quizBank", new QuizBank());
        model.addAttribute("subcategories", subcategoryService.getAllSubcategories());
        return "quiz-banks/create";
    }

    @PostMapping("/create")
    public String createQuizBank(@ModelAttribute QuizBank quizBank, BindingResult result, Model model) {
        User currentUser = userService.getCurrentUser();
        quizBank.setCreatedBy(currentUser);
        try {
            quizBankService.createQuizBank(quizBank);
            return "redirect:/quiz-banks";
        } catch (IllegalArgumentException e) {
            result.rejectValue("questions", "error.quizBank", e.getMessage());
            return "quiz-banks/create";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteQuizBank(@PathVariable Long id) {
        quizBankService.deleteQuizBank(id);
        return "redirect:/quiz-banks";
    }

}

