package com.quizify.controller;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import com.quizify.model.QuizBank;
import com.quizify.repository.QuestionRepository;
import com.quizify.repository.QuizBankRepository;
import com.quizify.service.QuestionService;
import com.quizify.service.QuizBankService;
import com.quizify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizBankService quizBankService;

//    @GetMapping("/quiz-banks/update-quiz-banks/{id}")
//    public String deleteQuestion(@PathVariable(value="id") long id, Model model) {
//        QuizBank quizBank = quizBankService.getQuizBankById(id);
////        List<Question> questions = questionService.getQuestionsByQuizBank(quizBank);
////        Map<Question, List<QuestionChoice>> questionChoicesMap = new HashMap<>();
//        Question question = questionService.
//    }
}
