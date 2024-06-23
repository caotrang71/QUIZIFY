package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz-banks")
public class QuizBankController {
    @Autowired
    private QuizBankService quizBankService;

    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionChoiceService questionChoiceService;
    @Autowired
    private CategoryService categoryService;

    //view list of quiz banks
    @GetMapping("/quiz-banks-list")
    public String quizBankList(Model model, @Param("keyword") String keyword) {
        List<QuizBank> quizBanksList = quizBankService.getAllQuizBanks();
        if (keyword != null && !keyword.isEmpty()) {
            quizBanksList = this.quizBankService.searchQuizBank(keyword);
            model.addAttribute("keyword", keyword);
        }
        model.addAttribute("quizBanksList", quizBanksList);
        return "quiz-bank-list";
    }

    @GetMapping("/quiz-bank-detail/{id}")
    public String getDetailQuizBank(@PathVariable(value="id") long id, Model model){
        QuizBank quizBank = quizBankService.getQuizBankById(id);
        List<Question> questions = questionService.getQuestionsByQuizBank(quizBank);
        Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
        for(Question question : questions) {
            List<QuestionChoice> questionChoices = questionChoiceService.getQuestionChoiceByQuestion(question);
            questionChoicesMap.put(question, questionChoices);
        }
        model.addAttribute("quizBank", quizBank);
        model.addAttribute("questions", questions);
        model.addAttribute("questionChoicesMap", questionChoicesMap);
        return "quiz-bank-detail";
    }

    //form to create quiz bank
    @GetMapping("/create-quiz-bank")
    public String createQuizBank(Model model) {
        model.addAttribute("quizBank", new QuizBank());
        return "create-quiz-bank";
    }

    @PostMapping("/created")
    public String createQuizBank(@ModelAttribute QuizBank quizBank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-quiz-bank";
        }

        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "create-quiz-bank";
        }

        for (Question question : quizBank.getQuestions()) {
//            if (question.getQuestionChoices() == null || question.getQuestionChoices().size() != 4) {
//                model.addAttribute("error", "Each question must have exactly 4 choices.");
//                return "create-quiz-bank";
//            }
            for (QuestionChoice choice : question.getQuestionChoices()) {
                System.out.println(choice.getCorrectOrNot());
                choice.setQuestion(question);
            }
        }

        quizBankService.createQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/quiz-banks-list";
    }

    @ModelAttribute("categoryList")
    public List<Category> getCategories() {
        return categoryService.getAllCategories()
                .stream().map(item -> {
            Category category = new Category();
            BeanUtils.copyProperties(item, category);
            return category;
        }).collect(Collectors.toList());
    }

    @ModelAttribute("subcategoryList")
    public List<Subcategory> getSubcategories() {
        return subcategoryService.getAllSubcategories()
                .stream().map(item -> {
            Subcategory subcategory = new Subcategory();
            BeanUtils.copyProperties(item, subcategory);
            return subcategory;
        }).collect(Collectors.toList());
    }

    //form to update quiz bank
    @GetMapping("/update-quiz-bank/{id}")
    public String updateQuizBank(@PathVariable(value="id") long id, Model model) {
        QuizBank quizBank = quizBankService.getQuizBankById(id);
        List<Question> questions = questionService.getQuestionsByQuizBank(quizBank);
        Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
        for(Question question : questions) {
            List<QuestionChoice> questionChoices = questionChoiceService.getQuestionChoiceByQuestion(question);
            questionChoicesMap.put(question, questionChoices);
        }
        model.addAttribute("quizBank", quizBank);
        model.addAttribute("questions", questions);
        model.addAttribute("questionChoicesMap", questionChoicesMap);
        return "update-quiz-bank";
    }

    //back to list after saved updating successfully
    @PostMapping("/saved")
    public String saveQuizBank(@ModelAttribute("quizBank") QuizBank quizBank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-quiz-bank";
        }
        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "update-quiz-bank";
        }
        for (Question question : quizBank.getQuestions()) {
//            if (question.getQuestionChoices() == null || question.getQuestionChoices().size() != 4) {
//                model.addAttribute("error", "Each question must have exactly 4 choices.");
//                return "update-quiz-bank";
//            }
            question.setId(null);
            for (QuestionChoice choice : question.getQuestionChoices()) {
                choice.setId(null);
                choice.setQuestion(question);
            }
        }

        // Save the QuizBank, Questions, and Choices
        quizBankService.updateQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/quiz-banks-list";
    }


    @GetMapping("/delete-quiz-bank/{id}")
    public String deleteQuizBank(@PathVariable("id") Long id, Model model) {
        quizBankService.deleteQuizBankById(id);
        return "redirect:/quiz-banks/quiz-banks-list";
    }


}

