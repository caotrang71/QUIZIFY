package com.quizify.controller;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import com.quizify.model.QuizBank;
import com.quizify.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz-banks")
public class QuizBankController {
    @Autowired
    private QuizBankService quizBankService;

//    @Autowired
//    private UserService userService;

    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionChoiceService questionChoiceService;

    //view list of quiz banks
    @GetMapping("/quiz-banks-list")
    public String quizBankList(Model model) {
        List<QuizBank> quizBanksList = quizBankService.getAllQuizBanks();
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

    @GetMapping("/")
    public String viewQuizUserHomePage(Model model) {
        model.addAttribute("quizBanksList", quizBankService.getAllQuizBanks());
        return "quiz-bank-list";
    }

//    @GetMapping("/")
//    public String viewQuizBankListPage(Model model) {
//        return findPaginated(1, "bankName", "asc", model);
//    }

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

        // Ensure there's at least one question
        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "create-quiz-bank";
        }

        // Ensure each question has exactly 4 choices
        for (Question question : quizBank.getQuestions()) {
            if (question.getQuestionChoices() == null || question.getQuestionChoices().size() != 4) {
                model.addAttribute("error", "Each question must have exactly 4 choices.");
                return "create-quiz-bank";
            }
            for (QuestionChoice choice : question.getQuestionChoices()) {
                System.out.println(choice.getCorrectOrNot());
                choice.setQuestion(question);  // Set the parent question reference
            }
        }

        // Save the QuizBank, Questions, and Choices
        quizBankService.createQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/";
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
        // Save quiz bank to db
        if (result.hasErrors()) {
            return "update-quiz-bank";
        }

        // Ensure there's at least one question
        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "update-quiz-bank";
        }

        // Ensure each question has exactly 4 choices
        for (Question question : quizBank.getQuestions()) {
            if (question.getQuestionChoices() == null || question.getQuestionChoices().size() != 4) {
                model.addAttribute("error", "Each question must have exactly 4 choices.");
                return "update-quiz-bank";
            }
            // Clear the ID of the question to ensure it's treated as a new question when saving
            question.setId(null);
            for (QuestionChoice choice : question.getQuestionChoices()) {
                // Clear the ID of the choice to ensure it's treated as a new choice when saving
                choice.setId(null);
                choice.setQuestion(question);  // Set the parent question reference
            }
        }

        // Save the QuizBank, Questions, and Choices
        quizBankService.updateQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/";
    }


//    @GetMapping("/delete-quiz-bank/{id}")
//    public String deleteQuizBank(@PathVariable(value="id") long id, Model model) {
//        this.quizBankService.deleteQuizBankById(id);
//        return "redirect:/quiz-banks/";
//    }

    @GetMapping("/delete-quiz-bank/{id}")
    public String deleteQuizBank(@PathVariable("id") Long id, Model model) {
        quizBankService.deleteQuizBankById(id);
        return "redirect:/quiz-banks/";
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value="pageNo") int pageNo, @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir, Model model) {

        int pageSize = 5;

        Page<QuizBank> page = quizBankService.findPaginated(pageNo,pageSize, sortField, sortDir);
        List<QuizBank> quizBanksList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("quizBanksList", quizBanksList);

        return "quiz-bank-list";
    }


}

