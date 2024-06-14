package com.example.project.controller;

import com.example.project.Repository.quiz_bank_Repository;
import com.example.project.Service.quiz_banks_Service;
import com.example.project.entity.quiz_banks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/show")
public class quiz_banks_Controller {
    @Autowired
    private quiz_bank_Repository quizBankRepository;
    @Autowired
    private quiz_banks_Service quizBanksService;

    @GetMapping("/quiz_banks")
    public String showAllQuizBanks(Model model){
        List<quiz_banks> ListQuizBanks = quizBanksService.getAllListQuizBanks();
        model.addAttribute("ListQuiz", ListQuizBanks);
        return "quiz_banks_page";
    }
    @DeleteMapping("/deleteQuiz_banks/{id}")
    @ResponseBody
    public void DeleteQuiz_Banks(@PathVariable int id){
        quizBanksService.deleteQuizBanks(id);
    }
}
