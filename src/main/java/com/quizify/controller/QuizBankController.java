package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.service.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz-banks")
public class QuizBankController {
    @Autowired
    private QuizBankService quizBankService;

//    @Autowired
//    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionChoiceService questionChoiceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ExcelService excelService;

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
    public String getDetailQuizBank(@PathVariable(value="id") long id, Model model,  HttpServletResponse response){
        QuizBank quizBank = quizBankService.getQuizBankById(id);
        List<Question> questions = questionService.getQuestionsByQuizBank(quizBank);
        Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
        for(Question question : questions) {
            List<QuestionChoice> questionChoices = questionChoiceService.getQuestionChoiceByQuestion(question);
            questionChoicesMap.put(question, questionChoices);
        }
        System.out.println("Số câu hỏi: "+questionChoicesMap.size());
        model.addAttribute("totalQuestions", questionChoicesMap.size());
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

    @PostMapping("/import-excel")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<Question> questions = excelService.parseExcelFile(file);
            Map<String, Object> response = new HashMap<>();
            for (Question question : questions) {
                System.out.println("Controller: Added question: " + question.getContent() + " with choices: " + question.getQuestionChoices().size());
            }
            response.put("success", true);
            response.put("questions", questions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/created")
    public String createQuizBank(@ModelAttribute QuizBank quizBank, BindingResult result, @RequestParam("questionImage") List<MultipartFile> questionImages, Model model) {
        if (result.hasErrors()) {
            return "create-quiz-bank";
        }

        if (quizBank.getQuestions() == null || quizBank.getQuestions().isEmpty()) {
            model.addAttribute("error", "You must add at least one question!");
            return "create-quiz-bank";
        }

        boolean tontai = true;

        if(questionImages != null && !questionImages.isEmpty()) {
            tontai = true;
        }
        System.out.println("File anh ton tai " + tontai);

        for (int i = 0; i < quizBank.getQuestions().size(); i++) {
            Question question = quizBank.getQuestions().get(i);
            MultipartFile imageFile = questionImages.get(i);

            if (!imageFile.isEmpty()) {
                try {
                    String fileName = fileStorageService.storeFile(imageFile);
                    question.setImage(fileName);
                } catch (RuntimeException e) {
                    model.addAttribute("error", "File upload failed: " + e.getMessage());
                    return "create-quiz-bank";
                }
            }

            for (QuestionChoice choice : question.getQuestionChoices()) {
                choice.setQuestion(question);
            }
        }
//
        QuizBank newQuizBank = quizBankService.createQuizBank(quizBank);


        model.addAttribute("success", true);
        return "redirect:/quiz-banks/quiz-bank-detail/" + newQuizBank.getId();
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
            question.setId(null);
            for (QuestionChoice choice : question.getQuestionChoices()) {
                choice.setId(null);
                choice.setQuestion(question);
            }
        }

        // Save the QuizBank, Questions, and Choices
        quizBankService.updateQuizBank(quizBank);

        model.addAttribute("success", true);
        return "redirect:/quiz-banks/quiz-bank-detail/"+quizBank.getId();
    }


    @GetMapping("/delete-quiz-bank/{id}")
    public String deleteQuizBank(@PathVariable("id") Long id, Model model) {
        quizBankService.deleteQuizBankById(id);
        return "redirect:/quiz-banks/quiz-banks-list";
    }


}

